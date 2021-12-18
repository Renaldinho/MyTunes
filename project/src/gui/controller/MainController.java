package gui.controller;

import be.Joins;
import be.PlayList;
import be.Song;
import bll.exceptions.CategoriesException;
import bll.exceptions.PlayListException;
import bll.exceptions.SongException;
import gui.model.MainModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    private ListView songsOnPlayList;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField keywordTextField;
    @FXML
    private Button searchCleanButton, playBtn, stopBtn;
    @FXML
    private Slider volumeSlider, progressSlider;
    @FXML
    private TableView songTable, lstPlayLists;
    @FXML
    private TableColumn<Song, String> titleColumn, artistColumn, categoryColumn, timeColumn;
    @FXML
    private TableColumn<PlayList, String> nameColumn, totalTimeColumn;
    @FXML
    private TableColumn<PlayList, Integer> songsColumn;

    MainModel mainModel;

    private PlayList playList;
    private Song song, selectedSong;
    private Joins joins;

    private Stage stage;


    enum PlayBackType {
        PLAYLIST_PLAYBACK,
        SONGLIST_PLAYBACK
    }

    PlayBackType playbackType;
    int selectedIndexInPlaylist = -1;

    MediaPlayer player;

    ChangeListener<Duration> changeListener;
    Runnable runnable;

    public MainController() {

        mainModel = new MainModel();

        runnable = new Runnable() {
            @Override
            public void run() {
                selectNextSong();
            }
        };

        changeListener = new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                progressSlider.setValue(((double) newValue.toSeconds()) / ((double) player.getTotalDuration().toSeconds()));
            }
        };
    }

    public void newSong(ActionEvent actionEvent) throws IOException, CategoriesException {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gui/view/newSong.fxml"));
        root = loader.load();
        SongController songController = loader.getController();
        songController.setController(this);
        songController.setMenuBar();
        songController.setTime();
        Stage stage = new Stage();
        stage.setTitle("Create a song");
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void newPlayList(ActionEvent actionEvent) throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gui/view/newEditPlayList.fxml"));
        root = loader.load();
        PlaylistController playlistController = loader.getController();
        playlistController.setController(this);
        playlistController.newPlayList();
        Stage stage = new Stage();
        stage.setTitle("New/Edit playList");
        stage.setScene(new Scene(root));
        stage.show();
    }


    public void handlePlayBtn(ActionEvent actionEvent) {

        if (song != selectedSong) {
            if (selectedSong == null)
                return;
            if (song != null)
                player.stop();

            initializePlayer();

            playBtn.setDisable(true);
            playBtn.setOpacity(0);
            stopBtn.setOpacity(100);
            stopBtn.setDisable(false);

            player.currentTimeProperty().addListener(changeListener);
            player.play();

            player.setOnEndOfMedia(runnable);
        }


    }

    public void handleStopBtn(ActionEvent actionEvent) {
        playBtn.setDisable(false);
        playBtn.setOpacity(100);
        stopBtn.setOpacity(0);
        stopBtn.setDisable(true);

        player.pause();
    }

    public void updatePlayListTableView() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        totalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        songsColumn.setCellValueFactory(new PropertyValueFactory<>("song"));
        try {
            lstPlayLists.setItems(mainModel.getAllPlayLists());
        } catch (PlayListException e) {
            e.printStackTrace();
        }

    }

    private void initializePlayer() {
        song = selectedSong;
        player = new MediaPlayer(new Media(song.getFilePath()));
        player.volumeProperty().bind(volumeSlider.valueProperty());
    }

    public void updateSongTableView() {
        //PropertyValueFactory corresponds to the new ProductSearchModel fields
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        try {
            songTable.setItems(mainModel.getAllSongs());
        } catch (SongException e) {
            e.printStackTrace();
        }
        songTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedSong = ((Song) newValue);
            playbackType = PlayBackType.SONGLIST_PLAYBACK;
        });
    }

    public void updateAllSongsForSelectedPlayList() {
        songsOnPlayList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setJoins((Joins) newValue);
            try {
                if (joins != null)
                    selectedSong = mainModel.getSongByID(joins.getSongId());
            } catch (SongException e) {
                e.printStackTrace();
            }
            selectedIndexInPlaylist = songsOnPlayList.getSelectionModel().getSelectedIndex();
            playbackType = PlayBackType.PLAYLIST_PLAYBACK;
        });
    }


    @FXML
    private void getSelectedPlayList(MouseEvent event) {
        setPlayList((PlayList) lstPlayLists.getSelectionModel().selectedItemProperty().get());
        if (playList != null) {
            try {
                songsOnPlayList.setItems(mainModel.getAllSongsForGivenPlayList(playList));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateSongTableView();
        // updateAllSongsForSelectedPlayList();
        updatePlayListTableView();

        updateAllSongsForSelectedPlayList();


        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Song> filteredData = null;
        try {
            filteredData = new FilteredList<>(mainModel.getAllSongs(), b -> true);
        } catch (SongException e) {
            e.printStackTrace();
        }


        // 2. Set the filter Predicate whenever the filter changes.
        FilteredList<Song> finalFilteredData = filteredData;
        keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            finalFilteredData.setPredicate(song -> {
                // If filter text is empty, display all persons.

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (song.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else // Does not match.
                    if (song.getArtist().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    } else return false;
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Song> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        //       Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(songTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        songTable.setItems(sortedData);

    }

    public void moveProgressSlider(MouseEvent mouseEvent) {
        player.currentTimeProperty().removeListener(changeListener);
    }

    public void setProgress(MouseEvent mouseEvent) {
        player.seek(player.getTotalDuration().multiply(progressSlider.getValue()));
        player.currentTimeProperty().addListener(changeListener);
    }

    //Alert window pop up in mainView
    public void logout(ActionEvent actionEvent) {
        //Alert window: create and basic properties
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert window");
        alert.setHeaderText("Do you want to close this window?");


        if (alert.showAndWait().get() == ButtonType.OK) {
            //close current stage
            stage = (Stage) anchorPane.getScene().getWindow();
            stage.close();
        }
    }

    public void cleanFilter(ActionEvent actionEvent) {
        keywordTextField.clear();
        searchCleanButton.setText("Search");
    }

    public void textFieldAction(KeyEvent keyEvent) {
        searchCleanButton.setText("Clean");
    }

    public void deletePlayList(ActionEvent actionEvent) {
        if (playList != null) {
            int index = lstPlayLists.getSelectionModel().getSelectedIndex();
            try {
                mainModel.deletePlayList(playList);
            } catch (PlayListException e) {
                e.printStackTrace();
            }
            if (index > 0) {
                setPlayList((PlayList) lstPlayLists.getItems().get(index - 1));
                try {
                    songsOnPlayList.setItems(mainModel.getAllSongsForGivenPlayList(playList));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else
                songsOnPlayList.getItems().clear();
        }
    }

    public void deleteSongFromPlayList(ActionEvent actionEvent) {
        if (joins != null)
            try {
                mainModel.deleteSongFromGivenPlayList(joins, playList);
                lstPlayLists.setItems(mainModel.getAllPlayLists());
                songsOnPlayList.setItems(mainModel.getAllSongsForGivenPlayList(playList));
            } catch (PlayListException | SQLException e) {
                e.printStackTrace();
            }
    }

    public void deleteSong(ActionEvent actionEvent) {
        if (selectedSong != null)
            try {
                lstPlayLists.setItems(mainModel.getAllPlayLists());
                mainModel.deleteSong(selectedSong);
            } catch (PlayListException | SongException e) {
                e.printStackTrace();
            }

    }

    public void moveSongUp(ActionEvent actionEvent) {
        if (joins != null) {
            try {
                mainModel.moveSongUp(joins);
                songsOnPlayList.setItems(mainModel.getAllSongsForGivenPlayList(playList));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveSongDown(ActionEvent actionEvent) throws SQLException {
        if (joins != null) {
            mainModel.moveSongDown(joins);
            songsOnPlayList.setItems(mainModel.getAllSongsForGivenPlayList(playList));
        }

    }


    public void moveSongToPlayList(ActionEvent actionEvent) {
        if ((playList != null) && (selectedSong != null)) {
            try {
                mainModel.addSongToGivenPlayList(selectedSong, playList);
                songsOnPlayList.setItems(mainModel.getAllSongsForGivenPlayList(playList));
                lstPlayLists.setItems(mainModel.getAllPlayLists());
            } catch (PlayListException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleEditSong(ActionEvent actionEvent) throws IOException, CategoriesException {
        if (selectedSong == null) {
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/editSong.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Edit a song");
            stage.setScene(new Scene(root));
            //Get controller for the song editing window to access methods on it
            SongEditController songEditController = loader.getController();
            songEditController.setMainController(this);
            songEditController.setMenuBar();
            songEditController.fillFields(selectedSong);

            stage.show();
        }
    }

    public void handleEditPlayList(ActionEvent actionEvent) throws IOException {
        if (playList == null) {
        } else {
            Parent root;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/view/newEditPlayList.fxml"));
            root = loader.load();
            PlaylistController playlistController = loader.getController();
            playlistController.setController(this);
            Stage stage = new Stage();
            stage.setTitle("New/Edit playList");
            stage.setScene(new Scene(root));
            stage.show();
            playlistController.fillField(playList);
        }

    }

    public PlayList getPlayList() {
        return playList;
    }

    public void setPlayList(PlayList playList) {
        this.playList = playList;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setJoins(Joins joins) {
        this.joins = joins;
    }

    public void handleNextSong(ActionEvent actionEvent) {
        selectNextSong();
    }


    public void handlePreviousSong(ActionEvent actionEvent) {
        selectPreviousSong();
    }

    private void selectPreviousSong() {
        player.stop();
        switch (playbackType) {
            case SONGLIST_PLAYBACK:
                if (songTable.getSelectionModel().getFocusedIndex() != 0)
                    songTable.getSelectionModel().select(songTable.getSelectionModel().getFocusedIndex() - 1);
                else
                    songTable.getSelectionModel().select(songTable.getItems().size() - 1);
                break;
            case PLAYLIST_PLAYBACK: {
                if (selectedIndexInPlaylist != 0) {
                    songsOnPlayList.getSelectionModel().select(selectedIndexInPlaylist - 1);
                } else {
                    songsOnPlayList.getSelectionModel().select(songsOnPlayList.getItems().size() - 1);
                    selectedIndexInPlaylist = songsOnPlayList.getItems().size() - 1;
                }
                break;
            }

        }
        initializePlayer();
        player.play();
        player.setOnEndOfMedia(runnable);
        player.currentTimeProperty().addListener(changeListener);
    }


    private void selectNextSong() {
        player.stop();
        switch (playbackType) {
            case SONGLIST_PLAYBACK:
                if (songTable.getItems().size() != songTable.getSelectionModel().getFocusedIndex() + 1)
                    songTable.getSelectionModel().select(songTable.getSelectionModel().getFocusedIndex() + 1);
                else
                    songTable.getSelectionModel().select(0);
                break;
            case PLAYLIST_PLAYBACK: {
                if (songsOnPlayList.getItems().size() - 1 != selectedIndexInPlaylist) {
                    songsOnPlayList.getSelectionModel().select(selectedIndexInPlaylist + 1);
                } else {
                    songsOnPlayList.getSelectionModel().select(0);
                    selectedIndexInPlaylist = 0;
                }
                break;
            }

        }
        initializePlayer();
        player.play();
        player.setOnEndOfMedia(runnable);
        player.currentTimeProperty().addListener(changeListener);
    }
}