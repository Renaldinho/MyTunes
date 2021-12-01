package bll;

import dal.Interfaces.IPlayListDAO;
import dal.dao.PlayListsDAO;


public class PlaylistManager {

    private IPlayListDAO playlistDAO;

    public PlaylistManager() {
        playlistDAO = new PlayListsDAO();
    }

    public void createPlaylist(String playlistName) throws Exception{
        playlistDAO.createPlayList(playlistName);
    }
}
