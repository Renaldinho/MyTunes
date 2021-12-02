package dal.Interfaces;


import be.Artist;

import java.sql.SQLException;
import java.util.List;

public interface IArtistsDAO {
    int createArtist(String name) throws SQLException;

    void deleteArtist(Artist artist) throws SQLException;

    void updateArtist(Artist artist, String name) throws SQLException;

    Artist getArtistById(int artistId) throws SQLException;

    List<Artist> getAllArtists() throws SQLException;
}
