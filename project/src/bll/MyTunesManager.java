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
        } catch (SQLException e) {
            throw new PlayListException("Playlist already exists",e);
        }
    }

    @Override
    public void deletePlayList(PlayList playList) throws PlayListException {
        try {
            playListDAO.deletePlayList(playList);
        } catch (SQLException e) {
            throw new PlayListException("Unable to delete playlist",e);
        }
    }

    @Override
    public List<PlayList> getAllPlayLists() throws PlayListException {
        try {
            return playListDAO.getAllPlayLists();
        } catch (SQLException e) {
            throw new PlayListException("Couldn't get all playlists",e);
        }
    }

    @Override
    public void addSongToPlayList(Song song, PlayList playList) throws PlayListException {
        try {
            joinsDAO.createJoin(song,playList);
        } catch (SQLException e) {
            throw new PlayListException("Song already exists in playlist",e);
        }
    }

    @Override
    public void removeSongFromPlayList(Joins joins,PlayList playList) throws PlayListException {
        try {
            joinsDAO.removeJoins(joins,playList);
        } catch (SQLException e) {
            throw new PlayListException("Couldn't remove song from playlist",e);
        }
    }

    public List<Joins> getAllJoinsPlayList(PlayList playList) throws JoinsException {
        try {
            return joinsDAO.getAllJoinsPlayList(playList);
        } catch (SQLException e) {
            throw new JoinsException("Couldn't get all songs in playlist",e);
        }
    }

    @Override
    public void moveSongUp(Joins joins) throws JoinsException {
        try {
            joinsDAO.moveSongUp(joins);
        } catch (SQLException e) {
            throw new JoinsException("Couldn't move song up",e);
        }
    }

    @Override
    public void moveSongDown(Joins joins) throws JoinsException {
        try {
            joinsDAO.moveSongDown(joins);
        } catch (SQLException e) {
            throw new JoinsException("Couldn't move song down",e);
        }
    }

    @Override
    public List<Song> getAllSongs() throws SongException {
        try {
            return songDAO.getAllSongs();
        } catch (SQLException e) {
            throw new SongException("Couldn't get all song",e);
        }
    }

    @Override
    public Song createSong(String title, String artist, String category, String filePath, String time) throws SongException {
        try {
            return songDAO.createSong(title,artist,category,filePath,time);
        } catch (SQLException e) {
            throw new SongException("Song with this name already exists",e);
        }
    }

    @Override
    public void deleteSong(Song song) throws SongException {
        try {
            songDAO.deleteSong(song);
        } catch (SQLException e) {
            throw new SongException("Couldn't delete song",e);
        }
    }

    @Override
    public void updateSong(String title, Song song, String newArtist, String newCategory) throws SongException {
        try {
            songDAO.updateSong(title,song,newArtist,newCategory);
        } catch (SQLException e) {
            throw new SongException("Couldn't update song",e);
        }
    }

    @Override
    public Song getSongByID(int songId) throws SongException {
        try {
            return songDAO.getSongById(songId);
        } catch (SQLException e) {
            throw new SongException("Couldn't get song with this id",e);
        }
    }

    public Joins createJoins(Song song,PlayList playList) throws JoinsException {
        try {
            return joinsDAO.createJoin(song,playList);
        } catch (SQLException e) {
            throw new JoinsException("Join already exists",e);
        }
    }

    
}
