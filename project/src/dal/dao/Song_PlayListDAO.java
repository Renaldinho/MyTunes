package dal.dao;

import dal.DatabaseConnector;
import dal.Interfaces.ISong_PlayListDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Song_PlayListDAO implements ISong_PlayListDAO {
    DatabaseConnector databaseConnector;
    public Song_PlayListDAO(){
        databaseConnector=new DatabaseConnector();
    }
    @Override
    public void addSongToPlayList(int songId,int playListId)throws SQLException {
        String sql="INSERT INTO song_playlist VALUES(?,?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,songId);
            preparedStatement.setInt(2,playListId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void removeSongFromPlayList(int songId,int playListId)throws SQLException {
        String sql="DELETE FROM song_playlist WHERE [Song Id]=? AND [PlayList Id]=?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,songId);
            preparedStatement.setInt(2,playListId);
            preparedStatement.executeUpdate();
        }
    }
}
