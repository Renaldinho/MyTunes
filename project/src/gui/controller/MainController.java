package gui.controller;

import be.Song;
import dal.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
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
            titleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            artistTableColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
            categoryTableColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            timeTableColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

            songTableView.setItems(songObservableList);

            // Wrap the ObservableList in a FilteredList (initially display all data).
            FilteredList<Song> filteredData = new FilteredList<>(songObservableList, b -> true);


            // 2. Set the filter Predicate whenever the filter changes.
            keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(employee -> {
                    // If filter text is empty, display all persons.

                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare first name and last name of every person with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (employee.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches first name.
                    } else // Does not match.
                        if (employee.getArtist().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches last name.
                        }
                        else return false;
                });
            });

            // 3. Wrap the FilteredList in a SortedList.
            SortedList<Song> sortedData = new SortedList<>(filteredData);

            // 4. Bind the SortedList comparator to the TableView comparator.
            // 	  Otherwise, sorting the TableView would have no effect.
            sortedData.comparatorProperty().bind(songTableView.comparatorProperty());

            // 5. Add sorted (and filtered) data to the table.
            songTableView.setItems(sortedData);


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

    public void cleanFilter(ActionEvent actionEvent) {
        keywordTextField.setText("");
        searchCleanButton.setText("Search");
    }


    public void textFieldAction(KeyEvent inputMethodEvent) {
        searchCleanButton.setText("Clean");
        System.out.println("Vajco");
    }
}
