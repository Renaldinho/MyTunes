package dal.dao;

import be.PlayList;
import bll.exceptions.PlayListException;
import dal.DatabaseConnector;
import dal.Interfaces.IPlayListDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayListsDAO implements IPlayListDAO {
    DatabaseConnector databaseConnector;

    public PlayListsDAO() {
        databaseConnector = new DatabaseConnector();
    }

    @Override
    public PlayList createPlayList(String name) throws SQLException, PlayListException {
        PlayList playList = null;
        String sql = "INSERT INTO playlists VALUES (?,?,?) ";

        checkPlayListNameNotNull(name);
        checkPlayListName(name);

        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, 0);
            preparedStatement.setString(3, "00:00:00");
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                playList = new PlayList(id, name, 0, "00:00:00");
            }
        }
        return playList;
    }

    @Override
    public void deletePlayList(PlayList playList) throws SQLException {
        deleteSongFromSong_PlayList(playList);
        String sql = "DELETE FROM playlists WHERE Name =?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, playList.getName());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<PlayList> getAllPlayLists() throws SQLException {
        List<PlayList> allPlayLists = new ArrayList<>();
        String sql = "SELECT * FROM playlists";
        try (Connection connection = databaseConnector.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int songs = resultSet.getInt(3);
                String time = resultSet.getString(4);
                PlayList playList = new PlayList(id, name, songs, time);
                allPlayLists.add(playList);
            }
        }
        return allPlayLists;
    }

    @Override
    public PlayList getPlayList(int id) throws SQLException {
        PlayList playList = null;
        String sql = "SELECT *  FROM playlists WHERE Id=?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                String name = resultSet.getString("Name");
                int songs = resultSet.getInt(3);
                String time = resultSet.getString(4);
                playList = new PlayList(id, name, songs, time);
            }
        }
        return playList;
    }


    private boolean playListNameTakenAlready(String name) throws SQLException {
        String sql = "SELECT * FROM playlists WHERE Name= ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            return resultSet.next();
        }
    }

    public void updatePlayList(PlayList playList, String name, int song, String time) throws SQLException, PlayListException {
        String sql = "UPDATE playlists SET Name=?, Songs=?,Time=? WHERE Id = ?";
        checkPlayListNameNotNull(name);
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, song);
            preparedStatement.setString(3, time);
            preparedStatement.setInt(4, playList.getId());
            preparedStatement.executeUpdate();
        }
    }

    private void deleteSongFromSong_PlayList(PlayList playList) throws SQLException {
        String sql = "DELETE FROM song_playlist WHERE [PlayList Id]=?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playList.getId());
            preparedStatement.executeUpdate();
        }
    }

    private void checkPlayListName(String namePlayList) throws SQLException, PlayListException {
        Exception exception = new Exception();
        if (playListNameTakenAlready(namePlayList))
            throw new PlayListException("Name already taken.", exception);
    }

    private void checkPlayListNameNotNull(String playListName) throws PlayListException {
        Exception exception = new Exception();
        if (playListName.isEmpty())
            throw new PlayListException("Please find a name for your playlist.", exception);
    }
}
