package gui.controller;

import gui.model.PlaylistModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PlaylistController {

    private PlaylistModel model;

    @FXML
    private Button savePlaylistBtn;

    @FXML
    private TextField playlistNameTxt;

    public PlaylistController(){
        model = new PlaylistModel();
    }

    public void handleSavePlaylistBtn(ActionEvent actionEvent) throws Exception {
        String playlistName = playlistNameTxt.getText();
        model.createPlaylist(playlistName);
    }
}
