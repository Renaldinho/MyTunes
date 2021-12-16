package dal.dao;

import be.Artist;
import be.Song;
import bll.exceptions.ArtistException;
import dal.DatabaseConnector;
import dal.Interfaces.IArtistsDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistsDAO implements IArtistsDAO {
    DatabaseConnector databaseConnector;

    public ArtistsDAO() {
        databaseConnector = new DatabaseConnector();
    }

    //looks for a given artist and return his/her id, if not found just creates a new one and returns the generated key associated to it.
    @Override
    public int createArtist(String name) throws SQLException, ArtistException {
        int id = 0;
        checkName(name);
        String sql0 = "SELECT * FROM artists WHERE Name = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql0);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                id = resultSet.getInt("Id");
                return id;
            }
            String sql1 = "INSERT INTO artists VALUES (?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            preparedStatement1.setString(1, name);
            preparedStatement1.executeUpdate();
            ResultSet resultSet1 = preparedStatement1.getGeneratedKeys();
            while (resultSet1.next()) {
                id = resultSet1.getInt(1);
            }
        }
        return id;
    }

    @Override
    public Artist getArtistById(int artistId) throws SQLException {
        String sql = "SELECT * FROM artists WHERE Id=?";
        Artist artist = null;
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, artistId);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("name");
                artist = new Artist(id, name);
            }
        }
        return artist;
    }

    private void checkName(String artistName) throws ArtistException {
        Exception exception = new Exception();
        if(artistName.isEmpty())
            throw new ArtistException("Please find an artist for your song.",exception);
    }

}

