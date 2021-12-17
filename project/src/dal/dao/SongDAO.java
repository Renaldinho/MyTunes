package dal.dao;

import be.Song;
import bll.exceptions.ArtistException;
import bll.exceptions.CategoriesException;
import bll.exceptions.SongException;
import dal.DatabaseConnector;
import dal.Interfaces.ISongDAO;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO implements ISongDAO {
    DatabaseConnector databaseConnector;
    CategoriesDAO categoriesDAO;
    ArtistsDAO artistsDAO;
    JoinsDAO joinsDAO = new JoinsDAO();


    public SongDAO() {
        databaseConnector = new DatabaseConnector();
        artistsDAO = new ArtistsDAO();
        categoriesDAO = new CategoriesDAO();
        joinsDAO = new JoinsDAO();
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
                String artist = artistsDAO.getArtistById(artistId).getName();
                int categoryId = resultSet.getInt("Category");
                String category = categoriesDAO.getCategoryById(categoryId).getCategoryName();
                String time = resultSet.getString("Time");
                String title = resultSet.getString("Title");
                int id = resultSet.getInt("Id");
                String filePath = resultSet.getString("Path");
                Song song = new Song(id, title, artist, category, time, filePath);
                allSongs.add(song);
            }
        }
        return allSongs;
    }


    @Override
    public Song createSong(String title, String artist, String category, String filePath, String time) throws SQLException, SongException, ArtistException, CategoriesException {
        Song song = null;
        int id = 0;
        int categoryId = 0;
        int artistId = 0;
        checkString(title);
            artistId = artistsDAO.createArtist(artist);
        try {
            categoryId = categoriesDAO.createNewCategory(category);
        } catch (CategoriesException e) {
            categoryId=e.getId();
            if(e.getId()==0)
                throw e;}
        checkPath0(filePath);
        checkPath(filePath);
        String sql = ("INSERT INTO songs VALUES (?,?,?,?,?)");
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, title);
            statement.setInt(2, artistId);
            statement.setInt(3, categoryId);
            statement.setString(4, time);
            statement.setString(5, filePath);
            int created = statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            if (created != 0) {
                song = new Song(id, title, artist, category, time, filePath);
            }
        }
        return song;
    }

    @Override
    public void deleteSong(Song song) throws SQLException {
        String sql = "DELETE FROM songs WHERE Id = ?";

        joinsDAO.deleteAllJoins(song);
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, song.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateSong(String title, Song song, String artist, String category) throws SQLException, ArtistException,CategoriesException {
        //update artist table
        int idArtist = artistsDAO.createArtist(artist);
        int idCategory = 0;
        //update category table
        idCategory = categoriesDAO.createNewCategory(category);

        //update song table
        String sql = "UPDATE songs SET Title = ?,Artist = ?, Category= ? WHERE Id=? ";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setInt(2, idArtist);
            statement.setInt(3, idCategory);
            statement.setInt(4, song.getId());
            statement.executeUpdate();
        }
    }

    public Song getSongById(int id) throws SQLException {
        Song song = null;
        String sql = "SELECT *  FROM songs WHERE Id=?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                String title = resultSet.getString("Title");
                int artistId = resultSet.getInt("Artist");
                int categoryId = resultSet.getInt("Category");
                String time = resultSet.getString("Time");
                String path = resultSet.getString("Path");
                song = new Song(id, title, artistsDAO.getArtistById(artistId).getName(), categoriesDAO.getCategoryById(categoryId).getCategoryName(), time, path);
            }

        }
        return song;
    }

    private boolean pathAlreadyUsed(String filePath) throws SQLException {
        String sql = " SELECT  * FROM songs WHERE Path = ?  ";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, filePath);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            return resultSet.next();
        }
    }

    static void checkString(String text) throws SongException {
        SQLException exception = new SQLException();
        if (text.isEmpty())
            throw new SongException("Please find a name for your song", exception);
    }

    void checkPath(String filePath) throws SongException, SQLException {
        Exception exception = new Exception();
        if (pathAlreadyUsed(filePath)) {
            throw new SongException("Song already exists", exception);
        }
    }
    void checkPath0(String filePath) throws SongException {
        filePath = filePath.substring(filePath.indexOf("/")+1);
        File file = new File(filePath).getAbsoluteFile();
        if(!(file.isFile()&& filePath.endsWith(".mp3")||filePath.endsWith(".wav")) )
            throw new SongException("Please find a correct path.",new Exception());
        }

    }


