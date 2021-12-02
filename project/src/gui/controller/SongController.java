package gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SongController {
    @FXML
    public Button cancelButton;
    @FXML
    public AnchorPane anchorPane;

    Stage stage;

    public void cancel(ActionEvent actionEvent) {
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
}
