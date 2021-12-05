package dal.Interfaces;

import be.PlayList;
import be.Song;

import java.sql.SQLException;
import java.util.List;

public interface IPlayListDAO {
    PlayList createPlayList(String name) throws Exception;

    void deletePlayList(PlayList playList) throws Exception;

    List<PlayList> getAllPlayLists() throws Exception;

    PlayList getPlayListById(int id) throws Exception;
}
