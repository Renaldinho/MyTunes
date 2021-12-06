package gui.controller;

import be.Joins;
import be.PlayList;
import be.Song;
import bll.MyTunesManager;
import dal.dao.*;
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
    private TableView lstPlayLists;
    MyTunesManager manager = new MyTunesManager();
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

    PlayList playList;
    Song song;

    public Joins getJoins() {
        return joins;
    }

    public void setJoins(Joins joins) {
        this.joins = joins;
    }

    Joins joins;

    public Slider progressSlider;
    public SplitMenuButton category;
    public Button logoutButton;
    public AnchorPane anchorPane;
    public TextField keywordTextField;
    public Button searchCleanButton;
    MainModel mainModel;

    MediaPlayer player;

    public Button playBtn;
    public Button stopBtn;

    public Slider volumeSlider;

    ChangeListener<Duration> changeListener;
    @FXML
    private TableView songTable;

    Stage stage;



    public TableColumn<Song, String> titleColumn;
    public TableColumn<Song, String> artistColumn;
    public TableColumn<Song, String> categoryColumn;
    public TableColumn<Song, String> timeColumn;
    public TableColumn  <PlayList, String> nameColumn;
    public TableColumn<PlayList, Integer> songsColumn;
    public TableColumn<PlayList, String> totalTimeColumn;



    SongDAO songDAO = new SongDAO();
    ArtistsDAO artistsDAO = new ArtistsDAO();
    CategoriesDAO categoriesDAO = new CategoriesDAO();
    PlayListsDAO playListsDAO =new PlayListsDAO();
    JoinsDAO joinsDAO = new JoinsDAO();



    public MainController(){

        mainModel=new MainModel();

        changeListener = new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                progressSlider.setValue(((double) newValue.toSeconds())/ ((double) player.getTotalDuration().toSeconds()));
            }
        };
    }

    public void newSong(ActionEvent actionEvent) throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gui/view/newSong.fxml"));
        root= loader.load();
        SongController songController = loader.getController();
        songController.setController(this);
        Stage stage = new Stage();
        stage.setTitle("Create a song");
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void newPlayList(ActionEvent actionEvent) throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gui/view/newPlayList.fxml"));
        root = loader.load();
        PlaylistController playlistController = loader.getController();
        playlistController.setController(this);
        Stage stage = new Stage();
        stage.setTitle("Create a playlist");
        stage.setScene(new Scene(root));
        stage.show();
    }



    public void handlePlayBtn(ActionEvent actionEvent) {
        playBtn.setDisable(true);
        playBtn.setOpacity(0);
        stopBtn.setOpacity(100);
        stopBtn.setDisable(false);

        player.currentTimeProperty().addListener(changeListener);
        player.play();

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
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        songsColumn.setCellValueFactory(new PropertyValueFactory<>("songs"));
        try {
            lstPlayLists.setItems(mainModel.getAllPlayLists());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        songTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Song selectedSong = ((Song) newValue);
            player = new MediaPlayer(new Media(selectedSong.getFilePath()));

            player.volumeProperty().bind(volumeSlider.valueProperty());
        });
    }
    public void updateSongTableView() {
        //PropertyValueFactory corresponds to the new ProductSearchModel fields
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        try {
            songTable.setItems(mainModel.getAllSongs());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*public void updateAllSongsForSelectedPlayList(){
        lstPlayLists.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                PlayList playList = (PlayList) newValue;
                setPlayList(playList);
                try {
                    songsOnPlayList.setItems(mainModel.getAllSongsForGivenPlayList(playList));
                } catch (SQLException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){
                    songsOnPlayList.getItems().clear();
                }
            }
        });
    }*/

    private void getSelectedSong(){
        songTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Song song = (Song) newValue;
                setSong(song);
            }});
    }
    private void getSelectedPlayList(){
        lstPlayLists.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                PlayList playList = (PlayList) newValue;
                setPlayList(playList);
                try {
                    songsOnPlayList.setItems(mainModel.getAllSongsForGivenPlayList(playList));
                } catch (SQLException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){
                }
            }
        });

    }
    private void getSelectedJoin(){
        songsOnPlayList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Joins joins = (Joins) newValue;
                setJoins(joins);
            }
        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateSongTableView();
        updatePlayListTableView();
       // updateAllSongsForSelectedPlayList();
        getSelectedSong();
        getSelectedPlayList();
        getSelectedJoin();



        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Song> filteredData = null;
        try {
            filteredData = new FilteredList<>(mainModel.getAllSongs(), b -> true);
        } catch (SQLException e) {
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
                    }
                    else return false;
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


        if(alert.showAndWait().get() == ButtonType.OK ){
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

    public void deletePlayList(ActionEvent actionEvent) throws SQLException {
        if(playList!=null)
        mainModel.deletePlayList(playList);
    }

    public void deleteSongFromPlayList(ActionEvent actionEvent) throws SQLException {
        if(joins!=null)
        mainModel.deleteSongFromGivenPlayList(joins,playList,playListsDAO,songDAO);
    }

    public void deleteSong(ActionEvent actionEvent) throws SQLException {
        if(song!=null)
        mainModel.deleteSong(song, joinsDAO,artistsDAO,categoriesDAO);

    }

    public void moveSongUp(ActionEvent actionEvent) throws SQLException {
        if(joins!=null)
        mainModel.moveSongUp(joins,playListsDAO);
    }

    public void moveSongDown(ActionEvent actionEvent) throws SQLException {
        if(joins!=null)
        mainModel.moveSongDown(joins,playListsDAO,playList);
    }



    public void moveSongToPlayList(ActionEvent actionEvent) throws SQLException {
        if((playList!=null)&&(song!=null))
        mainModel.addSongToGivenPlayList(song,playList);
    }
}