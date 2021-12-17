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

    PlayList createPlayList(String name) throws PlayListException;

    void deletePlayList(PlayList playList) throws  PlayListException;

    List<PlayList> getAllPlayLists() throws PlayListException;

    void removeSongFromPlayList(Joins joins, PlayList playList) throws SQLException;

    List<Joins> getAllJoinsPlayList(PlayList playList) throws SQLException;

    void moveSongUp(Joins joins) throws SQLException;

    void moveSongDown(Joins joins) throws SQLException;

    List<Song> getAllSongs() throws  SongException;

    Song createSong(String title, String artist, String category, String filePath, String time) throws  SongException, ArtistException, CategoriesException;

    void deleteSong(Song song) throws  SongException;

    void updateSong(String title, Song song, String newArtist, String newCategory) throws  SongException, ArtistException, CategoriesException;

    Song getSongByID(int songId) throws  SongException;

    List<Category> getAllCategories() throws CategoriesException;

    void updateCategory(Category category, String name) throws CategoriesException;

    int createNewCategory(String category) throws CategoriesException;

    void deleteCategory(Category category) throws CategoriesException;
}
