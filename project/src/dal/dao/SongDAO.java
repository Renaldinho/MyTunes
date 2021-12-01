package dal.dao;

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
    public List<Song> getAllSongs(ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
        List<Song> allSongs = new ArrayList<>();
        String sql = "SELECT * FROM songs";
        try (Connection connection = databaseConnector.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int artistId = resultSet.getInt("Artist");
                String artist = artistsDAO.getArtistById(artistId).getName();
                int categoryId = resultSet.getInt("Category");
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
    public Song createSong(String title, String artist, String category, String filePath, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
        Song song;
        int id = 0;
        int artistId = artistsDAO.createArtist(artist);
        int categoryId = categoriesDAO.createNewCategory(category);
        String sql = ("INSERT INTO songs VALUES (?,?,?,?,?)");
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
    public void deleteSong(int id, Song_PlayListDAO song_playListDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
        String sql = "DELETE FROM songs WHERE Id = ?";
        //delete artist if he only has one song that was deleted
        if (artistsDAO.artistOccurrences(songArtistId(id)) > 1) {
        } else artistsDAO.deleteArtist(songArtistId(id));
        //delete category if it only belongs to one song that was deleted
        if (categoriesDAO.categoryOccurrences(id) > 1) {
        } else categoriesDAO.deleteCategory(id);
        //delete song from all playlists
        song_playListDAO.deleteFromAllPlayLists(id);
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateSong(String title, int id, String artist, String category, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
        //update artist table
        artistsDAO.updateArtist(songArtistId(id), artist);
        //update category table
        categoriesDAO.updateCategory(songCategoryId(id), category);
        //update song table
        String sql = "UPDATE songs SET Title = ?,Artist = ?, Category= ? WHERE Id=? ";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, artist);
            statement.setString(3, category);
            statement.setInt(4, id);
            statement.executeUpdate();
        }
    }

    @Override
    public int getSongTime(Path filePath) {
        //to be implemented
        return 0;

    }

    //Given a song id this method returns the id of the artist associated to the song.
    private int songArtistId(int id) throws SQLException {
        int artistId = 0;
        String sql = "SELECT FROM songs WHERE Id = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                artistId = resultSet.getInt("Artist");
            }
        }
        return artistId;
    }

    //Given a song id this method returns the id of the category associated to the song.
    private int songCategoryId(int id) throws SQLException {
        int songCategoryId = 0;
        String sql = "SELECT FROM songs WHERE Id = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                songCategoryId = resultSet.getInt("Category");
            }
        }
        return songCategoryId;
    }


}
