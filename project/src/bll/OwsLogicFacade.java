package bll;

import be.Joins;
import be.PlayList;
import be.Song;
import bll.exceptions.JoinsException;
import bll.exceptions.PlayListException;
import bll.exceptions.SongException;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.dao.*;

import java.sql.SQLException;
import java.util.List;

public interface OwsLogicFacade {

    PlayList createPlayList(String name) throws SQLException, PlayListException;

    void deletePlayList(PlayList playList) throws SQLException, PlayListException;

    List<PlayList> getAllPlayLists() throws SQLException, PlayListException;

    void addSongToPlayList(Song song, PlayList playList) throws SQLException, SongException;

    void removeSongFromPlayList(Joins joins,PlayListsDAO playListsDAO ,PlayList playList,SongDAO songDAO) throws SQLException, SongException;

    List<Joins> getAllJoinsPlayList(PlayList playList) throws SQLException, JoinsException;

    void moveSongUp(Joins joins, PlayListsDAO playListsDAO) throws SQLException, SongException;

    void moveSongDown(Joins joins, PlayListsDAO playListsDAO) throws SQLException, SongException;

    List<Song> getAllSongs(ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLServerException, SQLException, SongException;

    Song createSong(String title, String artist, String category, String filePath, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO,String time) throws SQLException, SongException;

    void deleteSong(Song song, JoinsDAO joinsDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException, SongException;

    void updateSong(String title, Song song, String newArtist, String newCategory, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException, SongException;

}
