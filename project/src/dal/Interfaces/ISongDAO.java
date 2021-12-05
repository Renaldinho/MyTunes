package dal.Interfaces;

import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;
import dal.dao.JoinsDAO;

import java.sql.SQLException;
import java.util.List;

public interface ISongDAO {
    List<Song> getAllSongs(ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws Exception;

    Song createSong(String title, String artist, String category, String filePath, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO,String time) throws Exception;

    void deleteSong(Song song, JoinsDAO joinsDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws Exception;

    void updateSong(String title, Song song, String newArtist, String newCategory, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws Exception;


}