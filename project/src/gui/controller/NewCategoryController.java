package gui.controller;

import be.Category;
import bll.MyTunesManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import gui.model.NewCategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class NewCategoryController implements Initializable {
    public ListView categoriesList;
    public TextField newCategoryName;
    public TextField selectedCategoryName;
    public Button cancelPlaylistBtn;
    public TextField selectCategory;
    NewCategoryModel newCategoryModel;
    Category categorySelected;
    MyTunesManager manager;
    SongController songController;
    public NewCategoryController() throws SQLException {
         newCategoryModel= new NewCategoryModel();
         manager = new MyTunesManager();
         songController = new SongController();
    }
    public void setController(SongController songController) {
        this.songController=songController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            categoriesList.setItems(newCategoryModel.getAllCategories());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void addCategory(ActionEvent actionEvent) throws SQLException {
        if(newCategoryName==null){
            return;
        }
        else {
            newCategoryModel.addCategory(newCategoryName.getText());
            categoriesList.setItems(newCategoryModel.getAllCategories());
            songController.addMenuItem(newCategoryName.getText());
        }
    }

    public void editCategory(ActionEvent actionEvent) throws SQLException {
        if (categorySelected==null){
            return;
        }else {
        newCategoryModel.updateCategory(categorySelected,selectedCategoryName.getText());
        categoriesList.setItems(newCategoryModel.getAllCategories());
            songController.clearMenuBar();
            songController.setMenuBar();    }}

    public void closeWindow(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert window");
        alert.setHeaderText("Do you want to close this window?");


        if(alert.showAndWait().get() == ButtonType.OK ) {
            Stage stage = (Stage) cancelPlaylistBtn.getScene().getWindow();
            stage.close();
        }
}


    public void getSelectedCategory(MouseEvent event) {
        categorySelected = (Category) categoriesList.getSelectionModel().selectedItemProperty().get();
       selectedCategoryName.setText(categorySelected.getCategoryName());
       selectCategory.setText(categorySelected.getCategoryName());

    }

    public void deleteCategory(MouseEvent event) throws SQLException {
        try{
            newCategoryModel.deleteCategory(categorySelected);
            songController.deleteMenuItem(new MenuItem(categorySelected.toString()));
            categoriesList.setItems(newCategoryModel.getAllCategories());
            newCategoryModel.getAllCategories().remove(categorySelected);

        }
        catch (SQLServerException s){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert window");
            alert.setHeaderText("One or many songs are associated to this category.\n Please get rid of this dependency before you can be able to delete it.");

            ButtonType okButton = new ButtonType("OK");
            alert.getButtonTypes().setAll(okButton);
            alert.showAndWait();
        }


    }
}
