package bll;

import be.Category;
import be.Joins;
import be.PlayList;
import be.Song;
import bll.exceptions.CategoriesException;
import bll.exceptions.JoinsException;
import bll.exceptions.PlayListException;
import bll.exceptions.SongException;
import dal.dao.*;

import java.sql.SQLException;
import java.util.List;

public class MyTunesManager implements OwsLogicFacade {
    CategoriesException categoriesException;
    PlayListException playListException;
    SongException songException;
    ArtistsDAO artistsDAO;
    CategoriesDAO categoriesDAO;
    PlayListsDAO playListDAO;
    JoinsDAO joinsDAO;
    SongDAO songDAO;

    public MyTunesManager() {
        artistsDAO = new ArtistsDAO();
        categoriesDAO = new CategoriesDAO();
        playListDAO = new PlayListsDAO();
        joinsDAO = new JoinsDAO();
        songDAO = new SongDAO();
        songException = new SongException("Something went wrong in the database", new SQLException());
        playListException = new PlayListException("Something went wrong in the database", new SQLException());
        categoriesException = new CategoriesException("Something went wrong in the database", new SQLException());
    }


    @Override
    public PlayList createPlayList(String name) throws PlayListException {
       try {
           return playListDAO.createPlayList(name);
       }catch (SQLException e){
           throw playListException;
       }
    }

    @Override
    public void deletePlayList(PlayList playList) throws PlayListException {
        try {
            playListDAO.deletePlayList(playList);
        }catch (SQLException e){
            throw playListException;
        }

    }

    @Override
    public List<PlayList> getAllPlayLists() throws PlayListException {
try {
    return playListDAO.getAllPlayLists();

}catch (SQLException e){
    throw playListException;
}

    }

    @Override
    public void removeSongFromPlayList(Joins joins, PlayList playList) throws SQLException {
        joinsDAO.removeJoins(joins, playList);

    }

    public List<Joins> getAllJoinsPlayList(PlayList playList) throws JoinsException, SQLException {
        return joinsDAO.getAllJoinsPlayList(playList);

    }

    @Override
    public void moveSongUp(Joins joins) throws JoinsException, SQLException {
        joinsDAO.moveSongUp(joins);

    }

    @Override
    public void moveSongDown(Joins joins) throws JoinsException, SQLException {
        joinsDAO.moveSongDown(joins);

    }

    @Override
    public List<Song> getAllSongs() throws SongException {
        try {
            return songDAO.getAllSongs();
        }catch (SQLException e){
            throw songException;
        }
    }

    @Override
    public Song createSong(String title, String artist, String category, String filePath, String time) throws SongException {
        try {
            return songDAO.createSong(title, artist, category, filePath, time);
        } catch (SQLException e) {
            throw songException;
        }
    }

    @Override
    public void deleteSong(Song song) throws SongException {
        try {
            songDAO.deleteSong(song);
        } catch (SQLException e) {
            throw songException;
        }

    }

    @Override
    public void updateSong(String title, Song song, String newArtist, String newCategory) throws SongException, SQLException {
        try {
            songDAO.updateSong(title, song, newArtist, newCategory);
        } catch (SQLException e) {
            throw songException;
        }

    }

    @Override
    public Song getSongByID(int songId) throws SongException {
        try {
            return songDAO.getSongById(songId);
        } catch (SQLException e) {
            throw songException;
        }


    }

    @Override
    public List<Category> getAllCategories() throws CategoriesException {
        try {
            return categoriesDAO.getAllCategories();
        } catch (SQLException e) {
            throw categoriesException;
        }


    }

    @Override
    public void updateCategory(Category category, String name) throws CategoriesException {
        try {
            categoriesDAO.updateCategory(category, name);
        } catch (SQLException e) {
            throw categoriesException;
        }
    }

    @Override
    public int createNewCategory(String category) throws CategoriesException {
        try {
            return categoriesDAO.createNewCategory(category);
        } catch (SQLException e) {
            throw categoriesException;
        }

    }

    @Override
    public void deleteCategory(Category category) throws CategoriesException {
        try {
            categoriesDAO.deleteCategory(category);
        } catch (SQLException e) {
            throw categoriesException;
        }

    }

    public Joins createJoins(Song song, PlayList playList) throws JoinsException, SQLException {
        return joinsDAO.createJoin(song, playList);

    }


}
