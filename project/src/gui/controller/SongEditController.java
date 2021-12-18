package gui.controller;

import be.Category;
import be.Song;
import bll.exceptions.ArtistException;
import bll.exceptions.CategoriesException;
import bll.exceptions.SongException;
import dal.dao.ArtistsDAO;
import dal.dao.CategoriesDAO;
import gui.model.SongEditModel;
import gui.model.SongModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import javafx.stage.Stage;

import java.io.IOException;


public class SongEditController {
    public Button cancelSongBtn;
    @FXML
    private Label filePath;
    @FXML
    private Label timeSong;
    @FXML
    private TextField songTitleField;
    @FXML
    private TextField songArtistField;

    @FXML
    private SplitMenuButton category;
    SongModel songModel;


    Stage stage;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    MainController mainController;

    SongEditModel songEditModel;
    ArtistsDAO artistsDAO;
    CategoriesDAO categoriesDAO;
    Song song;

    public SongEditController() {
        songEditModel = new SongEditModel();
        artistsDAO = new ArtistsDAO();
        categoriesDAO = new CategoriesDAO();
        songModel = new SongModel();
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void handleEditSong(ActionEvent actionEvent) {
        try {
            songEditModel.updateSong(songTitleField.getText(), song, songArtistField.getText(), category.getText());
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
            e.printStackTrace();
        }
        mainController.updateSongTableView();
    }

    public void handleCancelSongBtn(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert window");
        alert.setHeaderText("Do you want to close this window?");


        if (alert.showAndWait().get() == ButtonType.OK) {
            Stage stage = (Stage) cancelSongBtn.getScene().getWindow();
            stage.close();
        }
    }

    public void fillFields(Song song) {
        songTitleField.setText(song.getTitle());
        songArtistField.setText(song.getArtist());
        category.setText(song.getCategory());
        filePath.setText(song.getFilePath());
        timeSong.setText(String.valueOf(song.getTime()));
        setSong(song);
        stage = (Stage) cancelSongBtn.getScene().getWindow();
    }

    public void moreCategories(MouseEvent event) throws IOException {
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

    public void setMenuBar() throws CategoriesException {
        MenuItem newMenuItem;
        for (Category cat : songModel.getAllCategories()) {
            newMenuItem = new MenuItem(cat.toString());
            category.getItems().addAll(newMenuItem);
            MenuItem finalNewMenuItem = newMenuItem;
            newMenuItem.setOnAction(event -> category.setText(finalNewMenuItem.getText()));
        }

    }
}