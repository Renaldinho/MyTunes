package bll;

import be.Category;
import be.Joins;
import be.PlayList;
import be.Song;
import bll.exceptions.*;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.dao.*;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

public interface OwsLogicFacade {

    PlayList createPlayList(String name) throws SQLException, PlayListException;

    void deletePlayList(PlayList playList) throws  PlayListException;

    List<PlayList> getAllPlayLists() throws  CategoriesException, PlayListException;

    void removeSongFromPlayList(Joins joins, PlayList playList) throws SQLException, PlayListException;

    List<Joins> getAllJoinsPlayList(PlayList playList) throws SQLException, JoinsException;

    void moveSongUp(Joins joins) throws SQLException, JoinsException;

    void moveSongDown(Joins joins) throws SQLException, JoinsException;

    List<Song> getAllSongs() throws SQLException, SongException;

    Song createSong(String title, String artist, String category, String filePath, String time) throws SQLException, SongException, ArtistException, CategoriesException, URISyntaxException;

    void deleteSong(Song song) throws SQLException, SongException;

    void updateSong(String title, Song song, String newArtist, String newCategory) throws SQLException, SongException, ArtistException;

    Song getSongByID(int songId) throws SQLException, SongException;

    List<Category> getAllCategories() throws CategoriesException, SQLException;

    void updateCategory(Category category, String name) throws CategoriesException, SQLException;

    int createNewCategory(String category) throws CategoriesException, SQLException;

    void deleteCategory(Category category) throws CategoriesException, SQLException;
}
