package gui.controller;

import be.Song;
import gui.model.MainModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.sql.SQLException;

import static java.lang.Math.pow;
import static java.lang.Math.toIntExact;

public class SongController {



    MainModel model;

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
        model = new MainModel();
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



    public void handleChooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(pathField.getScene().getWindow());
        if (selectedFile != null) {
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

        //model.createSong(title,artist,songCategory,time,filePath);





        //model.createSong(title,artist,songCategory,time,filePath);



        //MediaPlayer player = new MediaPlayer(new Media(filePath));

        //MediaPlayer player = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
        //player.play();

        //MediaPlayer player = new MediaPlayer(new Media(("file:/"+filePath.replace("\\","/"))));
        //player.play();


        //model.createSong(title,artist,songCategory,filePath);
    }
}
