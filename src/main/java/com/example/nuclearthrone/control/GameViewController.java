package com.example.nuclearthrone.control;

import com.example.nuclearthrone.HelloApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import com.example.nuclearthrone.model.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameViewController implements Initializable {
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean isRunning = true;
    private Avatar avatar;
    private Image avatarImg;

    @FXML
    private ProgressBar avatarLife;

    @FXML
    private ProgressBar enemy1Life;

    @FXML
    private ProgressBar enemy2Life;

    @FXML
    private ProgressBar enemy3Life;

    private Avatar enemy1;
    private Image enemy1Img;

    private Avatar enemy2;
    private Image enemy2Img;

    private Avatar enemy3;
    private Image enemy1Im3;

    private List<Avatar> avatars;
    private List<Obstacle> obstacles;

    boolean Wpressed = false;
    boolean Apressed = false;
    boolean Spressed = false;
    boolean Dpressed = false;

    public static final int RELOAD_FACTOR = 10;

    public GameViewController(){
        avatarImg = new Image ("file:"+ HelloApplication.class.getResource("RebelWalk1.png").getPath());

        avatars = new ArrayList<>();
        obstacles = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);

        canvas.setOnKeyPressed(this::onKeyPressed);

        avatar = new Avatar(Game.getInstance().getPlayer(), canvas, avatarImg, Color.YELLOW, new Vector(50, 50), new Vector(2, 2));

        avatars.add(avatar);

        Game.getInstance().setAvatars(avatars);

        avatarLife.setProgress(1);
        enemy1Life.setProgress(1);
        enemy2Life.setProgress(1);
        enemy3Life.setProgress(1);

        setUpWalls();

        enemyAI();

        draw();

    }

    public void draw() {

        new Thread(
                () -> {
                    while (isRunning) {
                        Platform.runLater(() -> {
                            gc.setFill(Color.BLACK);
                            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                            drawObstacles();

                            renderAvatar(avatar, avatarLife);
                            renderAvatar(enemy1, enemy1Life);
                            renderAvatar(enemy2, enemy1Life);
                            renderAvatar(enemy3, enemy1Life);

                            int count = 0;
                            for (Avatar avatar : avatars) {
                                if(!avatar.isAlive) count++;
                            }

                            if(count == 3){
                                isRunning = false;
                                for (Avatar avatar : avatars)
                                    if(avatar.isAlive)
                                        Game.getInstance().winner(avatar);

                                Game.getInstance().update();
                            }

                            doKeyboardActions();

                            if(!isRunning){
                                // Hacer una mejor implementacion para volver a jugar, de momento la deje asi
                                HelloApplication.open("WelcomeView.fxml");
                                Stage stage = (Stage) canvas.getScene().getWindow();
                                stage.close();
                            }

                        });
                        //Sleep
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
        ).start();


    }

    private void doKeyboardActions() {

        int angle = 4;

        if (Wpressed && !detectCollisionForward(avatar))
            avatar.moveForward();

        if (Spressed && !detectCollisionBackward(avatar))
            avatar.moveBackward();

        if (Apressed) avatar.changeAngle(-angle);
        if (Dpressed) avatar.changeAngle(angle);

    }

    private void onKeyPressed(KeyEvent keyEvent) {

        //Tank 1
        if (keyEvent.getCode() == KeyCode.W) Wpressed = true;
        if (keyEvent.getCode() == KeyCode.A) Apressed = true;
        if (keyEvent.getCode() == KeyCode.S) Spressed = true;
        if (keyEvent.getCode() == KeyCode.D) Dpressed = true;
        if (keyEvent.getCode() == KeyCode.R) avatar.reload();

        if (keyEvent.getCode() == KeyCode.SPACE)
            if(avatar.hasBullets()) avatar.shoot();
    }

    private void setUpWalls(){

        Obstacle obstacle1 = new Obstacle(canvas, 150, 150);
        Obstacle obstacle2 = new Obstacle(canvas, 150, obstacle1.bounds.getY() + obstacle1.HEIGHT);
        Obstacle obstacle3 = new Obstacle(canvas, 150, obstacle2.bounds.getY() + obstacle1.HEIGHT);
        Obstacle obstacle4 = new Obstacle(canvas, 190, obstacle1.bounds.getX());
        Obstacle obstacle5 = new Obstacle(canvas, 230, obstacle1.bounds.getX());

        Obstacle obstacle6 = new Obstacle(canvas,150,450);
        Obstacle obstacle7 = new Obstacle(canvas, 150, 410);
        Obstacle obstacle8 = new Obstacle(canvas, 150, 370);
        Obstacle obstacle9 = new Obstacle(canvas, 190, 450);
        Obstacle obstacle10 = new Obstacle(canvas, 230, 450);

        Obstacle obstacle11 = new Obstacle(canvas, 604, 150);
        Obstacle obstacle12 = new Obstacle(canvas, 604, 190);
        Obstacle obstacle13 = new Obstacle(canvas, 604, 230);
        Obstacle obstacle14 = new Obstacle(canvas, 564, 150);
        Obstacle obstacle15 = new Obstacle(canvas, 524, 150);

        Obstacle obstacle16 = new Obstacle(canvas, 604, 450);
        Obstacle obstacle17 = new Obstacle(canvas, 604, 410);
        Obstacle obstacle18 = new Obstacle(canvas, 604, 370);
        Obstacle obstacle19 = new Obstacle(canvas, 564, 450);
        Obstacle obstacle20 = new Obstacle(canvas, 524, 450);

        Obstacle obstacle21 = new Obstacle(canvas, 377, 300);
        Obstacle obstacle22 = new Obstacle(canvas, 377, 260);
        Obstacle obstacle23 = new Obstacle(canvas, 377, 340);
        Obstacle obstacle24 = new Obstacle(canvas, 337, 300);
        Obstacle obstacle25 = new Obstacle(canvas, 417, 300);

        obstacles.add(obstacle1);
        obstacles.add(obstacle2);
        obstacles.add(obstacle3);
        obstacles.add(obstacle4);
        obstacles.add(obstacle5);
        obstacles.add(obstacle6);
        obstacles.add(obstacle7);
        obstacles.add(obstacle8);
        obstacles.add(obstacle9);
        obstacles.add(obstacle10);
        obstacles.add(obstacle11);
        obstacles.add(obstacle12);
        obstacles.add(obstacle13);
        obstacles.add(obstacle14);
        obstacles.add(obstacle15);
        obstacles.add(obstacle16);
        obstacles.add(obstacle17);
        obstacles.add(obstacle18);
        obstacles.add(obstacle19);
        obstacles.add(obstacle20);
        obstacles.add(obstacle21);
        obstacles.add(obstacle22);
        obstacles.add(obstacle23);
        obstacles.add(obstacle24);
        obstacles.add(obstacle25);

    }

    private void drawObstacles(){
        for(int i = 0; i < obstacles.size(); i++){
            obstacles.get(i).draw();
        }
    }

    private void renderAvatar(Avatar avatar, ProgressBar life){
        if(avatar.isAlive){
            avatar.draw();
            avatar.manageBullets(avatars);
            life.setProgress((double) avatar.getHearts() / 5.0);
        }else
            life.setProgress(0);
    }

    public boolean detectCollisionForward(Avatar avatar){

        for(int i = 0; i < obstacles.size(); i++){

            if(obstacles.get(i).bounds.intersects(avatar.pos.x + avatar.direction.x - 25, avatar.pos.y + avatar.direction.y - 25, 50, 50))
                return true;

        }

        return false;

    }

    public boolean detectCollisionBackward(Avatar avatar){

        for(int i = 0; i < obstacles.size(); i++){

            if(obstacles.get(i).bounds.intersects(avatar.pos.x - avatar.direction.x - 25, avatar.pos.y - avatar.direction.y - 25, 50, 50))
                return true;

        }

        return false;

    }

    private void enemyAI(){

        new Thread(()->{

            while (enemy1.isAlive){

                if(!enemy1.hasBullets()) enemy1.reload();

                //(1-3)
                int random = (int)(Math.random()*(3-1+1)+1);

                if(random == 1){
                    for (int i = 0; i < 20; i++){
                        if(!detectCollisionForward(enemy1))
                            enemy1.moveForward();
                        else
                            enemy1.changeAngle(180);
                    }
                }

                if(random == 2){
                    enemy1.changeAngle(-25);
                    enemy1.shoot();
                }

                if(random == 3){
                    enemy1.changeAngle(25);
                    enemy1.shoot();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        }).start();

    }

    public void onReturnButton(ActionEvent actionEvent) {
        HelloApplication.hideWindow((Stage) canvas.getScene().getWindow());
        HelloApplication.showWindow("MenuView");
    }

}
