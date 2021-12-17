package gui.model;

import be.Category;
import be.Song;
import bll.MyTunesManager;
import bll.exceptions.ArtistException;
import bll.exceptions.CategoriesException;
import bll.exceptions.SongException;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;
import dal.dao.SongDAO;
import gui.controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;


public class SongModel {
    private ObservableList<Category> allCategories;
    private final MyTunesManager manager;
    ArtistsDAO artistsDAO;
    CategoriesDAO categoriesDAO;
    MainModel mainModel;

    public SongModel() {
        manager = new MyTunesManager();
        artistsDAO = new ArtistsDAO();
        categoriesDAO = new CategoriesDAO();
        mainModel = new MainModel();
    }

    public void createSong(String title, String artist, String songCategory, String time, String filePath) throws SongException, ArtistException, CategoriesException {
        Song song = manager.createSong(title, artist, songCategory, filePath, time);
        mainModel.getAllSongs().add(song);
    }

    public ObservableList<Category> getAllCategories() throws CategoriesException {
        allCategories = FXCollections.observableArrayList();
        allCategories.addAll(manager.getAllCategories());
        return allCategories;
    }
}

