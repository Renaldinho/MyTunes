package dal.Interfaces;


import be.Artist;
import bll.exceptions.ArtistException;

import java.sql.SQLException;
import java.util.List;

public interface IArtistsDAO {
    int createArtist(String name) throws SQLException, ArtistException;

    Artist getArtistById(int artistId) throws SQLException;

}
