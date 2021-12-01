package bll;

import dal.Interfaces.ISongDAO;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;
import dal.dao.SongDAO;


public class SongManager {

    private ISongDAO songDAO;

    public SongManager() {
        songDAO = new SongDAO();
    }

    public void createSong(String songName, String songArtist, String category, String songFile) throws Exception{
        ArtistsDAO artistDAO = new ArtistsDAO();
        CategoriesDAO categoriesDAO = new CategoriesDAO();
        songDAO.createSong(songName, songArtist, category, songFile, artistDAO, categoriesDAO);
    }
}
