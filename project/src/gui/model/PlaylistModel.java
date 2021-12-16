package gui.model;

import be.PlayList;
import bll.MyTunesManager;
import bll.exceptions.CategoriesException;
import bll.exceptions.PlayListException;
import bll.exceptions.SongException;

import java.sql.SQLException;

public class PlaylistModel {

    private MyTunesManager manager;
    MainModel mainModel;
    PlayList playList;

    public PlaylistModel() {
        manager = new MyTunesManager();
        mainModel = new MainModel();
    }

    public void createPlaylist(String name) throws PlayListException, SongException {
        playList = manager.createPlayList(name);
        mainModel.getAllSongs().removeAll();
        mainModel.getAllSongs().addAll(manager.getAllPlayLists());
    }

}
