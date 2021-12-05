package dal.Interfaces;


import be.Artist;

import java.sql.SQLException;
import java.util.List;

public interface IArtistsDAO {
    int createArtist(String name) throws Exception;

    void deleteArtist(Artist artist) throws Exception;

    void updateArtist(Artist artist, String name) throws Exception;

    Artist getArtistById(int artistId) throws Exception;

    List<Artist> getAllArtists() throws Exception;
}
