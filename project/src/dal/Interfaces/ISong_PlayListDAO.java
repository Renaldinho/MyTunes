package dal.Interfaces;

import be.Song;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;

import java.sql.SQLException;
import java.util.List;

public interface ISong_PlayListDAO {
    void addSongToPlayList(int songId, int playListId) throws SQLException;

    void removeSongFromPlayList(int songId, int playListId, int rank) throws SQLException;

    List<Song> getAllSongsForGivenPlayList(int playListID, ArtistsDAO artistsDAO, CategoriesDAO categoriesDA) throws SQLException;

    void moveSongUp(int playListId, int songRank) throws SQLException;

    void moveSongDown(int playListId, int songRank) throws SQLException;

    void deleteFromAllPlayLists(int songId) throws SQLException;


}
