package gui.model;

import bll.SongManager;

public class SongModel {

    private SongManager songManager;

    public SongModel() {
        songManager = new SongManager();
    }

    public void createPlaylist(String songName, String songArtist, String category, String songFile) throws Exception {
        songManager.createSong(songName, songArtist, category, songFile);
    }
}

