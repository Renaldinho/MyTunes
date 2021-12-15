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

import java.sql.Statement;

public class PlaylistController {

    private PlaylistModel model;

    @FXML
    private Button savePlaylistBtn;

    @FXML
    private TextField playlistNameTxt;

    @FXML
    private Button cancelPlaylistBtn;
    MainController mainController;
    Stage stage;
    public PlaylistController(){
        model = new PlaylistModel();
    }

    @FXML
    public void handleSavePlaylistBtn(javafx.scene.input.MouseEvent actionEvent)  {
        String playlistName = playlistNameTxt.getText();

        try {
            model.createPlaylist(playlistName);
            mainController.updatePlayListTableView();
            stage = (Stage) savePlaylistBtn.getScene().getWindow();
            stage.close();
        } catch (PlayListException | SongException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error");
            alert.setHeaderText(" Playlist already exists.");

            ButtonType okButton = new ButtonType("OK");
            alert.getButtonTypes().setAll(okButton);
            alert.showAndWait();
            }

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

    public void setController(MainController mainController) {
        this.mainController=mainController;
    }
}
