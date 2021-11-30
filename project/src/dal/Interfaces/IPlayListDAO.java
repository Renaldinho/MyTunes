package dal.Interfaces;

import be.PlayList;
import be.Song;

import java.sql.SQLException;
import java.util.List;

public interface IPlayListDAO {
    PlayList createPlayList(String name) throws SQLException;
    void deletePlayList(String name) throws SQLException;
    List<PlayList>getAllPlayLists() throws SQLException;
}
