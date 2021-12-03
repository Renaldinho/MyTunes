package bll;

import dal.dao.*;

import java.sql.SQLException;

public class MyTunesManager {

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

    public void createPlaylist(String playlistName) throws Exception{
        playListDAO.createPlayList(playlistName);
    }

    public void createSong(String songName, String songArtist, String category, String songFile) throws SQLException {
        songDAO.createSong(songName,songArtist,category,songFile,artistsDAO,categoriesDAO);
    }
}
