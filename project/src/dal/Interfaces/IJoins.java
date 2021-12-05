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
    Joins createJoin(Song song, PlayList playList, PlayListsDAO playListsDAO) throws Exception;

    void removeJoins(Joins joins,PlayListsDAO playListsDAO,PlayList playList,SongDAO songDAO) throws Exception;

     List<Joins> getAllJoinsPlayList(PlayList playList) throws Exception ;

    void moveSongUp(Joins joins,PlayListsDAO playListsDAO) throws Exception;

    void moveSongDown(Joins joins,PlayListsDAO playListsDAO) throws Exception;

    void deleteFromAllPlayLists(Song song) throws Exception;


}
