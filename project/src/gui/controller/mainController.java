package gui.controller;

import gui.model.MainModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    public SplitMenuButton category;

    public Button playBtn;
    public Button stopBtn;

    private MainModel model;

    public MainController(){
        model = new MainModel();
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

    public void rockMusic(ActionEvent actionEvent) {
        category.setText("Rock Music");
    }

    public void rythmAndBlues(ActionEvent actionEvent) {
        category.setText("Rythm and blues");
    }

    public void reggae(ActionEvent actionEvent) {
        category.setText("Reggae");
    }

    public void jazz(ActionEvent actionEvent) {
        category.setText("Jazz");
    }

    public void handlePlayBtn(ActionEvent actionEvent) {
        playBtn.setDisable(true);
        playBtn.setOpacity(0);
        stopBtn.setOpacity(100);
        stopBtn.setDisable(false);

        model.playSong();
    }

    public void handleStopBtn(ActionEvent actionEvent) {
        playBtn.setDisable(false);
        playBtn.setOpacity(100);
        stopBtn.setOpacity(0);
        stopBtn.setDisable(true);


    }
}
