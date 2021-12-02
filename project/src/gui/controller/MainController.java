package gui.controller;

import be.Song;
//import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController implements Initializable {
    @FXML
    public SplitMenuButton category;
    @FXML
    public MenuItem rockMusic;
    @FXML
    public MenuItem rythmAndBlues;
    @FXML
    public MenuItem reggae;
    @FXML
    public MenuItem jazz;
    @FXML
    public Button playBtn;
    @FXML
    public Button stopBtn;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Button logoutButton;
    @FXML
    public AnchorPane currentPane;
    @FXML
    public Button fileButton;
    @FXML
    public TextField filePath;
    @FXML
    public TextField keywordTextField;
    @FXML
    public Button searchCleanButton;
    @FXML
    public TableView<Song> songTableView;
    @FXML
    public TableColumn<Song, String> titleTableColumn;
    @FXML
    public TableColumn<Song, String> artistTableColumn;
    @FXML
    public TableColumn<Song, String> categoryTableColumn;
    @FXML
    public TableColumn<Song, Integer> timeTableColumn;

    ObservableList<Song> songObservableList = FXCollections.observableArrayList();
    Stage stage;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DatabaseConnector connectNow = new DatabaseConnector();
        //SQL-query
        String songViewQuery = "SELECT Title, Artist, Category, [Time] FROM songs";
        try (Connection connectToDB = connectNow.getConnection()){
            Statement statement = connectToDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(songViewQuery);

            while (queryOutput.next()){
                String queryTitle = queryOutput.getString("Title");
                String queryArtist = queryOutput.getString("Artist");
                String queryCategory = queryOutput.getString("Category");
                Integer queryTime = queryOutput.getInt("Time");

                //Fill observableList
                songObservableList.add(new Song(queryTitle,queryArtist,queryCategory,queryTime));
            }

            //PropertyValueFactory corresponds to the new ProductSearchModel fields
            titleTableColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
            artistTableColumn.setCellValueFactory(new PropertyValueFactory<>("Artist"));
            categoryTableColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
            timeTableColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));

            songTableView.setItems(songObservableList);


        } catch (SQLException e) {
            Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }


    }

    public void rockMusic(ActionEvent actionEvent) {
    }

    public void rythmAndBlues(ActionEvent actionEvent) {
    }

    public void reggae(ActionEvent actionEvent) {
    }

    public void jazz(ActionEvent actionEvent) {
    }

    public void handlePlayBtn(ActionEvent actionEvent) {
    }

    public void newPlayList(ActionEvent actionEvent) {
    }

    public void newSong(ActionEvent actionEvent) {

    }

    public void handleStopBtn(ActionEvent actionEvent) {
    }

    //Alert window pop up in mainView
    public void logout(ActionEvent actionEvent) {
        //Alert window: create and basic properties
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert window");
        alert.setHeaderText("Do you want to close this window?");


        if(alert.showAndWait().get() == ButtonType.OK ){
            //close current stage
            stage = (Stage) anchorPane.getScene().getWindow();
            stage.close();
        }
    }
    //Alert window pop up in newSong
    public void cancelAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert window");
        alert.setHeaderText("Do you want to close this window?");


        if(alert.showAndWait().get() == ButtonType.OK ) {
            //close current stage
            stage = (Stage) currentPane.getScene().getWindow();
            stage.close();
        }
    }
    //Opens a File Explorer in newSong
    public void fileSearchAction(ActionEvent actionEvent) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        Stage stage = (Stage) currentPane.getScene().getWindow();
        File file = directoryChooser.showDialog(stage);

        if(file != null){
            filePath.setText(file.getAbsolutePath());
            //System.out.println(file.getAbsolutePath());
        }
    }
}
