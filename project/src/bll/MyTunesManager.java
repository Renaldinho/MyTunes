package bll;

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
    Song_PlayListDAO song_playListDAO;
    SongDAO songDAO;

    public MyTunesManager(){
        artistsDAO = new ArtistsDAO();
        categoriesDAO = new CategoriesDAO();
        playListDAO = new PlayListsDAO();
        song_playListDAO = new Song_PlayListDAO();
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
    song_playListDAO.addSongToPlayList(song,playList, playListDAO);
    }

    @Override
    public void removeSongFromPlayList(Song song, PlayList playList, int rank) throws SQLException {
    song_playListDAO.removeSongFromPlayList(song,playList,rank);
    }

    @Override
    public List<Song> getAllSongsForGivenPlayList(PlayList playList, SongDAO songDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDA) throws SQLException {
        return song_playListDAO.getAllSongsForGivenPlayList(playList,songDAO,artistsDAO,categoriesDAO);
    }

    @Override
    public void moveSongUp(PlayList playList, int songRank) throws SQLException {
    song_playListDAO.moveSongUp(playList,songRank);
    }

    @Override
    public void moveSongDown(PlayList playList, int songRank) throws SQLException {
    song_playListDAO.moveSongDown(playList,songRank);
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
    public void deleteSong(Song song, Song_PlayListDAO song_playListDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
    songDAO.deleteSong(song,song_playListDAO,artistsDAO,categoriesDAO);
    }

    @Override
    public void updateSong(String title, Song song, String newArtist, String newCategory, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
    songDAO.updateSong(title,song,newArtist,newCategory,artistsDAO,categoriesDAO);
    }
}
