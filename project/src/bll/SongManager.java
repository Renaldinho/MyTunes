package bll;

import be.PlayList;
import dal.Interfaces.ISongDAO;
import dal.dao.SongDAO;

import java.util.List;

public class SongManager {

    private ISongDAO songDAO;

    public SongManager() {
        songDAO = new SongDAO();
    }

    public List<PlayList> createSong() throws Exception{
        return null;
    }
}
