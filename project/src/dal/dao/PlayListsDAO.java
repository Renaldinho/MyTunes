package dal.dao;

import be.PlayList;
import be.Song;
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
    public PlayList createPlayList(String name) throws Exception {
        PlayList playList = null;
        String sql = "INSERT INTO playlists VALUES (?,?,?) ";
        if (playListNameTakenAlready(name)){
            System.out.println("This name already exists. Please find another one for your new playlist or delete the old one.");
        }
      {   try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2,0);
            preparedStatement.setInt(3,0);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                playList = new PlayList(id, name,0,0);
            }
     }
        }
        return playList;
    }

    @Override
    public void deletePlayList(PlayList playList) throws Exception {
        deleteSongFromSong_PlayList(playList);
        String sql = "DELETE FROM playlists WHERE Name =?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, playList.getName());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<PlayList> getAllPlayLists() throws Exception {
        List<PlayList> allPlayLists = new ArrayList<>();
        String sql = "SELECT * FROM playlists";
        try (Connection connection = databaseConnector.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int songs= resultSet.getInt(3);
                int time = resultSet.getInt(4);
                PlayList playList = new PlayList(id, name,songs,time);
                allPlayLists.add(playList);
            }
        }
        return allPlayLists;
    }

    @Override
    public PlayList getPlayListById(int id) throws Exception {
            PlayList playList= null;
            String sql= "SELECT *  FROM playlists WHERE Id=?";
            try (Connection connection = databaseConnector.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,id);
                preparedStatement.execute();
                ResultSet resultSet = preparedStatement.getResultSet();
                if(resultSet.next()){
                    String name = resultSet.getString("Name");
                    int songs= resultSet.getInt(3);
                    int time = resultSet.getInt(4);
                    playList=new PlayList(id,name,songs,time);}

            }
            return playList;
        }


    private boolean playListNameTakenAlready(String name)throws Exception{
        String sql ="SELECT * FROM playlists WHERE Name= ?";
        try (Connection connection = databaseConnector.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            return resultSet.next();
        }
    }
    public void updatePlayList(PlayList playList,int song, int time) throws Exception{
        String sql = "UPDATE playlists SET Songs=?,Time=? WHERE Id = ?";
        try (Connection connection = databaseConnector.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,song);
            preparedStatement.setInt(2,time);
            preparedStatement.setInt(3,playList.getId());
            preparedStatement.executeUpdate();
        }
    }
    private void deleteSongFromSong_PlayList(PlayList playList) throws Exception{
        String sql ="DELETE FROM song_playlist WHERE [PlayList Id]=?";
        try (Connection connection  = databaseConnector.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,playList.getId());
            preparedStatement.executeUpdate();
        }
    }
}
