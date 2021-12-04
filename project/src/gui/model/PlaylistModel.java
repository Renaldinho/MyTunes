package gui.model;
import be.PlayList;
import bll.MyTunesManager;

public class PlaylistModel {

    private MyTunesManager manager;
    MainModel mainModel;
    PlayList playList;
    public PlaylistModel() {
        manager = new MyTunesManager();
         mainModel = new MainModel();
    }

    public void createPlaylist(String name) throws Exception {
        playList = manager.createPlayList(name);
        mainModel.getAllSongs().removeAll();
        mainModel.getAllSongs().addAll(manager.getAllPlayLists());
    }

}
