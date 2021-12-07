package gui.controller;

import be.Joins;
import be.PlayList;
import be.Song;
import bll.MyTunesManager;
import bll.exceptions.JoinsException;
import bll.exceptions.PlayListException;
import bll.exceptions.SongException;
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
    public TableColumn<Song, String> titleColumn;
    @FXML
    public TableColumn<Song, String> artistColumn;
    @FXML
    public TableColumn<Song, String> categoryColumn;
    @FXML
    public TableColumn<Song, Integer> timeColumn;
    @FXML
    public TableColumn<PlayList, String> nameColumn;
    @FXML
    public TableColumn<PlayList, String> songsColumn;
    @FXML
    public TableColumn<PlayList, String> totalTimeColumn;
    @FXML
    private Label currentSongLabel;
    @FXML
    private ListView<Song> songsOnPlayList;
    @FXML
    private TableView<PlayList> lstPlayLists;
    @FXML
    public Slider progressSlider;
    @FXML
    public SplitMenuButton category;
    @FXML
    public Button logoutButton;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public TextField keywordTextField;
    @FXML
    public Button searchCleanButton;
    @FXML
    public Button playBtn;
    @FXML
    public Button stopBtn;
    @FXML
    public Slider volumeSlider;
    @FXML
    private TableView<Song> songTable;

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

    public Joins getJoins() {
        return joins;
    }

    public void setJoins(Joins joins) {
        this.joins = joins;
    }


    Joins joins;
    Stage stage;
    PlayList playList;
    Song song;
    MainModel mainModel;
    MediaPlayer player;
    ChangeListener<Duration> changeListener;

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
        root = FXMLLoader.load(getClass().getResource("/gui/view/newSong.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Create a song");
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void newPlayList(ActionEvent actionEvent) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/gui/view/newPlayList.fxml"));
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        songsColumn.setCellValueFactory(new PropertyValueFactory<>("Song"));
        try {
            lstPlayLists.setItems(mainModel.getAllPlayLists());
        } catch (PlayListException e) {
            e.printStackTrace();
        }
        songTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Song selectedSong = ((Song) newValue);
            player = new MediaPlayer(new Media(selectedSong.getFilePath()));

            player.volumeProperty().bind(volumeSlider.valueProperty());
        });

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
                }// Does not match.
                    return song.getArtist().toLowerCase().contains(lowerCaseFilter); // Filter matches last name.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Song> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        //       Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(songTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        songTable.setItems(sortedData);

        lstPlayLists.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                 PlayList playList = (PlayList) newValue;
                 setPlayList(playList);
                try {
                    songsOnPlayList.setItems(mainModel.getAllSongsForGivenPlayList(playList));
                } catch (NullPointerException e){
                    songsOnPlayList.getItems().clear();
                } catch (JoinsException e) {
                    e.printStackTrace();
                }
            }
        });
        songTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Song song = (Song) newValue;
                setSong(song);
                currentSongLabel.setText(song.getTitle());
            }});



        songsOnPlayList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Joins joins = (Joins) newValue;
                setJoins(joins);
            }
        });




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

    public void deletePlayList(ActionEvent actionEvent) throws PlayListException {
        PlayList playList =(PlayList)lstPlayLists.getSelectionModel().getSelectedItem();
        mainModel.deletePlayList(playList);
        lstPlayLists.refresh();
    }

    public void deleteSongFromPlayList(ActionEvent actionEvent) throws SongException {
        mainModel.deleteSongFromGivenPlayList(joins,playList,playListsDAO,songDAO);
    }

    public void deleteSong(ActionEvent actionEvent) throws SongException {
        Song song = (Song)  songTable.getSelectionModel().getSelectedItem();
        mainModel.deleteSong(song, joinsDAO,artistsDAO,categoriesDAO);


    }

    public void moveSongUp(ActionEvent actionEvent) throws SongException {
        mainModel.moveSongUp(joins,playListsDAO);
    }

    public void moveSongDown(ActionEvent actionEvent) throws SongException {
        mainModel.moveSongDown(joins,playListsDAO);
    }



    public void moveSongToPlayList(ActionEvent actionEvent) throws SongException {

        mainModel.addSongToGivenPlayList(song,playList);
    }

    public void handleEditSong(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/editSong.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Edit a song");
        stage.setScene(new Scene(root));
        //Get controller for the song editing window to access methods on it
        SongEditController songEditController = loader.getController();
        songEditController.fillFields(song);

        stage.show();
    }
}