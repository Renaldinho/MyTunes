package dal.dao;

import be.Song;
import dal.DatabaseConnector;
import dal.Interfaces.ISongDAO;

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
    public Song createSong(String title, String artist, String category, String filePath,String time) throws SQLException {
        Song song=null;
        int id = 0;
        int artistId = artistsDAO.createArtist(artist);
        int categoryId = categoriesDAO.createNewCategory(category);
        if(pathAlreadyUsed(filePath)==true){
        System.out.println("Song exists in the list as the path is already used. Try to delete the old one if you want to add this.");
        }
        else {
        String sql = ("INSERT INTO songs VALUES (?,?,?,?,?)");
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
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
                song = new Song(id, title, artist, category,time, filePath);
            }
        }}
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
    public void updateSong(String title, Song song, String artist, String category) throws SQLException {
        //update artist table
        int idArtist = artistsDAO.createArtist(artist);
        //update category table
        int idCategory = categoriesDAO.createNewCategory( category);
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

    public Song getSongById(int id)throws SQLException{
        Song song= null;
        String sql= "SELECT *  FROM songs WHERE Id=?";
        try (Connection connection = databaseConnector.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if(resultSet.next()){
                String title = resultSet.getString("Title");
                int artistId = resultSet.getInt("Artist");
                int categoryId = resultSet.getInt("Category");
                String time = resultSet.getString("Time");
                String path = resultSet.getString("Path");
                song = new Song(id,title,artistsDAO.getArtistById(artistId).getName(),categoriesDAO.getCategoryById(categoryId).getCategoryName(),time,path);
            }

        }
        return song;
    }

    //controlling update
    private boolean pathAlreadyUsed(String filePath)throws SQLException{
        String sql = " SELECT  * FROM songs WHERE Path = ?  ";
        try (Connection connection = databaseConnector.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,filePath);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            return resultSet.next();
        }
    }


}
