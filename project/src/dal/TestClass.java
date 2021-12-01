package dal;

import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;
import dal.dao.SongDAO;

import java.sql.SQLException;

public class TestClass {
    public static void main(String[] args) throws SQLException {

        SongDAO songDAO = new SongDAO();
        ArtistsDAO artistsDAO = new ArtistsDAO();
        CategoriesDAO categoriesDAO = new CategoriesDAO();
        songDAO.createSong("test","test","test","testFilePath",artistsDAO,categoriesDAO);



    }
}
