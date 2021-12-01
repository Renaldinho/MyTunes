package dal.dao;

import be.Song;
import dal.DatabaseConnector;
import dal.Interfaces.ISong_PlayListDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class Song_PlayListDAO implements ISong_PlayListDAO {
    DatabaseConnector databaseConnector;

    public Song_PlayListDAO() {
        databaseConnector = new DatabaseConnector();
    }

    @Override
    public void addSongToPlayList(int songId, int playListId) throws SQLException {
        String sql = "INSERT INTO song_playlist VALUES(?,?,?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, songId);
            preparedStatement.setInt(2, playListId);
            preparedStatement.setInt(3, lastRankInThePlayList(playListId) + 1);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * returns the rank of a song we add to a given playList
     * we need the rank for moving songs up and down.
     */
    private int lastRankInThePlayList(int playListId) throws SQLException {
        int rank = 0;
        String sql = "SELECT FROM song_playlist where [PlayList Id] = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playListId);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                rank = resultSet.getInt("Rank");
            }
        }
        return rank;
    }

    @Override
    public void removeSongFromPlayList(int songId, int playListId, int rank) throws SQLException {
        String sql = "DELETE FROM song_playlist WHERE [Song Id]=? AND [PlayList Id]=? AND Rank= ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, songId);
            preparedStatement.setInt(2, playListId);
            preparedStatement.setInt(3, rank);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Song> getAllSongsForGivenPlayList(int playListID, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
        List<Song> allSongsFromSamePlayList = new ArrayList<>();
        String sql = "SELECT FROM song_playlist WHERE PlayListId=? ORDERED BY Rank ASC";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playListID);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                int artistId = resultSet.getInt("Artist");
                String artist = artistsDAO.getArtistById(artistId).getName();
                int categoryId = resultSet.getInt("Category");
                String category = categoriesDAO.getCategoryById(categoryId).getCategoryName();
                int time = resultSet.getInt("Time");
                String title = resultSet.getString("Title");
                int id = resultSet.getInt("Id");
                String filePath = resultSet.getString("FilePath");
                Song song = new Song(id, title, artist, category, time, filePath);
                allSongsFromSamePlayList.add(song);
            }
        }
        return allSongsFromSamePlayList;
    }

    @Override
    public void moveSongDown(int playListId, int songRank) throws SQLException {
        boolean possible = false;  //check if there is any song from the same playlist that has a higher rank
        String sql0 = "SELECT FROM song_playlist WHERE PlayListId=? AND Rank>?ORDERED BY Rank ASC";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql0);
            preparedStatement.setInt(1, playListId);
            preparedStatement.setInt(2, songRank);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next() && !possible) {
                possible = resultSet.getInt("Rank") != 0;
            }
            if (possible) {
                String sql = "UPDATE song_playlist SET Rank = ? WHERE RANK=? AND [PlayList Id]= ?ORDERED BY Rank ASC";
                PreparedStatement preparedStatement0 = connection.prepareStatement(sql);
                preparedStatement0.setInt(1, songRank + 1);
                preparedStatement0.setInt(2, songRank);
                preparedStatement0.setInt(3, playListId);
                preparedStatement0.executeUpdate();
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
                preparedStatement1.setInt(1, songRank);
                preparedStatement1.setInt(2, songRank + 1);
                preparedStatement1.setInt(3, playListId);
                preparedStatement1.executeUpdate();
            } else
                switchFirstLast(0, lastRankInThePlayList(playListId), playListId);
        }
    }

    @Override
    public void moveSongUp(int playListId, int songRank) throws SQLException {
        if (songRank == 0) {
            switchFirstLast(0, lastRankInThePlayList(playListId), playListId);
        } else {
            String sql = "UPDATE song_playlist SET Rank = ? WHERE RANK=? AND [PlayList Id]= ?";
            try (Connection connection = databaseConnector.getConnection()) {
                PreparedStatement preparedStatement0 = connection.prepareStatement(sql);
                preparedStatement0.setInt(1, songRank - 1);
                preparedStatement0.setInt(2, songRank);
                preparedStatement0.setInt(3, playListId);
                preparedStatement0.executeUpdate();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, songRank);
                preparedStatement.setInt(2, songRank - 1);
                preparedStatement.setInt(3, playListId);
                preparedStatement.executeUpdate();
            }
        }

    }

    @Override
    public void deleteFromAllPlayLists(int songId) throws SQLException {
        String sql = "DELETE FROM song_playlist WHERE [Song Id]=?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, songId);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * this method is used when the user wants to push the last song down
     * or first song up.
     * It switches between them.
     */
    private void switchFirstLast(int firstSongId, int lastSongId, int playListId) throws SQLException {
        String sql = "UPDATE song_playlist SET Rank = ? WHERE [PlayList Id]= ? AND Rank=?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement0 = connection.prepareStatement(sql);
            preparedStatement0.setInt(1, lastRankInThePlayList(playListId));
            preparedStatement0.setInt(2, playListId);
            preparedStatement0.setInt(3, 0);
            preparedStatement0.executeUpdate();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, 0);
            preparedStatement.setInt(2, playListId);
            preparedStatement.setInt(3, lastRankInThePlayList(playListId));
            preparedStatement.executeUpdate();
        }
    }

    List<Integer> getRankSongInPlayList(int songId, int playListId) throws SQLException {
        List<Integer> allRankings = new ArrayList<>();
        String sql = "SELECT FROM song_playlist WHERE songId=? AND [playList Id]=?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, songId);
            preparedStatement.setInt(2, playListId);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                int ranking = resultSet.getInt(2);
                allRankings.add(ranking);
            }
        }
        return allRankings;
    }
}
