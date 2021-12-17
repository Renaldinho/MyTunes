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
    private TextField songTitleField;
    @FXML
    private TextField songArtistField;
    @FXML
    private TextField pathField;
    @FXML
    private SplitMenuButton category;
    @FXML
    private TextField songTimeField;


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

    public void handleChooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"),
                new FileChooser.ExtensionFilter("WAV Files", "*.wav"));
        File selectedFile = fileChooser.showOpenDialog(pathField.getScene().getWindow());
        if (selectedFile != null) {
            pathField.setText(selectedFile.getAbsolutePath());

            MediaPlayer player = new MediaPlayer(new Media(new File(pathField.getText()).toURI().toString()));
            player.setOnReady(new Runnable() {
                @Override
                public void run() {
                    Duration time = player.getTotalDuration();
                    String strDuration = String.format("%d:%02d:%02d", (int) player.getTotalDuration().toSeconds() / 3600, (int) player.getTotalDuration().toSeconds() / 60, (int) player.getTotalDuration().toSeconds() % 60);
                    songTimeField.setText(strDuration);
                    System.out.println();
                }
            });
        }
    }

    public void fillFields(Song song) {
        songTitleField.setText(song.getTitle());
        songArtistField.setText(song.getArtist());
        category.setText(song.getCategory());
        pathField.setText(song.getFilePath());
        songTimeField.setText(String.valueOf(song.getTime()));
        setSong(song);
        stage = (Stage) cancelSongBtn.getScene().getWindow();
    }

    public void rockMusic(ActionEvent actionEvent) {
        category.setText("Rock music");
    }

    public void rythmAndBlues(ActionEvent actionEvent) {
        category.setText("Rythm and blues");
    }

    public void reggae(ActionEvent actionEvent) {
        category.setText("reggae");
    }

    public void jazz(ActionEvent actionEvent) {
        category.setText("jazz");
    }
}