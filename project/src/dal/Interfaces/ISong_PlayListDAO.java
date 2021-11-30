package dal.Interfaces;

import java.sql.SQLException;

public interface ISong_PlayListDAO {
    void addSongToPlayList(int songId,int playListId) throws SQLException;
    void removeSongFromPlayList(int songId,int playListId) throws SQLException;
}
