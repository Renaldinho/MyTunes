package bll;

import be.Joins;
import be.PlayList;
import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.dao.*;

import java.sql.SQLException;
import java.util.List;

public interface OwsLogicFacade {

    PlayList createPlayList(String name) throws SQLException;

    void deletePlayList(PlayList playList) throws SQLException;

    List<PlayList> getAllPlayLists() throws SQLException;

    void addSongToPlayList(Song song, PlayList playList) throws SQLException;

    void removeSongFromPlayList(Joins joins,PlayListsDAO playListsDAO ,PlayList playList,SongDAO songDAO) throws SQLException;

    List<Joins> getAllJoinsPlayList(PlayList playList) throws SQLException;

    void moveSongUp(Joins joins, PlayListsDAO playListsDAO) throws SQLException;

    void moveSongDown(Joins joins, PlayListsDAO playListsDAO) throws SQLException;

    List<Song> getAllSongs(ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLServerException, SQLException;

    Song createSong(String title, String artist, String category, String filePath, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO,String time) throws SQLException;

    void deleteSong(Song song, JoinsDAO joinsDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException;

    void updateSong(String title, Song song, String newArtist, String newCategory, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException;

}
