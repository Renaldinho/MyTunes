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
            preparedStatement.setInt(3, lastRank(playListId) + 1);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * returns the rank of a song we add to a given playList
     * we need the rank for moving songs up and down.
     */
    public int lastRank(int playListId) throws SQLException {
        int rank =0;
        String sql = "SELECT Rank FROM song_playlist where [PlayList Id] = ? ORDER BY Rank ASC";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playListId);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                rank =resultSet.getInt("Rank");
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
        if(songRank==1)
            switchFirstLast(playListId);
        else {
            String sql = "UPDATE song_playlist SET Rank = CASE Rank WHEN ? THEN ? WHEN ? THEN ? ELSE Rank END WHERE [playList Id]=?";
            try (Connection connection =databaseConnector.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,songRank);
                preparedStatement.setInt(2,songRank-1);
                preparedStatement.setInt(3,songRank-1);
                preparedStatement.setInt(4,songRank);
                preparedStatement.setInt(5,playListId);
            }
        }
    }

    @Override
    public void moveSongUp(int playListId, int songRank) throws SQLException {
        if (songRank == lastRank(playListId)) {
            switchFirstLast(playListId);
        } else {
            String sql ="UPDATE song_playlist SET Rank = CASE Rank WHEN ? THEN ? WHEN ? THEN ? ELSE Rank END WHERE [playList Id]=?";
            try (Connection connection = databaseConnector.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,songRank);
                preparedStatement.setInt(2,songRank+1);
                preparedStatement.setInt(3,songRank+1);
                preparedStatement.setInt(4,songRank);
                preparedStatement.setInt(5,playListId);
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
    public void switchFirstLast( int playListId) throws SQLException {
        String sql ="UPDATE song_playlist SET Rank = CASE Rank WHEN ? THEN ? WHEN ? THEN ? ELSE Rank END WHERE [playList Id]=?";
        try (Connection connection = databaseConnector.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,1);
            preparedStatement.setInt(2, lastRank(playListId));
            preparedStatement.setInt(3, lastRank(playListId));
            preparedStatement.setInt(4,1);
            preparedStatement.setInt(5,playListId);
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
