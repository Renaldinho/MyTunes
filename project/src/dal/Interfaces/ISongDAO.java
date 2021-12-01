package dal.Interfaces;

import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;
import dal.dao.Song_PlayListDAO;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

public interface ISongDAO {
    List<Song> getAllSongs(ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLServerException, SQLException;

    Song createSong(String title, String artist, String category, String filePath, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException;

    void deleteSong(int id, Song_PlayListDAO song_playListDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException;

    void updateSong(String title, int id, String newArtist, String newCategory, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException;

    int getSongTime(Path filePath);


}