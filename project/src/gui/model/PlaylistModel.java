package gui.model;
import bll.MyTunesManager;

public class PlaylistModel {

    private MyTunesManager manager;

    public PlaylistModel() {
        manager = new MyTunesManager();
    }

    public void createPlaylist(String name) throws Exception {
        manager.createPlayList(name);
    }
}
