package com.example.nuclearthrone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("WelcomeView.fxml"));
        Scene scene = new Scene((Region) fxmlLoader.load(), 900, 700);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void hideWindow(Stage stage){

        stage.close();

    }

    public static void showWindow(String fxml){

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml + ".fxml"));
        Scene scene;
        try {
            scene = new Scene((Region) fxmlLoader.load(), 900, 700); // Cambiar el tamaño de la escena
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        launch();
    }
}