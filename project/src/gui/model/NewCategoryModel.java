package gui.model;

import be.Category;
import bll.MyTunesManager;
import gui.controller.NewCategoryController;
import gui.controller.SongController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class NewCategoryModel {
    private ObservableList<Category>allCategories;
    MyTunesManager myTunesManager;
    public NewCategoryModel(){
        myTunesManager = new MyTunesManager();
    }
    public ObservableList getAllCategories() throws SQLException {
    allCategories= FXCollections.observableArrayList();
    allCategories.addAll(myTunesManager.getAllCategories());
    return allCategories;
    }

    public void addCategory(String newCategoryName) throws SQLException {
        myTunesManager.createNewCategory(newCategoryName);
    }

    public void updateCategory(Category categorySelected, String newCategoryName) throws SQLException {
        myTunesManager.updateCategory(categorySelected,newCategoryName);
    }

    public void deleteCategory(Category category) throws SQLException {
        myTunesManager.deleteCategory(category);
    }
}
