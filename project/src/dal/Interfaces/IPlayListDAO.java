package dal.Interfaces;

import be.PlayList;
import be.Song;
import bll.exceptions.PlayListException;

import java.sql.SQLException;
import java.util.List;

public interface IPlayListDAO {
    PlayList createPlayList(String name) throws SQLException, PlayListException;

    void deletePlayList(PlayList playList) throws SQLException;

    List<PlayList> getAllPlayLists() throws SQLException;

    PlayList getPlayListById(int id) throws SQLException;
}
