package gui.model;

import be.Song;
import bll.MyTunesManager;
import bll.exceptions.ArtistException;
import bll.exceptions.CategoriesException;
import bll.exceptions.SongException;


public class SongEditModel {
    MyTunesManager manager;

    public SongEditModel() {
        manager = new MyTunesManager();
    }

    public void updateSong(String title, Song song, String artist, String category) throws SongException, ArtistException, CategoriesException {
        manager.updateSong(title, song, artist, category);
    }
}
