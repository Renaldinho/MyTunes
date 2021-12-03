package gui.model;
import bll.MyTunesManager;

public class PlaylistModel {

    private MyTunesManager manager;

    public PlaylistModel() {
        manager = new MyTunesManager();
    }

    public void createPlaylist(String playlistName) throws Exception {
        manager.createPlaylist(playlistName);
    }
}
