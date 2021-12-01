package gui.model;

import bll.PlaylistManager;

public class PlaylistModel {

    private PlaylistManager playlistManager;

    public PlaylistModel() {
        playlistManager = new PlaylistManager();
    }

    public void createPlaylist(String playlistName) throws Exception {
        playlistManager.createPlaylist(playlistName);
    }
}
