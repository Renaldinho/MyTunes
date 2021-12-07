package gui.controller;

import be.Song;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SongEditController implements Initializable {
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

    public void handleEditSong(ActionEvent actionEvent) {
    }

    public void handleCancelSongBtn(ActionEvent actionEvent) {
    }

    public void handleChooseFile(ActionEvent actionEvent) {
    }

    public void fillFields(Song song){
        songTitleField.setText(song.getTitle());
        songArtistField.setText(song.getArtist());
        category.setText(song.getCategory());
        pathField.setText(song.getFilePath());
        songTimeField.setText(String.valueOf(song.getTime()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}