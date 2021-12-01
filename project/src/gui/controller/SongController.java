package gui.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.SplitMenuButton;

public class SongController {

    public SplitMenuButton category;

    public void rockMusic(ActionEvent actionEvent) {
        category.setText("Rock Music");
    }

    public void rhythmAndBlues(ActionEvent actionEvent) {
        category.setText("Rhythm and blues");
    }

    public void reggae(ActionEvent actionEvent) {
        category.setText("Reggae");
    }

    public void jazz(ActionEvent actionEvent) {
        category.setText("Jazz");
    }
}
