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
        if (playListNameTakenAlready(name)){
            System.out.println("This name already exists. Please find another one for your new playlist or delete the old one.");
        }
     else {   try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                playList = new PlayList(id, name);
            }
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
        String sql = "SELECT * FROM playlists";
        try (Connection connection = databaseConnector.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                PlayList playList = new PlayList(id, name);
                allPlayLists.add(playList);
            }
        }
        return allPlayLists;
    }
    private boolean playListNameTakenAlready(String name)throws SQLException{
        String sql ="SELECT * FROM playlists WHERE Name= ?";
        try (Connection connection = databaseConnector.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            return resultSet.next();
        }
    }
}