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
    private Button ReturnButton;
    @FXML
    private Canvas canvas;
    @FXML
    private Label MenuTitle;
    @FXML
    private Label ChooseLevelText;
    @FXML
    private Button playButton;
    private GraphicsContext gc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(this::onKeyPressed);
        canvas.setOnKeyReleased(this::onKeyReleased);

    }

    private boolean left = false;
    private boolean up = false;
    private boolean down = false;
    private boolean right = false;
    public void onKeyReleased(KeyEvent event){
        switch (event.getCode()){
            case LEFT, A: left = false; break;
            case UP, W: up = false; break;
            case RIGHT, D: right = false; break;
            case DOWN, S: down = false; break;
        }
    }
    public void onKeyPressed(KeyEvent event){
        System.out.println(event.getCode());
        switch (event.getCode()){
            case LEFT, A: left = true; break;
            case UP, W: up = true; break;
            case RIGHT, D: right = true; break;
            case DOWN, S: down = true; break;
        }
    }

    @FXML

    protected void onFacil(){



    }

    @FXML

    protected void onMedio(){



    }
    @FXML

    protected void onDificil(){



    }


    @FXML
    protected void onReturn(){

        HelloApplication.hideWindow((Stage) MenuTitle.getScene().getWindow());
        HelloApplication.showWindow("MenuView");

    }


    public void onHelloButtonClick(ActionEvent actionEvent) {
    }
}