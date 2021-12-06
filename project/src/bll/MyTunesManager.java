package bll;

import be.Joins;
import be.PlayList;
import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.dao.*;

import java.sql.SQLException;
import java.util.List;

public class MyTunesManager implements OwsLogicFacade {

    ArtistsDAO artistsDAO;
    CategoriesDAO categoriesDAO;
    PlayListsDAO playListDAO;
    JoinsDAO joinsDAO;
    SongDAO songDAO;

    public MyTunesManager(){
        artistsDAO = new ArtistsDAO();
        categoriesDAO = new CategoriesDAO();
        playListDAO = new PlayListsDAO();
        joinsDAO = new JoinsDAO();
        songDAO = new SongDAO();
    }


    @Override
    public PlayList createPlayList(String name) throws SQLException {
        return playListDAO.createPlayList(name);
    }

    @Override
    public void deletePlayList(PlayList playList) throws SQLException {
    playListDAO.deletePlayList(playList);
    }

    @Override
    public List<PlayList> getAllPlayLists() throws SQLException {
        return playListDAO.getAllPlayLists();
    }

    @Override
    public void addSongToPlayList(Song song, PlayList playList) throws SQLException {
    joinsDAO.createJoin(song,playList, playListDAO);
    }

    @Override
    public void removeSongFromPlayList(Joins joins,PlayListsDAO playListsDAO,PlayList playList,SongDAO songDAO) throws SQLException {
    joinsDAO.removeJoins(joins,playListsDAO,playList,songDAO);
    }

    public List<Joins> getAllJoinsPlayList(PlayList playList) throws SQLException {
        return joinsDAO.getAllJoinsPlayList(playList);
    }

    @Override
    public void moveSongUp(Joins joins, PlayListsDAO playListsDAO) throws SQLException {
    joinsDAO.moveSongUp(joins,playListsDAO);
    }

    @Override
    public void moveSongDown(Joins joins,PlayListsDAO playListsDAO) throws SQLException {
    joinsDAO.moveSongDown(joins,playListsDAO);
    }

    @Override
    public List<Song> getAllSongs(ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLServerException, SQLException {
        return songDAO.getAllSongs(artistsDAO,categoriesDAO);
    }

    @Override
    public Song createSong(String title, String artist, String category, String filePath, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO, String time) throws SQLException {
        return songDAO.createSong(title,artist,category,filePath,artistsDAO,categoriesDAO,time);
    }

    @Override
    public void deleteSong(Song song, JoinsDAO joinsDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
    songDAO.deleteSong(song, joinsDAO,artistsDAO,categoriesDAO);
    }

    @Override
    public void updateSong(String title, Song song, String newArtist, String newCategory, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
    songDAO.updateSong(title,song,newArtist,newCategory,artistsDAO,categoriesDAO);
    }
    public Joins createJoins(Song song,PlayList playList,PlayListsDAO playListsDAO) throws SQLException {
        return joinsDAO.createJoin(song,playList,playListDAO);
    }

}
