package gui.controller;

import gui.model.SongModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;

public class SongController {

    private SongModel model;

    @FXML
    private TextField songArtistTxt;

    @FXML
    private TextField songNameTxt;

    @FXML
    private TextField songFileTxt;

    @FXML
    private SplitMenuButton category;

    @FXML
    private Button SaveSongBtn;

    @FXML
    private Button CancelSongBtn;

    public SongController(){
        model = new SongModel();
    }

    public void rockMusic(ActionEvent actionEvent) {
        category.setText("Rock Music");
    }

    public void rhythmAndBlues(ActionEvent actionEvent) {
        category.setText("Rhythm and blues");
    }

    public void reggae(ActionEvent actionEvent) {
        category.setText("Reggae");
    }

    public void jazz(ActionEvent actionEvent) {
        category.setText("Jazz");
    }

    @FXML
    public void handleSaveSongBtn(javafx.scene.input.MouseEvent actionEvent) throws Exception {
        String songName = songNameTxt.getText();
        String songArtist = songArtistTxt.getText();
        String songCategory = category.getText();
        String songFile = songFileTxt.getText();
        model.createPlaylist(songName, songArtist, songCategory, songFile);
    }

    @FXML
    public void handleCancelSongBtn(javafx.scene.input.MouseEvent actionEvent) throws Exception {
        Stage stage = (Stage) CancelSongBtn.getScene().getWindow();
        stage.close();
    }
}
