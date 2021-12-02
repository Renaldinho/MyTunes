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
            statement.setInt(4, getSongTime(Path.of(filePath)));
            statement.setString(5, filePath);
            int created = statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            if (created != 0) {
                song = new Song(id, title, artist, category, getSongTime(Path.of(filePath)), filePath);
            }
        }}
        return song;
    }

    @Override
    public void deleteSong(Song song, Song_PlayListDAO song_playListDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
        String sql = "DELETE FROM songs WHERE Id = ?";
        int artistsId = songArtistId(song.getId());
        int categoryId = songCategoryId(song.getId());

        song_playListDAO.deleteFromAllPlayLists(song);
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, song.getId());
            preparedStatement.executeUpdate();
        }
        //delete artist if he only has one song that was deleted
        if (artistsDAO.artistOccurrences(artistsDAO.getArtistById(artistsId)) ==0)
            artistsDAO.deleteArtist(artistsDAO.getArtistById(artistsId));
        //delete category if it only belongs to one song that was deleted
        if (categoriesDAO.categoryOccurrences(categoriesDAO.getCategoryById(categoryId)) ==0)
             categoriesDAO.deleteCategory(categoriesDAO.getCategoryById(categoryId));
    }

    @Override
    public void updateSong(String title, Song song, String artist, String category, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
        //old ids are used for deletion from database
        int oldArtistsId = songArtistId(song.getId());
        int oldCategoryId = songCategoryId(song.getId());

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
        //check if the old artist still have one song at least in the list otherwise clear it.
        //same goes for category
        if (artistsDAO.artistOccurrences(artistsDAO.getArtistById(oldArtistsId)) ==0)
            artistsDAO.deleteArtist(artistsDAO.getArtistById(oldArtistsId));
        if (categoriesDAO.categoryOccurrences(categoriesDAO.getCategoryById(oldCategoryId)) ==0)
            categoriesDAO.deleteCategory(categoriesDAO.getCategoryById(oldCategoryId));
    }

    @Override
    public int getSongTime(Path filePath) {
        //to be implemented
        return 0;

    }

    //Given a song id this method returns the id of the artist associated to the song.
    private int songArtistId(int id) throws SQLException {
        int artistId = 0;
        String sql = "SELECT * FROM songs WHERE Id = ?";
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
        String sql = "SELECT * FROM songs WHERE Id = ?";
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
    public Song getSongById(int id,ArtistsDAO artistsDAO,CategoriesDAO categoriesDAO)throws SQLException{
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
                int time = resultSet.getInt("Time");
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
