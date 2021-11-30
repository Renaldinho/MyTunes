package dal.dao;

import be.Category;
import be.Song;
import dal.DatabaseConnector;
import dal.Interfaces.ISongDAO;

import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO implements ISongDAO {
    DatabaseConnector databaseConnector;

    public SongDAO() {
        databaseConnector = new DatabaseConnector();
    }

    @Override
    public List<Song> getAllSongs() throws SQLException {
        List<Song> allSongs = new ArrayList<>();
        String sql = "SELECT * FROM songs";
        try (Connection connection = databaseConnector.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int artistId = resultSet.getInt("Artist");
                ArtistsDAO artistsDAO = new ArtistsDAO();
                String artist = artistsDAO.getArtistById(artistId).getName();
                int categoryId = resultSet.getInt("Category");
                CategoriesDAO categoriesDAO = new CategoriesDAO();
                String category = categoriesDAO.getCategoryById(categoryId).getCategoryName();
                int time = resultSet.getInt("Time");
                String title = resultSet.getString("Title");
                int id = resultSet.getInt("Id");
                String filePath = resultSet.getString("FilePath");
                Song song = new Song(id, title, artist, category, time, filePath);
                allSongs.add(song);
            }
        }
        return allSongs;
    }


    @Override
    public Song createSong(String title, String artist, String category, String filePath) throws SQLException {
        Song song;
        int id = 0;
        ArtistsDAO artistsDAO = new ArtistsDAO();
        int artistId = artistsDAO.createArtist(artist);
        CategoriesDAO categoriesDAO = new CategoriesDAO();
        int categoryId = categoriesDAO.createNewCategory(category);
        String sql = ("INSERT INTO songs VALUES (?,?,?,?,?");
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setInt(2, artistId);
            statement.setInt(3, categoryId);
            statement.setInt(4, getSongTime(Path.of(filePath)));
            statement.setString(5, filePath);
            int created = statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            if (created != 0) {
                song = new Song(id, title, artist, category, getSongTime(Path.of(filePath)), filePath);
            } else song = null;
        }
        return song;
    }

    @Override
    public void deleteSong(String title) throws SQLException {
        String sql = "DELETE FROM songs WHERE title = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateSong(String title, String newTitle, String newArtist, String newCategory) throws SQLException {
        String sql = "UPDATE songs SET Title = ?,Artist = ?, Category= ? WHERE Title=? ";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newTitle);
            statement.setString(2, newArtist);
            statement.setString(3, newCategory);
            statement.setString(4, title);
            statement.executeUpdate();
        }
    }

    @Override
    public int getSongTime(Path filePath) {
        //to be implemented
        return 0;

    }


}
