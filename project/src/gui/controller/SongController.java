package gui.controller;

import bll.exceptions.SongException;
import gui.model.MainModel;
import gui.model.SongModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;
import java.sql.SQLException;

public class SongController {

    MainController mainController;
    public Button cancelSongBtn;

    MainModel model;
    SongModel songModel;

    @FXML
    private TextField pathField;

    @FXML
    private TextField songTimeField;

    @FXML
    private SplitMenuButton category;
    @FXML
    private MenuItem rythmAndBlues;
    @FXML
    private MenuItem reggae;
    @FXML
    private MenuItem jazz;
    @FXML
    private MenuItem rockMusic;

    @FXML
    private TextField songTitleField;
    @FXML
    private TextField songArtistField;

    public SongController(){
        songModel = new SongModel();
    }

    public void rockMusic(ActionEvent actionEvent) {
        category.setText("Rock music");
    }

    public void rythmAndBlues(ActionEvent actionEvent) {
        category.setText("Rythm and Blues");
    }

    public void reggae(ActionEvent actionEvent) {
        category.setText("Reggae");
    }

    public void jazz(ActionEvent actionEvent) {
        category.setText("Jazz");
    }


    @FXML
    public void handleCancelSongBtn(ActionEvent actionEvent) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert window");
        alert.setHeaderText("Do you want to close this window?");


        if(alert.showAndWait().get() == ButtonType.OK ) {
        Stage stage = (Stage) cancelSongBtn.getScene().getWindow();
        stage.close();
        }
    }


    public void handleChooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        //fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3 Files",".mp3"),
        //        new FileChooser.ExtensionFilter("WAV Files",".wav"));

        File selectedFile = fileChooser.showOpenDialog(pathField.getScene().getWindow());
        if (selectedFile != null ) {
            pathField.setText(selectedFile.getAbsolutePath());
        }
        MediaPlayer player = new MediaPlayer(new Media(new File(pathField.getText()).toURI().toString()));
        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                Duration time = player.getTotalDuration();
                String strDuration = String.format("%d:%02d:%02d",(int)player.getTotalDuration().toSeconds()/3600,(int)player.getTotalDuration().toSeconds()/60,(int)player.getTotalDuration().toSeconds()%60);
                songTimeField.setText(strDuration);
                System.out.println();
            }
        });
    }

    public void handleSaveNewSong(ActionEvent actionEvent) throws SQLException {
        String title = songTitleField.getText();
        String artist = songArtistField.getText();
        String songCategory = category.getText();
        String filePath = new File(pathField.getText()).toURI().toString();
        //String filePath = ("file:/"+pathField.getText().replace("\\","/"));
        String time = songTimeField.getText();

        try {
            songModel.createSong(title,artist,songCategory,time,filePath);
        } catch (SongException e) {
            e.printStackTrace();
        }
        mainController.updateSongTableView();



    }
    public void setController(MainController mainController) {
        this.mainController=mainController;
    }
}
