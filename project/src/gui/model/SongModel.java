package gui.model;

import be.Song;
import bll.MyTunesManager;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;
import dal.dao.SongDAO;
import gui.controller.MainController;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;


public class SongModel {

    private final MyTunesManager manager;
    ArtistsDAO artistsDAO;
    CategoriesDAO categoriesDAO;
    MainModel mainModel;
    SongDAO songDAO = new SongDAO();

    public SongModel() {
        manager = new MyTunesManager();
        artistsDAO = new ArtistsDAO();
        categoriesDAO = new CategoriesDAO();
        mainModel=new MainModel();
    }

    public void createSong(String title, String artist, String songCategory, String time, String filePath) throws SQLException {
        Song song = manager.createSong(title,artist,songCategory,filePath,artistsDAO,categoriesDAO,time);
        mainModel.getAllSongs().add(song);

    }

}

