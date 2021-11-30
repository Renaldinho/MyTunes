package dal.Interfaces;

import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

public interface ISongDAO {
    List<Song> getAllSongs () throws SQLServerException, SQLException;
    Song createSong(String title,String artist, String category,String filePath) throws SQLException;
    void deleteSong(String title) throws SQLException;
    void updateSong(String title,String newTitle,String newArtist,String newCategory) throws SQLException;
    int getSongTime(Path filePath);
}
