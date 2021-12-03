package gui.model;

import bll.MyTunesManager;

import java.sql.SQLException;

public class MainModel {

    MyTunesManager manager;

    public MainModel(){
        manager = new MyTunesManager();
    }

    public void createSong(String title, String artist, String songCategory, String time, String filePath) throws SQLException {
        manager.createSong(time,artist,songCategory,filePath );
    }
}
