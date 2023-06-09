package com.example.nuclearthrone;a

import com.example.nuclearthrone.control.GameViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelloApplication extends Application {

    private static Stage primaryStage;
    private static Map<String, Scene> scenes = new HashMap<>(); 

    @Override
    public void start(Stage primaryStage) {
        HelloApplication.primaryStage = primaryStage;
        loadScenes();
        showWelcomeView();
    }

    private void loadScenes() {
        try {
            FXMLLoader welcomeLoader = new FXMLLoader(HelloApplication.class.getResource("WelcomeView.fxml"));
            AnchorPane welcomeRoot = welcomeLoader.load();
            Scene welcomeScene = new Scene(welcomeRoot);
            scenes.put("WelcomeView", welcomeScene);

            FXMLLoader menuLoader = new FXMLLoader(HelloApplication.class.getResource("MenuView.fxml"));
            AnchorPane menuRoot = menuLoader.load();
            Scene menuScene = new Scene(menuRoot);
            scenes.put("MenuView", menuScene);

//            FXMLLoader gameLoader = new FXMLLoader(HelloApplication.class.getResource("GameView.fxml"));
//            StackPane gameRoot = gameLoader.load();
//            Scene gameScene = new Scene(gameRoot);
//            scenes.put("GameView", gameScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void showWelcomeView() {
        primaryStage.setScene(scenes.get("WelcomeView"));
        primaryStage.show();
    }

    public static void showMenuView() {
        primaryStage.setScene(scenes.get("MenuView"));
        primaryStage.show();
    }

    public static void showGameView() {
        primaryStage.setScene(scenes.get("GameView"));
        primaryStage.show();
    }

    public static void hideWindow(Stage stage) {
        stage.hide();
    }

    public static void showWindow(String sceneName) {
        primaryStage.setScene(scenes.get(sceneName));
        primaryStage.show();
    }


    public static void openGame(int level) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("GameView.fxml"));
            StackPane root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            

            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}