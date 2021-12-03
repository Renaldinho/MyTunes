package dal;

import be.PlayList;
import be.Song;
import dal.dao.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestClass {
    public static void main(String[] args) throws SQLException {
        List<Song>allSongsFromGivenPlaylist = new ArrayList<>();
        SongDAO songDAO = new SongDAO();
        ArtistsDAO artistsDAO = new ArtistsDAO();
        CategoriesDAO categoriesDAO = new CategoriesDAO();
        Song_PlayListDAO song_playListDAO = new Song_PlayListDAO();
        PlayListsDAO playListsDAO = new PlayListsDAO();
        //song_playListDAO.addSongToPlayList(songDAO.getSongById(67,artistsDAO,categoriesDAO), playListsDAO.getPlayListById(19),playListsDAO );
       //songDAO.createSong("sisi","loulou","hih","zebi",artistsDAO,categoriesDAO,"02");
        playListsDAO.createPlayList("hihi");
        //song_playListDAO.addSongToPlayList(songDAO.getSongById(43,artistsDAO,categoriesDAO), playListsDAO.getPlayListById(9));
//song_playListDAO.moveSongUp(playListsDAO.getPlayListById(9),3 );
        //playListsDAO.getAllPlayLists();
//allSongsFromGivenPlaylist=song_playListDAO.getAllSongsForGivenPlayList(playListsDAO.getPlayListById(9),songDAO,artistsDAO,categoriesDAO );
//for(int i = 0 ; i<allSongsFromGivenPlaylist.size();i++){
        //System.out.println(playListsDAO.getAllPlayLists().get(0));
        //System.out.println(playListsDAO.getAllPlayLists().get(0));
       // playListsDAO.createPlayList("sidisidi");
       // song_playListDAO.addSongToPlayList(songDAO.getSongById(41,artistsDAO,categoriesDAO), playListsDAO.getPlayListById(1),playListsDAO );
    //}
       // for(PlayList playList: playListsDAO.getAllPlayLists()){
          //  System.out.println(playList);
      //  }
}
}
