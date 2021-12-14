package dal.Interfaces;

import be.Joins;
import be.PlayList;
import be.Song;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;
import dal.dao.PlayListsDAO;
import dal.dao.SongDAO;

import java.sql.SQLException;
import java.util.List;

public interface IJoins {
    Joins createJoin(Song song, PlayList playList) throws SQLException;

    void removeJoins(Joins joins,PlayList playList) throws SQLException;

     List<Joins> getAllJoinsPlayList(PlayList playList) throws SQLException ;

    void moveSongUp(Joins joins) throws SQLException;

    void moveSongDown(Joins joins) throws SQLException;

    void deleteFromAllPlayLists(Song song) throws SQLException;


}
