package dal;

import dal.dao.*;

import java.sql.SQLException;

public class TestClass {
    public static void main(String[] args) throws SQLException {

        SongDAO songDAO = new SongDAO();
        ArtistsDAO artistsDAO = new ArtistsDAO();
        CategoriesDAO categoriesDAO = new CategoriesDAO();
        Song_PlayListDAO song_playListDAO = new Song_PlayListDAO();
        //songDAO.createSong("opo","coucou","dsvdsqd","si",artistsDAO,categoriesDAO);
        PlayListsDAO playListsDAO = new PlayListsDAO();
        //playListsDAO.createPlayList("zebi zebi");
        //artistsDAO.deleteArtist(14);
      //songDAO.deleteSong(30,song_playListDAO,artistsDAO,categoriesDAO);
       // song_playListDAO.addSongToPlayList(31,3);
        //song_playListDAO.moveSongDown(3,1);
        //song_playListDAO.moveSongDown(3,1);
        //song_playListDAO.removeSongFromPlayList(32,3,2);
        //System.out.println(song_playListDAO.lastRank(3));
        //songDAO.deleteSong(31,song_playListDAO,artistsDAO,categoriesDAO);
        songDAO.updateSong("kiki",33,"pppppppp","xxx",artistsDAO,categoriesDAO);


    }
}
