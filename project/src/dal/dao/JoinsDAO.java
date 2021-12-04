package dal.dao;

import be.Joins;
import be.PlayList;
import be.Song;
import dal.DatabaseConnector;
import dal.Interfaces.IJoins;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JoinsDAO implements IJoins {
    DatabaseConnector databaseConnector;

    public JoinsDAO() {
        databaseConnector = new DatabaseConnector();
    }

    @Override
    public Joins createJoin(Song song, PlayList playList, PlayListsDAO playListsDAO) throws SQLException {
        Joins joins;
        String sql = "INSERT INTO song_playlist VALUES(?,?,?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, song.getId());
            preparedStatement.setInt(2, playList.getId());
            preparedStatement.setInt(3, lastRank(playList) + 1);
            preparedStatement.executeUpdate();
            joins = new Joins(song.getId(),playList.getId(),lastRank(playList) + 1);

            playListsDAO.updatePlayList(playList,playList.getSong()+1,playList.getTime()+song.getTime());

        }
        return joins;
    }

    /**
     * returns the rank of a song we add to a given playList
     * we need the rank for moving songs up and down.
     */
    public int lastRank(PlayList playList) throws SQLException {
        int rank =0;
        String sql = "SELECT Rank FROM song_playlist where [PlayList Id] = ? ORDER BY Rank ASC";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playList.getId());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                rank =resultSet.getInt("Rank");
            }
        }
        return rank;
    }

    @Override
    public void removeJoins(Joins joins,PlayListsDAO playListsDAO,PlayList playList,SongDAO songDAO) throws SQLException {
        String sql = "DELETE FROM song_playlist WHERE [Song Id]=? AND [PlayList Id]=? AND Rank= ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, joins.getSongId());
            preparedStatement.setInt(2, joins.getPlayListId());
            preparedStatement.setInt(3, joins.getRank());
            preparedStatement.executeUpdate();
            playListsDAO.updatePlayList(playList,playList.getSong()-1,playList.getTime());
        }
    }

    public List<Joins> getAllJoinsPlayList(PlayList playList) throws SQLException {
        List<Joins> allSongsFromSamePlayList = new ArrayList<>();
        String sql = "SELECT * FROM song_playlist WHERE [PlayList Id]=? ORDER BY Rank ASC";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playList.getId());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                int songId = resultSet.getInt("Song Id");
                int songRank = resultSet.getInt("Rank");
                Joins joins = new Joins(songId,playList.getId(),songRank);
                allSongsFromSamePlayList.add(joins);
            }
        }
        return allSongsFromSamePlayList;
    }

    @Override
    public void moveSongDown(Joins joins,PlayListsDAO playListsDAO) throws SQLException {
        if(joins.getRank()==1)
            switchFirstLast(playListsDAO.getPlayListById(joins.getPlayListId()));
        else {
            String sql = "UPDATE song_playlist SET Rank = CASE Rank WHEN ? THEN ? WHEN ? THEN ? ELSE Rank END WHERE [playList Id]=?";
            try (Connection connection =databaseConnector.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,joins.getRank());
                preparedStatement.setInt(2,joins.getRank()-1);
                preparedStatement.setInt(3,joins.getRank()-1);
                preparedStatement.setInt(4,joins.getRank());
                preparedStatement.setInt(5,joins.getPlayListId());
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public void moveSongUp(Joins joins,PlayListsDAO playListsDAO) throws SQLException {
        if (joins.getRank() == lastRank(playListsDAO.getPlayListById(joins.getPlayListId()))) {
            switchFirstLast(playListsDAO.getPlayListById(joins.getPlayListId()));
        } else {
            String sql ="UPDATE song_playlist SET Rank = CASE Rank WHEN ? THEN ? WHEN ? THEN ? ELSE Rank END WHERE [playList Id]=?";
            try (Connection connection = databaseConnector.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,joins.getRank());
                preparedStatement.setInt(2,joins.getRank()+1);
                preparedStatement.setInt(3,joins.getRank()+1);
                preparedStatement.setInt(4,joins.getRank());
                preparedStatement.setInt(5,joins.getPlayListId());
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public void deleteFromAllPlayLists(Song song) throws SQLException {
        String sql = "DELETE FROM song_playlist WHERE [Song Id]=?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, song.getId());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * this method is used when the user wants to push the last song down
     * or first song up.
     * It switches between them.
     */
    public void switchFirstLast( PlayList playList) throws SQLException {
        String sql ="UPDATE song_playlist SET Rank = CASE Rank WHEN ? THEN ? WHEN ? THEN ? ELSE Rank END WHERE [playList Id]=?";
        try (Connection connection = databaseConnector.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,1);
            preparedStatement.setInt(2, lastRank(playList));
            preparedStatement.setInt(3, lastRank(playList));
            preparedStatement.setInt(4,1);
            preparedStatement.setInt(5,playList.getId());
            preparedStatement.executeUpdate();
        }
    }

    public int getRankSongInPlayList(int songId, int playListId) throws SQLException {
        int ranking =0;
        //List<Integer> allRankings = new ArrayList<>();
        String sql = "SELECT * FROM song_playlist WHERE [song Id]=? AND [playList Id]=?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, songId);
            preparedStatement.setInt(2, playListId);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                 ranking = resultSet.getInt(3);
               // allRankings.add(ranking);
            }
        }
        return ranking;
    }

}
