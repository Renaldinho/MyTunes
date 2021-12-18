package gui.controller;

import be.Category;
import bll.exceptions.ArtistException;
import bll.exceptions.CategoriesException;
import bll.exceptions.SongException;
import gui.model.SongModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


public class SongController {

    MainController mainController;
    public Button cancelSongBtn;
    String filePath;

    SongModel songModel;
    @FXML
    private TextField pathField;

    @FXML
    private TextField songTimeField;

    @FXML
    private SplitMenuButton category = new SplitMenuButton();

    @FXML
    private TextField songTitleField;
    @FXML
    private TextField songArtistField;
    Stage stage;

    public SongController() {
        songModel = new SongModel();
    }


    @FXML
    public void handleCancelSongBtn(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert window");
        alert.setHeaderText("Do you want to close this window?");


        if (alert.showAndWait().get() == ButtonType.OK) {
            Stage stage = (Stage) cancelSongBtn.getScene().getWindow();
            stage.close();
        }
    }


    public void handleChooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"),
                new FileChooser.ExtensionFilter("WAV Files", "*.wav"));

        File selectedFile = fileChooser.showOpenDialog(pathField.getScene().getWindow());
        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            pathField.setText(filePath);

            MediaPlayer player = new MediaPlayer(new Media(new File(pathField.getText()).toURI().toString()));
            player.setOnReady(new Runnable() {
                @Override
                public void run() {
                    Duration time = player.getTotalDuration();
                    String strDuration = String.format("%d:%02d:%02d", (int) player.getTotalDuration().toSeconds() / 3600, (int) player.getTotalDuration().toSeconds() / 60, (int) player.getTotalDuration().toSeconds() % 60);
                    songTimeField.setText(strDuration);
                    System.out.println();
                }
            });
        }
    }

    public void handleSaveNewSong(ActionEvent actionEvent) {
        String title = songTitleField.getText();
        String artist = songArtistField.getText();
        String songCategory = category.getText();
        if (songCategory.equals("Choose one"))
            songCategory = "";

        String time = songTimeField.getText();

        try {
            songModel.createSong(title, artist, songCategory, time, filePath);
            stage = (Stage) cancelSongBtn.getScene().getWindow();
            stage.close();
        } catch (SongException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error");
            alert.setHeaderText(e.getExceptionMessage());
            ButtonType okButton = new ButtonType("OK");
            alert.getButtonTypes().setAll(okButton);
            alert.showAndWait();
        } catch (ArtistException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error");
            alert.setHeaderText(e.getExceptionMessage());
            ButtonType okButton = new ButtonType("OK");
            alert.getButtonTypes().setAll(okButton);
            alert.showAndWait();
        } catch (CategoriesException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error");
            alert.setHeaderText(e.getExceptionMessage());
            ButtonType okButton = new ButtonType("OK");
            alert.getButtonTypes().setAll(okButton);
            alert.showAndWait();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mainController.updateSongTableView();


    }

    public void setController(MainController mainController) {
        this.mainController = mainController;
    }

    public void newCategory(ActionEvent actionEvent) throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gui/view/newCategory.fxml"));

        root = loader.load();
        NewCategoryController newCategoryController = loader.getController();
        newCategoryController.setController(this);
        Stage stage = new Stage();
        stage.setTitle("Manage categories");
        stage.setScene(new Scene(root));
        stage.show();


    }

    public void setMenuBar() throws CategoriesException {
        MenuItem newMenuItem;
        for (Category cat : songModel.getAllCategories()) {
            newMenuItem = new MenuItem(cat.toString());
            category.getItems().addAll(newMenuItem);
            MenuItem finalNewMenuItem = newMenuItem;
            newMenuItem.setOnAction(event -> category.setText(finalNewMenuItem.getText()));
        }

    }

    public void clearMenuBar() {
        category.getItems().clear();
    }

    public void addMenuItem(String categoryName) {
        MenuItem menuItem = new MenuItem(categoryName);
        category.getItems().add(menuItem);
        menuItem.setOnAction(event -> category.setText(menuItem.getText()));
    }

    public void deleteMenuItem(MenuItem menuItem) {
        MenuItem search = null;
        for (MenuItem menuItem1 : category.getItems()) {
            if (menuItem1.getText().equals(menuItem.getText())) {
                search = menuItem1;
                break;
            }
        }
        category.getItems().remove(search);
    }

    public void setTime() {
        songTimeField.setText("00:00:00");
    }
}
