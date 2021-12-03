package gui.controller;

import gui.model.PlaylistModel;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PlaylistController {

    private PlaylistModel model;

    @FXML
    private Button savePlaylistBtn;

    @FXML
    private TextField playlistNameTxt;

    @FXML
    private Button CancelPlaylistBtn;

    public PlaylistController(){
        model = new PlaylistModel();
    }

    @FXML
    public void handleSavePlaylistBtn(javafx.scene.input.MouseEvent actionEvent) throws Exception {
        String playlistName = playlistNameTxt.getText();
        model.createPlaylist(playlistName);
    }

    @FXML
    public void handleCancelPlaylistBtn(javafx.scene.input.MouseEvent actionEvent) {
        Stage stage = (Stage) CancelPlaylistBtn.getScene().getWindow();
        stage.close();
    }
}
