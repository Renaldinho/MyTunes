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
    public PlayList createPlayList(String name) throws PlayListException {
        try {
            return playListDAO.createPlayList(name);
        } catch (Exception e) {
            throw new PlayListException("Not able to create PlayList",e);
        }
    }

    @Override
    public void deletePlayList(PlayList playList) throws PlayListException {
        try {
            playListDAO.deletePlayList(playList);
        } catch (Exception e) {
            throw new PlayListException("Not able to delete PlayList",e);
        }
    }

    @Override
    public List<PlayList> getAllPlayLists() throws PlayListException {
        try {
            return playListDAO.getAllPlayLists();
        } catch (Exception e) {
            throw new PlayListException("Not able to get all PlayList",e);
        }
    }

    @Override
    public void addSongToPlayList(Song song, PlayList playList) throws SongException {
        try {
            joinsDAO.createJoin(song,playList, playListDAO);
        } catch (Exception e) {
            throw new SongException("Not able to add song to playlist",e);
        }
    }

    @Override
    public void removeSongFromPlayList(Joins joins,PlayListsDAO playListsDAO,PlayList playList,SongDAO songDAO) throws SongException {
        try {
            joinsDAO.removeJoins(joins,playListsDAO,playList,songDAO);
        } catch (Exception e) {
            throw new SongException("Not able to remove song to playlist",e);
        }
    }

    public List<Joins> getAllJoinsPlayList(PlayList playList) throws JoinsException {
        try {
            return joinsDAO.getAllJoinsPlayList(playList);
        } catch (Exception e) {
            throw new JoinsException("Not able to get all joins playlist",e);
        }
    }

    @Override
    public void moveSongUp(Joins joins, PlayListsDAO playListsDAO) throws SongException {
        try {
            joinsDAO.moveSongUp(joins,playListsDAO);
        } catch (Exception e) {
            throw new SongException("Not able to move song up",e);
        }
    }

    @Override
    public void moveSongDown(Joins joins,PlayListsDAO playListsDAO) throws SongException {
        try {
            joinsDAO.moveSongDown(joins,playListsDAO);
        } catch (Exception e) {
            throw new SongException("Not able to move song down",e);
        }
    }

    @Override
    public List<Song> getAllSongs(ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SongException {
        try {
            return songDAO.getAllSongs(artistsDAO,categoriesDAO);
        } catch (Exception e) {
            throw new SongException("Not able to get all song ",e);
        }
    }

    @Override
    public Song createSong(String title, String artist, String category, String filePath, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO, String time) throws SongException {
        try {
            return songDAO.createSong(title,artist,category,filePath,artistsDAO,categoriesDAO,time);
        } catch (Exception e) {
            throw new SongException("Not able to create song",e);
        }
    }

    @Override
    public void deleteSong(Song song, JoinsDAO joinsDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SongException {
        try {
            songDAO.deleteSong(song, joinsDAO,artistsDAO,categoriesDAO);
        } catch (Exception e) {
            throw new SongException("Not able to delete song",e);
        }
    }

    @Override
    public void updateSong(String title, Song song, String newArtist, String newCategory, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SongException {
        try {
            songDAO.updateSong(title,song,newArtist,newCategory,artistsDAO,categoriesDAO);
        } catch (Exception e) {
            throw new SongException("Not able to upgrade song",e);
        }
    }

}
