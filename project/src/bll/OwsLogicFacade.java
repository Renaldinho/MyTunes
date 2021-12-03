package bll;

import be.Artist;
import be.Category;
import be.PlayList;
import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;
import dal.dao.SongDAO;
import dal.dao.Song_PlayListDAO;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

public interface OwsLogicFacade {

    PlayList createPlayList(String name) throws SQLException;

    void deletePlayList(PlayList playList) throws SQLException;

    List<PlayList> getAllPlayLists() throws SQLException;

    void addSongToPlayList(Song song, PlayList playList) throws SQLException;

    void removeSongFromPlayList(Song song, PlayList playList, int rank) throws SQLException;

    List<Song> getAllSongsForGivenPlayList(PlayList playList, SongDAO songDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDA) throws SQLException;

    void moveSongUp(PlayList playList, int songRank) throws SQLException;

    void moveSongDown(PlayList playList, int songRank) throws SQLException;

    List<Song> getAllSongs(ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLServerException, SQLException;

    Song createSong(String title, String artist, String category, String filePath, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO,String time) throws SQLException;

    void deleteSong(Song song, Song_PlayListDAO song_playListDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException;

    void updateSong(String title, Song song, String newArtist, String newCategory, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException;

}
