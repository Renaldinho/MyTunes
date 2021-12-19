package be;
import dal.dao.SongDAO;

import java.sql.SQLException;

public class Joins {
    int songId;
    int playListId;
    SongDAO songDAO = new SongDAO();

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

    public Joins(int songId, int playListId, int rank) {
        this.rank = rank;
        this.playListId = playListId;
        this.songId = songId;
    }

    public String toString() {
        String toString = null;
        try {
            toString = songDAO.getSongById(songId).toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toString;
    }
}
