package dal.dao;

import be.PlayList;
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
    public PlayList createPlayList(String name) throws SQLException {
        PlayList playList = null;
        String sql = "INSERT INTO playlists VALUES (?) ";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                playList = new PlayList(id, name);
            }
        }
        return playList;
    }

    @Override
    public void deletePlayList(String name) throws SQLException {
        String sql = "DELETE FROM playlists WHERE Name =?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<PlayList> getAllPlayLists() throws SQLException {
        List<PlayList> allPlayLists = new ArrayList<>();
        String sql = "SELECT * FROM playlists"; //should it have a ";" after "playlists" ? and should it be inside the try ?
        try (Connection connection = databaseConnector.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sql); // should be in an "if" ?
            ResultSet resultSet = statement.getResultSet(); // and this inside the "if" ? and all the "while"
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                PlayList playList = new PlayList(id, name);
                allPlayLists.add(playList);
            }
        }
        return allPlayLists;
    }
}
