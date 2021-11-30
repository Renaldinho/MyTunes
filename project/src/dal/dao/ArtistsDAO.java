package dal.dao;

import be.Artist;
import be.Song;
import dal.DatabaseConnector;
import dal.Interfaces.IArtistsDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtistsDAO implements IArtistsDAO {
    DatabaseConnector databaseConnector;

    public ArtistsDAO() {
        databaseConnector = new DatabaseConnector();
    }

    @Override
    public int createArtist(String name) throws SQLException {
        int id = 0;
        String sql0 = "SELECT FROM artists WHERE name = ?";
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
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            int created = preparedStatement1.executeUpdate();
            ResultSet resultSet1 = preparedStatement1.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        }
        return id;
    }

    @Override
    public void deleteArtist(String name) throws SQLException {
        String sql = "DELETE FROM artists WHERE Name = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        }

    }

    @Override
    public Artist getArtistById(int artistId) throws SQLException {
        String sql = "SELECT FROM artists WHERE Id=?";
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
}
