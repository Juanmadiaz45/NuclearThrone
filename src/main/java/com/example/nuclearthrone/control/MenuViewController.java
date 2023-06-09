package com.example.nuclearthrone.control;

import com.example.nuclearthrone.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuViewController implements Initializable {

    @FXML
    private Label MenuTitle;
    @FXML
    private Label ChooseLevelText;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    protected void onReturnButton(){
        //Volver a la pantalla de bienvenida
        HelloApplication.hideWindow((Stage) MenuTitle.getScene().getWindow());
        HelloApplication.showWindow("WelcomeView");
    }


    @FXML
    public void onFacilButton(ActionEvent actionEvent) {
        //comenzar el juego en modo facil
        HelloApplication.hideWindow((Stage) MenuTitle.getScene().getWindow());
        HelloApplication.openGame(1);
    }

    @FXML
    public void onMedioButton() {
        //comenzar el juego en modo medio
        HelloApplication.hideWindow((Stage) MenuTitle.getScene().getWindow());
        HelloApplication.openGame(2);

    }

    public void onDificilButton() {
        //comenzar el juego en modo dificil
        HelloApplication.hideWindow((Stage) MenuTitle.getScene().getWindow());
        HelloApplication.openGame(3);

    }

}