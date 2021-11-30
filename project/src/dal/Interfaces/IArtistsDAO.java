package dal.Interfaces;


import be.Artist;

import java.sql.SQLException;

public interface IArtistsDAO {
    int createArtist(String name) throws SQLException;
    void deleteArtist(String name) throws SQLException;

    Artist getArtistById(int artistId) throws SQLException;
}
