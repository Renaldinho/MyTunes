package be;

import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;
import dal.dao.PlayListsDAO;
import dal.dao.SongDAO;

import java.sql.SQLException;

public class Joins {
    int songId;
    int playListId;
 PlayListsDAO playListsDAO = new PlayListsDAO();
 SongDAO songDAO = new SongDAO();
 ArtistsDAO artistsDAO = new ArtistsDAO();
 CategoriesDAO categoriesDAO = new CategoriesDAO();
    public int getSongId() {
        return songId;
    }


    public int getPlayListId() {
        return playListId;
    }


    public int getRank() {
        return rank;
    }


    int rank;
    public Joins(int songId, int playListId, int rank){
        this.rank=rank;
        this.playListId=playListId;
        this.songId=songId;
    }

    public String toString() {
        String toString= null;
        try {
            toString=   songDAO.getSongById(songId,artistsDAO,categoriesDAO).toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }return toString;
    }
}
