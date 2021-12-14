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

    void addSongToPlayList(Song song, PlayList playList) throws SQLException, PlayListException;

    void removeSongFromPlayList(Joins joins,PlayList playList) throws SQLException, PlayListException;

    List<Joins> getAllJoinsPlayList(PlayList playList) throws SQLException, JoinsException;

    void moveSongUp(Joins joins) throws SQLException, JoinsException;

    void moveSongDown(Joins joins) throws SQLException, JoinsException;

    List<Song> getAllSongs() throws SQLServerException, SQLException, SongException;

    Song createSong(String title, String artist, String category, String filePath,String time) throws SQLException, SongException;

    void deleteSong(Song song) throws SQLException, SongException;

    void updateSong(String title, Song song, String newArtist, String newCategory) throws SQLException, SongException;

    Song getSongByID(int songId) throws SQLException, SongException;
}
