package gui.controller;

import be.Song;
import bll.exceptions.ArtistException;
import bll.exceptions.CategoriesException;
import bll.exceptions.SongException;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;
import gui.model.SongEditModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;


public class SongEditController {
    public Button cancelSongBtn;
    @FXML
    private Label filePath;
    @FXML
    private Label timeSong;
    @FXML
    private TextField songTitleField;
    @FXML
    private TextField songArtistField;

    @FXML
    private SplitMenuButton category;



    Stage stage;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    MainController mainController;

    SongEditModel songEditModel;
    ArtistsDAO artistsDAO;
    CategoriesDAO categoriesDAO;
    Song song;

    public SongEditController() {
        songEditModel = new SongEditModel();
        artistsDAO = new ArtistsDAO();
        categoriesDAO = new CategoriesDAO();
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void handleEditSong(ActionEvent actionEvent)  {
        try {
            songEditModel.updateSong(songTitleField.getText(), song, songArtistField.getText(), category.getText());
        } catch (SongException | ArtistException | CategoriesException e) {
            e.printStackTrace();
        }
        mainController.updateSongTableView();
        stage.close();
    }

    public void handleCancelSongBtn(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert window");
        alert.setHeaderText("Do you want to close this window?");


        if (alert.showAndWait().get() == ButtonType.OK) {
            Stage stage = (Stage) cancelSongBtn.getScene().getWindow();
            stage.close();
        }
    }

    public void fillFields(Song song) {
        songTitleField.setText(song.getTitle());
        songArtistField.setText(song.getArtist());
        category.setText(song.getCategory());
        filePath.setText(song.getFilePath());
        timeSong.setText(String.valueOf(song.getTime()));
        setSong(song);
        stage = (Stage) cancelSongBtn.getScene().getWindow();
    }
}