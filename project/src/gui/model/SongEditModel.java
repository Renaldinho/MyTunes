package gui.model;

import be.Song;
import bll.MyTunesManager;
import bll.exceptions.ArtistException;
import bll.exceptions.SongException;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;

import java.sql.SQLException;

public class SongEditModel {
    MyTunesManager manager;

    public SongEditModel() {
        manager = new MyTunesManager();
    }

    public void updateSong(String title, Song song, String artist, String category) throws SongException, ArtistException {
        manager.updateSong(title, song, artist, category);
    }
}
