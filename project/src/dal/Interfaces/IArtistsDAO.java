package dal.Interfaces;


import be.Artist;
import bll.exceptions.ArtistException;

import java.sql.SQLException;

public interface IArtistsDAO {
    int createArtist(String name) throws SQLException, ArtistException;

    Artist getArtist(int artistId) throws SQLException;

}






