package gui.model;

import be.Song;
import bll.MyTunesManager;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;

import java.sql.SQLException;

public class SongEditModel {
    MyTunesManager manager;
    public  SongEditModel(){
        manager=new MyTunesManager();
    }
    public void updateSong(String title, Song song, String artist, String category, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
    manager.updateSong(title,song,artist,category,artistsDAO,categoriesDAO);
    }
}
