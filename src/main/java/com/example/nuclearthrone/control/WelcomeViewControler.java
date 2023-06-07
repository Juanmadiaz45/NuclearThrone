package com.example.nuclearthrone.control;

import com.example.nuclearthrone.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeViewControler implements Initializable {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView imageView;

    @FXML
    private Button PlayButton;

    @FXML
    private ImageView titleImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        rootPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setFitWidth(newValue.doubleValue());
            PlayButton.setLayoutX(newValue.doubleValue() * 0.42);
            titleImageView.setLayoutX(newValue.doubleValue() * 0.32);
            titleImageView.setFitWidth(newValue.doubleValue() * 0.44);
        });

        rootPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setFitHeight(newValue.doubleValue());
            PlayButton.setLayoutY(newValue.doubleValue() * 0.81);
            titleImageView.setLayoutY(newValue.doubleValue() * 0.174);
            titleImageView.setFitHeight(newValue.doubleValue() * 0.135);
        });

        PlayButton.setOnAction(action ->{
            HelloApplication.hideWindow((Stage) PlayButton.getScene().getWindow());
            HelloApplication.showWindow("MenuView");
        });

    }
}
