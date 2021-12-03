package dal.dao;

import be.PlayList;
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
    public void addSongToPlayList(Song song, PlayList playList) throws SQLException {
        String sql = "INSERT INTO song_playlist VALUES(?,?,?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, song.getId());
            preparedStatement.setInt(2, playList.getId());
            preparedStatement.setInt(3, lastRank(playList) + 1);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * returns the rank of a song we add to a given playList
     * we need the rank for moving songs up and down.
     */
    public int lastRank(PlayList playList) throws SQLException {
        int rank =0;
        String sql = "SELECT Rank FROM song_playlist where [PlayList Id] = ? ORDER BY Rank ASC";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playList.getId());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                rank =resultSet.getInt("Rank");
            }
        }
        return rank;
    }

    @Override
    public void removeSongFromPlayList(Song song, PlayList playList, int rank) throws SQLException {
        String sql = "DELETE FROM song_playlist WHERE [Song Id]=? AND [PlayList Id]=? AND Rank= ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, song.getId());
            preparedStatement.setInt(2, playList.getId());
            preparedStatement.setInt(3, rank);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Song> getAllSongsForGivenPlayList(PlayList playList, SongDAO songDAO,ArtistsDAO artistsDAO,CategoriesDAO categoriesDAO) throws SQLException {
        List<Song> allSongsFromSamePlayList = new ArrayList<>();
        String sql = "SELECT * FROM song_playlist WHERE [PlayList Id]=? ORDER BY Rank ASC";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playList.getId());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                int songId = resultSet.getInt("Song Id");
                allSongsFromSamePlayList.add(songDAO.getSongById(songId,artistsDAO,categoriesDAO));
            }
        }
        return allSongsFromSamePlayList;
    }

    @Override
    public void moveSongDown(PlayList playList, int songRank) throws SQLException {
        if(songRank==1)
            switchFirstLast(playList);
        else {
            String sql = "UPDATE song_playlist SET Rank = CASE Rank WHEN ? THEN ? WHEN ? THEN ? ELSE Rank END WHERE [playList Id]=?";
            try (Connection connection =databaseConnector.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,songRank);
                preparedStatement.setInt(2,songRank-1);
                preparedStatement.setInt(3,songRank-1);
                preparedStatement.setInt(4,songRank);
                preparedStatement.setInt(5,playList.getId());
            }
        }
    }

    @Override
    public void moveSongUp(PlayList playList, int songRank) throws SQLException {
        if (songRank == lastRank(playList)) {
            switchFirstLast(playList);
        } else {
            String sql ="UPDATE song_playlist SET Rank = CASE Rank WHEN ? THEN ? WHEN ? THEN ? ELSE Rank END WHERE [playList Id]=?";
            try (Connection connection = databaseConnector.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,songRank);
                preparedStatement.setInt(2,songRank+1);
                preparedStatement.setInt(3,songRank+1);
                preparedStatement.setInt(4,songRank);
                preparedStatement.setInt(5,playList.getId());
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public void deleteFromAllPlayLists(Song song) throws SQLException {
        String sql = "DELETE FROM song_playlist WHERE [Song Id]=?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, song.getId());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * this method is used when the user wants to push the last song down
     * or first song up.
     * It switches between them.
     */
    public void switchFirstLast( PlayList playList) throws SQLException {
        String sql ="UPDATE song_playlist SET Rank = CASE Rank WHEN ? THEN ? WHEN ? THEN ? ELSE Rank END WHERE [playList Id]=?";
        try (Connection connection = databaseConnector.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,1);
            preparedStatement.setInt(2, lastRank(playList));
            preparedStatement.setInt(3, lastRank(playList));
            preparedStatement.setInt(4,1);
            preparedStatement.setInt(5,playList.getId());
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
