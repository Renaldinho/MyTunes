package gui.controller;

import be.Song;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;
import dal.dao.SongDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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


    public Slider progressSlider;
    public SplitMenuButton category;
    public MenuItem rockMusic;
    public MenuItem reggae;
    public MenuItem jazz;
    public MenuItem rythmAndBlues;
    public Button logoutButton;
    public AnchorPane anchorPane;
    public TextField keywordTextField;
    public Button searchCleanButton;

    MediaPlayer player;

    public Button playBtn;
    public Button stopBtn;

    public Slider volumeSlider;

    ChangeListener<Duration> changeListener;

    public TableView songTable;

    Stage stage;



    public TableColumn<Object, Object> titleColumn;
    public TableColumn<Object, Object> artistColumn;
    public TableColumn<Object, Object> categoryColumn;
    public TableColumn<Object, Object> timeColumn;


    ObservableList<Song> songObservableList = FXCollections.observableArrayList();
    SongDAO songDAO = new SongDAO();
    ArtistsDAO artistsDAO = new ArtistsDAO();
    CategoriesDAO categoriesDAO = new CategoriesDAO();



    public MainController(){



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
        songTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Song selectedSong = ((Song) newValue);
            player = new MediaPlayer(new Media(selectedSong.getFilePath()));

            player.volumeProperty().bind(volumeSlider.valueProperty());
        });

        //DatabaseConnector connectNow = new DatabaseConnector();
        try {
            songObservableList.addAll(songDAO.getAllSongs(artistsDAO,categoriesDAO));
        }catch (SQLException e) {
            e.printStackTrace();
        }
        //PropertyValueFactory corresponds to the new ProductSearchModel fields
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        songTable.setItems(songObservableList);

        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Song> filteredData = new FilteredList<>(songObservableList, b -> true);


        // 2. Set the filter Predicate whenever the filter changes.
        keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(song -> {
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
}