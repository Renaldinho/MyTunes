package gui.controller;

import bll.exceptions.PlayListException;
import bll.exceptions.SongException;
import gui.model.PlaylistModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlaylistController {

    private PlaylistModel model;

    @FXML
    private Button savePlaylistBtn;

    @FXML
    private TextField playlistNameTxt;

    @FXML
    private Button cancelPlaylistBtn;


    public PlaylistController(){
        model = new PlaylistModel();
    }

    @FXML
    public void handleSavePlaylistBtn(javafx.scene.input.MouseEvent actionEvent) throws  PlayListException, SongException {
        String playlistName = playlistNameTxt.getText();
        model.createPlaylist(playlistName);
    }

    @FXML
    public void handleCancelPlaylistBtn(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert window");
        alert.setHeaderText("Do you want to close this window?");


        if(alert.showAndWait().get() == ButtonType.OK ) {
            Stage stage = (Stage) cancelPlaylistBtn.getScene().getWindow();
            stage.close();
        }
    }
}
