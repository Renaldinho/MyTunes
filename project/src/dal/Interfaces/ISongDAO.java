package dal.Interfaces;

import be.Song;
import bll.exceptions.ArtistException;
import bll.exceptions.CategoriesException;
import bll.exceptions.SongException;


import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

public interface ISongDAO {
    List<Song> getAllSongs() throws  SQLException;

    Song createSong(String title, String artist, String category, String filePath,String time) throws SQLException, SongException, ArtistException, CategoriesException, URISyntaxException;

    void deleteSong(Song song) throws SQLException;

    void updateSong(String title, Song song, String newArtist, String newCategory) throws SQLException, ArtistException, CategoriesException;

}


