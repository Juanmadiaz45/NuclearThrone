package com.example.nuclearthrone.control;

import com.example.nuclearthrone.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeViewController implements Initializable {

    @FXML
    private Button playButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    @FXML
    protected void onPlayButton(){

        HelloApplication.hideWindow((Stage) playButton.getScene().getWindow());
        HelloApplication.showWindow("MenuView");
    }
}
