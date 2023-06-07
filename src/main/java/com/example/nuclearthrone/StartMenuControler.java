package com.example.nuclearthrone;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StartMenuControler implements Initializable {

    @FXML
    private Button PlayButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        PlayButton.setOnAction(action ->{
            HelloApplication.hideWindow((Stage) PlayButton.getScene().getWindow());
            HelloApplication.showWindow("MenuView");
        });

    }
}
