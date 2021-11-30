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

    //looks for a given artist and return his/her id, if not found just creates a new one and returns the generated key associated to it.
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
    public void deleteArtist(int id) throws SQLException {
        String sql = "DELETE FROM artists WHERE Id = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }

    }

    @Override
    public void updateArtist(int id, String name) throws SQLException {
        String sql = "UPDATE TABLE SET Name=? WHERE Id = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

        }
    }

    /**
     * this method returns how many song an artist is involved in. We need this to check while deleting a song.
     * If an artist has only one song that we want to delete, we clear him from database.
     */

    public int artistOccurrences(int artistId) throws SQLException {
        int occurrences = 0;
        String sql = "SELECT FROM songs WHERE Artist = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, artistId);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                occurrences += 1;
            }
        }
        return occurrences;
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
