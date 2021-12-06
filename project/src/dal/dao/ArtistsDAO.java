package dal.dao;

import be.Artist;
import be.Song;
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
    public int createArtist(String name) throws SQLException {
        int id = 0;
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
            preparedStatement1.setString(1,name);
            preparedStatement1.executeUpdate();
            ResultSet resultSet1 = preparedStatement1.getGeneratedKeys();
            while (resultSet1.next()) {
                id = resultSet1.getInt(1);
            }
        }
        return id;
    }

    @Override
    public void deleteArtist(Artist artist) throws SQLException {
        String sql = "DELETE FROM artists WHERE Id = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, artist.getId());
            preparedStatement.executeUpdate();
        }

    }

    @Override
    public void updateArtist(Artist artist, String name) throws SQLException {
        String sql = "UPDATE artists SET Category=? WHERE Id = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, artist.getId());
            preparedStatement.executeUpdate();}
    }

   /**
     * this method returns how many song an artist is involved in. We need this to check while deleting a song.
     * If an artist has only one song that we want to delete, we clear him from database.
     */
/*
    public int artistOccurrences(Artist artist) throws SQLException {
        int occurrences = 0;
        String sql = "SELECT * FROM songs WHERE Artist = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, artist.getId());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                occurrences += 1;
            }
        }
        return occurrences;
    }*/

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

    @Override
    public List<Artist> getAllArtists() throws SQLException {
        List<Artist>allArtists = new ArrayList<>();
        String sql = "SELECT * FROM artists ";
        try (Connection connection = databaseConnector.getConnection()){
            Statement statement = connection.createStatement();
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()){
                int id  = resultSet.getInt("Id");
                String name = resultSet.getString("Name");
                Artist artist = new Artist(id,name);
                allArtists.add(artist);

            }
        }
        return allArtists;
    }

}

