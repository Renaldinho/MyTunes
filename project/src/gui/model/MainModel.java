package gui.model;

import be.PlayList;
import be.Song;
import bll.MyTunesManager;
import dal.dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class MainModel {
    private ObservableList<PlayList>allPlayLists;
    private ObservableList<Song>allSongs;
    private ObservableList<Song>allSongsForGivenPlayList;


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



    public void moveSongDown() {
    }

    public void moveSongUp(PlayList playList,int songRank) throws SQLException {
        manager.moveSongUp(playList,songRank);
    }

    public void deleteSong(Song song, Song_PlayListDAO song_playListDAO, ArtistsDAO artistsDAO, CategoriesDAO categoriesDAO) throws SQLException {
        manager.deleteSong(song,song_playListDAO,artistsDAO,categoriesDAO);
        Song search=null;
        for(Song song1: allSongs){
            if(song.getId()==song1.getId())
                search=song;
            break;
        }
        allSongs.remove(search);
    }

    public void deleteSongFromGivenPlayList() {
    }


    public ObservableList getAllPlayLists() throws SQLException {
        allPlayLists= FXCollections.observableArrayList();
        allPlayLists.addAll(manager.getAllPlayLists());
        return allPlayLists;
    }
    public ObservableList getAllSongs() throws SQLException{
        allSongs=FXCollections.observableArrayList();
        allSongs.addAll(manager.getAllSongs(artistsDAO,categoriesDAO));
        return allSongs;
    }

    public void deletePlayList(PlayList playList) throws SQLException {
        PlayList search=null;
        manager.deletePlayList(playList);
        for(PlayList playList0 : allPlayLists){
            if(playList.getId()==playList0.getId());
            search=playList0;
            break;
        }
        allPlayLists.remove(search);
    }

    public ObservableList getAllSongsForGivenPlayList(PlayList playList) throws SQLException {
        allSongsForGivenPlayList=FXCollections.observableArrayList();
        allSongsForGivenPlayList.addAll(manager.getAllSongsForGivenPlayList(playList,songDAO,artistsDAO,categoriesDAO));
        return allSongsForGivenPlayList;
    }

    public void addSongToGivenPlayList(Song song, PlayList playList) throws SQLException {
        manager.addSongToPlayList(song,playList);
    }

    public void moveSongDown(PlayList playList, int rankSongInPlayList) throws SQLException {
        manager.moveSongDown(playList,rankSongInPlayList);
    }
}
