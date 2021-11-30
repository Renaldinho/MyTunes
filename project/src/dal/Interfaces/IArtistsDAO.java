package dal.Interfaces;


import be.Artist;

import java.sql.SQLException;

public interface IArtistsDAO {
    //looks for a given artist and returns his/her name if not found creates one and returns the generated key same goes with categories.
    int createArtist(String name) throws SQLException;
    void deleteArtist(String name) throws SQLException;

    Artist getArtistById(int artistId) throws SQLException;
}
