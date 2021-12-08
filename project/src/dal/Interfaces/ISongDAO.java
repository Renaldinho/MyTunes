package dal.Interfaces;

import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.dao.*;

import java.sql.SQLException;
import java.util.List;

public interface ISongDAO {
    List<Song> getAllSongs(ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLServerException, SQLException;

    Song createSong(String title, String artist, String category, String filePath, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO,String time) throws SQLException;

    void deleteSong(Song song, JoinsDAO joinsDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO,PlayListsDAO playListsDAO, SongDAO songDAO) throws SQLException;

    void updateSong(String title, Song song, String newArtist, String newCategory, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException;


}