package dal.Interfaces;


import be.Artist;

import java.sql.SQLException;

public interface IArtistsDAO {
    int createArtist(String name) throws SQLException;

    void deleteArtist(int id) throws SQLException;

    void updateArtist(int id, String name) throws SQLException;

    Artist getArtistById(int artistId) throws SQLException;
}
