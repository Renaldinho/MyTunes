package dal.Interfaces;

import be.Song;
import bll.exceptions.ArtistException;
import bll.exceptions.SongException;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.dao.*;

import java.sql.SQLException;
import java.util.List;

public interface ISongDAO {
    List<Song> getAllSongs() throws SQLServerException, SQLException;

    Song createSong(String title, String artist, String category, String filePath,String time) throws SQLException, SongException, ArtistException;

    void deleteSong(Song song) throws SQLException;

    void updateSong(String title, Song song, String newArtist, String newCategory) throws SQLException, ArtistException;


}