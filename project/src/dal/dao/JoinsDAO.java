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
    PlayListsDAO playListsDAO;

    public JoinsDAO() {
        databaseConnector = new DatabaseConnector();
        playListsDAO = new PlayListsDAO();
    }

    @Override
    public Joins createJoin(Song song, PlayList playList) throws SQLException {
        Joins joins;
        String sql = "INSERT INTO song_playlist VALUES(?,?,?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, song.getId());
            preparedStatement.setInt(2, playList.getId());
            preparedStatement.setInt(3, lastRank(playList) + 1);
            preparedStatement.executeUpdate();
            joins = new Joins(song.getId(), playList.getId(), lastRank(playList) + 1);
            playList.setSongs(playList.getSong() + 1);
            playListsDAO.updatePlayList(playList, playList.getSong(), calculateTime(getAllJoinsPlayList(playList)));

        }
        return joins;
    }

    /**
     * returns the rank of a song we add to a given playList
     * we need the rank for moving songs up and down.
     */
    private int lastRank(PlayList playList) throws SQLException {
        int rank = 0;
        String sql = "SELECT Rank FROM song_playlist where [PlayList Id] = ? ORDER BY Rank ASC";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playList.getId());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                rank = resultSet.getInt("Rank");
            }
        }
        return rank;
    }

    @Override
    public void removeJoins(Joins joins, PlayList playList) throws SQLException {
        String sql = "DELETE FROM song_playlist WHERE [Song Id]=? AND [PlayList Id]=? AND Rank= ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, joins.getSongId());
            preparedStatement.setInt(2, joins.getPlayListId());
            preparedStatement.setInt(3, joins.getRank());
            preparedStatement.executeUpdate();
            fillRankGap(playList, joins);
            playList.setSongs(playList.getSong() - 1);
            playListsDAO.updatePlayList(playList, playList.getSong(), calculateTime(getAllJoinsPlayList(playList)));
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
                Joins joins = new Joins(songId, playList.getId(), songRank);
                allSongsFromSamePlayList.add(joins);
            }
        }
        return allSongsFromSamePlayList;
    }

    @Override
    public void moveSongDown(Joins joins) throws SQLException {
        if (joins.getRank() == 1)
            switchFirstLast(playListsDAO.getPlayList(joins.getPlayListId()));
        else {
            String sql = "UPDATE song_playlist SET Rank = CASE Rank WHEN ? THEN ? WHEN ? THEN ? ELSE Rank END WHERE [playList Id]=?";
            try (Connection connection = databaseConnector.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, joins.getRank());
                preparedStatement.setInt(2, joins.getRank() - 1);
                preparedStatement.setInt(3, joins.getRank() - 1);
                preparedStatement.setInt(4, joins.getRank());
                preparedStatement.setInt(5, joins.getPlayListId());
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public void moveSongUp(Joins joins) throws SQLException {
        if (joins.getRank() == lastRank(playListsDAO.getPlayList(joins.getPlayListId()))) {
            switchFirstLast(playListsDAO.getPlayList(joins.getPlayListId()));
        } else {
            String sql = "UPDATE song_playlist SET Rank = CASE Rank WHEN ? THEN ? WHEN ? THEN ? ELSE Rank END WHERE [playList Id]=?";
            try (Connection connection = databaseConnector.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, joins.getRank());
                preparedStatement.setInt(2, joins.getRank() + 1);
                preparedStatement.setInt(3, joins.getRank() + 1);
                preparedStatement.setInt(4, joins.getRank());
                preparedStatement.setInt(5, joins.getPlayListId());
                preparedStatement.executeUpdate();
            }
        }
    }

    /**
     * this method is used when the user wants to push the last song down
     * or first song up.
     * It switches between them.
     */
    private void switchFirstLast(PlayList playList) throws SQLException {
        String sql = "UPDATE song_playlist SET Rank = CASE Rank WHEN ? THEN ? WHEN ? THEN ? ELSE Rank END WHERE [playList Id]=?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, 1);
            preparedStatement.setInt(2, lastRank(playList));
            preparedStatement.setInt(3, lastRank(playList));
            preparedStatement.setInt(4, 1);
            preparedStatement.setInt(5, playList.getId());
            preparedStatement.executeUpdate();
        }
    }

    private void fillRankGap(PlayList playList, Joins joins) throws SQLException {
        String sql = "UPDATE song_playlist SET RANK = ? WHERE [PlayList Id]=? AND RANK=?";
        String sql0 = "SELECT * FROM song_playlist WHERE [PlayList Id]=? AND RANK>? ";
        if (joins.getRank() == 1) {
        } else {
            try (Connection connection = databaseConnector.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql0);
                preparedStatement.setInt(1, playList.getId());
                preparedStatement.setInt(2, joins.getRank());
                preparedStatement.execute();
                ResultSet resultSet = preparedStatement.getResultSet();
                while (resultSet.next()) {
                    int rank = resultSet.getInt(3);
                    PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
                    preparedStatement1.setInt(1, rank - 1);
                    preparedStatement1.setInt(2, playList.getId());
                    preparedStatement1.setInt(3, rank);
                    preparedStatement1.executeUpdate();
                }
            }
        }

    }

    private String calculateTime(List<Joins> allJoinsPlayList) {
        int totalHours = 0, totalMinutes = 0, totalSeconds = 0;
        SongDAO songDAO = new SongDAO();

        for (Joins join : allJoinsPlayList) {
            try {
                Song song = songDAO.getSongById(join.getSongId());
                totalHours += Integer.parseInt(song.getTime().split(":")[0]);
                totalMinutes += Integer.parseInt(song.getTime().split(":")[1]);
                totalSeconds += Integer.parseInt(song.getTime().split(":")[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        totalMinutes += totalSeconds / 60;
        totalSeconds %= 60;
        totalHours += totalMinutes / 60;
        totalMinutes %= 60;

        return String.format("%d:%02d:%02d", totalHours, totalMinutes, totalSeconds);
    }
    @Override
    public void deleteAllJoins(Song song) throws SQLException {
        String sql0 = "SELECT * FROM song_playlist WHERE [Song Id]= ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql0);
            preparedStatement.setInt(1, song.getId());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                int playListId = resultSet.getInt("PlayList Id");
                int rank = resultSet.getInt("Rank");
                Joins joins = new Joins(song.getId(), playListId, rank);
                removeJoins(joins, playListsDAO.getPlayList(playListId));
            }
        }
    }

}
