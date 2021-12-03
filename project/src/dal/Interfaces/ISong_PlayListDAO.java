package dal.Interfaces;

import be.PlayList;
import be.Song;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;
import dal.dao.SongDAO;

import java.sql.SQLException;
import java.util.List;

public interface ISong_PlayListDAO {
    void addSongToPlayList(Song song, PlayList playList) throws SQLException;

    void removeSongFromPlayList(Song song, PlayList playList, int rank) throws SQLException;

    List<Song> getAllSongsForGivenPlayList(PlayList playList, SongDAO songDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDA) throws SQLException;

    void moveSongUp(PlayList playList, int songRank) throws SQLException;

    void moveSongDown(PlayList playList, int songRank) throws SQLException;

    void deleteFromAllPlayLists(Song song) throws SQLException;


}
