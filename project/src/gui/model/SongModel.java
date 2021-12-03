package gui.model;

import bll.MyTunesManager;


public class SongModel {

    private MyTunesManager manager;

    public SongModel() {
        manager = new MyTunesManager();
    }

    public void createPlaylist(String songName, String songArtist, String category, String songFile) throws Exception {
        manager.createSong(songName, songArtist, category, songFile);
    }
}

