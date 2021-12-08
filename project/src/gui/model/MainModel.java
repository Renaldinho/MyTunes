package gui.model;

import be.Joins;
import be.PlayList;
import be.Song;
import bll.MyTunesManager;
import dal.dao.*;
import gui.controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class MainModel {
    private ObservableList<PlayList>allPlayLists;
    private ObservableList<Song>allSongs;
    private ObservableList<Joins> allSongsForGivenPlayList;


    MyTunesManager manager;
    ArtistsDAO artistsDAO;
    CategoriesDAO categoriesDAO;
    PlayListsDAO playListsDAO;
    SongDAO songDAO;

    public MainModel(){
        manager = new MyTunesManager();
        artistsDAO = new ArtistsDAO();
        categoriesDAO = new CategoriesDAO();
        playListsDAO=new PlayListsDAO();
        songDAO = new SongDAO();
    }



    public void moveSongDown(Joins joins, PlayListsDAO playListsDAO,PlayList playList) throws SQLException {
        manager.moveSongDown(joins,playListsDAO);
    }

    public void moveSongUp(Joins joins, PlayListsDAO playListsDAO) throws SQLException {
        manager.moveSongUp(joins,playListsDAO);
    }

    public void deleteSong(Song song, JoinsDAO joinsDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
        manager.deleteSong(song, joinsDAO,artistsDAO,categoriesDAO,playListsDAO);
        allSongs.remove(song);
    }

    public void deleteSongFromGivenPlayList(Joins joins,PlayList playList,PlayListsDAO playListsDAO,SongDAO songDAO) throws SQLException {
         manager.removeSongFromPlayList(joins,playListsDAO,playList,songDAO);
            allSongsForGivenPlayList.remove(joins);
    }


    public ObservableList getAllPlayLists() throws SQLException {
        allPlayLists= FXCollections.observableArrayList();
        allPlayLists.addAll(manager.getAllPlayLists());
        return allPlayLists;
    }
    public ObservableList getAllSongs() throws SQLException{
        //allSongs.clear();
        allSongs=FXCollections.observableArrayList();
        allSongs.addAll(manager.getAllSongs(artistsDAO,categoriesDAO));
        return allSongs;
    }

    public void deletePlayList(PlayList playList) throws SQLException {
        manager.deletePlayList(playList);
        allPlayLists.remove(playList);
    }

    public ObservableList getAllSongsForGivenPlayList(PlayList playList) throws SQLException {
        allSongsForGivenPlayList=FXCollections.observableArrayList();
        allSongsForGivenPlayList.addAll(manager.getAllJoinsPlayList(playList));
        return allSongsForGivenPlayList;
    }

    public void addSongToGivenPlayList(Song song, PlayList playList) throws SQLException {
        allSongsForGivenPlayList.add(manager.createJoins(song,playList,playListsDAO));

    }
}
