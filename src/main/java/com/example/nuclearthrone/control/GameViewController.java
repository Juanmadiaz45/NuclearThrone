package com.example.nuclearthrone.control;

import com.example.nuclearthrone.HelloApplication;
import com.example.nuclearthrone.model.Avatar;
import com.example.nuclearthrone.model.Game;
import com.example.nuclearthrone.model.Obstacle;
import com.example.nuclearthrone.model.Vector;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
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
    private Image enemy3Img;
    private List<Avatar> avatars;
    private List<Obstacle> obstacles;
    private boolean up = false;
    private boolean left = false;
    private boolean down = false;
    private boolean right = false;

    public static final int RELOAD_FACTOR = 10;

    public GameViewController() {
        avatars = new ArrayList<>();
        obstacles = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(this::onKeyPressed);
        canvas.setOnKeyReleased(this::onKeyReleased);

        Image avatarImg = new Image("file:" + HelloApplication.class.getResource("Rebel1.png").getPath());
        avatar = new Avatar(Game.getInstance().getPlayer(), canvas, avatarImg, Color.YELLOW, new Vector(50, 50), new Vector(2, 2));

        Image enemy1Img = new Image("file:" + HelloApplication.class.getResource("Enemy1.gif").getPath());
        enemy1 = new Avatar("Enemy1", canvas, enemy1Img, Color.BLUE, new Vector(canvas.getWidth() - 50, canvas.getHeight() - 50), new Vector(-2, -2));

        Image enemy2Img = new Image("file:" + HelloApplication.class.getResource("Enemy2.gif").getPath());
        enemy2 = new Avatar("Enemy2", canvas, enemy2Img, Color.RED, new Vector(canvas.getWidth() - 50, canvas.getHeight() - 50), new Vector(-2, -2));

        Image enemy3Img = new Image("file:" + HelloApplication.class.getResource("Enemy3.gif").getPath());
        enemy3 = new Avatar("Enemy3", canvas, enemy3Img, Color.PURPLE, new Vector(50, canvas.getHeight() - 100), new Vector(2, -2));

        avatars.add(avatar);
        avatars.add(enemy1);
        avatars.add(enemy2);
        avatars.add(enemy3);

        Game.getInstance().setAvatars(avatars);

        avatarLife.setProgress(1);
        enemy1Life.setProgress(1);
        enemy2Life.setProgress(1);
        enemy3Life.setProgress(1);

        setUpWalls();

        enemy1AI();

        enemy2AI();

        enemy3AI();

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
                            renderAvatar(enemy2, enemy2Life);
                            renderAvatar(enemy3, enemy3Life);

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
        if (up && !detectCollisionUp(avatar) && avatar.pos.getY() - 3 >= 0) {
            avatar.pos.setY(avatar.pos.getY() - 3);
        }
        if (left && !detectCollisionLeft(avatar) && avatar.pos.getX() - 3 >= 0) {
            avatar.pos.setX(avatar.pos.getX() - 3);
        }
        if (down && !detectCollisionDown(avatar) && avatar.pos.getY() + 3 <= canvas.getHeight()) {
            avatar.pos.setY(avatar.pos.getY() + 3);
        }
        if (right && !detectCollisionRight(avatar) && avatar.pos.getX() + 3 <= canvas.getWidth()) {
            avatar.pos.setX(avatar.pos.getX() + 3);
        }
    }

    public void onKeyReleased(KeyEvent event){
        switch (event.getCode()){
            case LEFT, A: left = false; break;
            case UP, W: up = false; break;
            case RIGHT, D: right = false; break;
            case DOWN, S: down = false; break;
        }
    }

    public void onKeyPressed(KeyEvent event) {
        System.out.println(event.getCode());
        switch (event.getCode()) {
            case LEFT, A:
                if (avatar.pos.getX() - 3 >= 0) {
                    left = true;
                }
                break;
            case UP, W:
                if (avatar.pos.getY() - 3 >= 0) {
                    up = true;
                }
                break;
            case RIGHT, D:
                if (avatar.pos.getX() + 3 <= canvas.getWidth()) {
                    right = true;
                }
                break;
            case DOWN, S:
                if (avatar.pos.getY() + 3 <= canvas.getHeight()) {
                    down = true;
                }
                break;
            case R:
                avatar.reload();
                break;
            case SPACE:
                if (avatar.hasBullets()) avatar.shoot();
                break;
        }
    }

    private void setUpWalls(){

        Obstacle obstacle1 = (new Obstacle(canvas, 150, 150));
        Obstacle obstacle2 = new Obstacle(canvas, 150, obstacle1.bounds.getY() + obstacle1.HEIGHT);
        Obstacle obstacle3 = new Obstacle(canvas, 150, obstacle2.bounds.getY() + obstacle1.HEIGHT);
        Obstacle obstacle4 = new Obstacle(canvas, 190, obstacle1.bounds.getY());
        Obstacle obstacle5 = new Obstacle(canvas, 230, obstacle1.bounds.getY());

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
        obstacles.add(new Obstacle(canvas, 377, 300));
        obstacles.add(new Obstacle(canvas, 377, 260));
        obstacles.add(new Obstacle(canvas, 377, 340));
        obstacles.add(new Obstacle(canvas, 337, 300));
        obstacles.add(new Obstacle(canvas, 417, 300));

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

    public boolean detectCollisionRight(Avatar avatar){


        for(int i = 0; i < obstacles.size(); i++){

            if( obstacles.get(i).bounds.intersects(
                    avatar.pos.x + avatar.direction.x - 25, avatar.pos.y + avatar.direction.y - 25, 50, 50))
                return true;
        }
        return false;
    }

    public boolean detectCollisionLeft(Avatar avatar){
        for(int i = 0; i < obstacles.size(); i++){
            if(obstacles.get(i).bounds.intersects(avatar.pos.x - avatar.direction.x - 25, avatar.pos.y - avatar.direction.y - 25, 50, 50))
                return true;
        }
        return false;
    }

    public boolean detectCollisionUp(Avatar avatar) {
        for (int i = 0; i < obstacles.size(); i++) {
            if (obstacles.get(i).bounds.intersects(avatar.pos.x - 25, avatar.pos.y - avatar.direction.y - 25, 50, 50))
                return true;
        }
        return false;
    }

    public boolean detectCollisionDown(Avatar avatar) {
        for (int i = 0; i < obstacles.size(); i++) {
            if (obstacles.get(i).bounds.intersects(avatar.pos.x - 25, avatar.pos.y + avatar.direction.y - 25, 50, 50))
                return true;
        }
        return false;
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


    private void enemy1AI(){

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

    private void enemy2AI(){

        new Thread(()->{

            while (enemy2.isAlive){

                if(!enemy2.hasBullets()) enemy2.reload();

                //(1-3)
                int random = (int)(Math.random()*(3-1+1)+1);

                if(random == 1){
                    for (int i = 0; i < 20; i++){
                        if(!detectCollisionForward(enemy2))
                            enemy2.moveForward();
                        else
                            enemy2.changeAngle(180);
                    }
                }

                if(random == 2){
                    enemy2.changeAngle(-25);
                    enemy2.shoot();
                }

                if(random == 3){
                    enemy2.changeAngle(25);
                    enemy2.shoot();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        }).start();

    }

    private void enemy3AI(){

        new Thread(()->{

            while (enemy3.isAlive){

                if(!enemy3.hasBullets()) enemy3.reload();

                //(1-3)
                int random = (int)(Math.random()*(3-1+1)+1);

                if(random == 1){
                    for (int i = 0; i < 20; i++){
                        if(!detectCollisionForward(enemy3))
                            enemy3.moveForward();
                        else
                            enemy3.changeAngle(180);
                    }
                }

                if(random == 2){
                    enemy3.changeAngle(-25);
                    enemy3.shoot();
                }

                if(random == 3){
                    enemy3.changeAngle(25);
                    enemy3.shoot();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        }).start();

    }

    public void onReturnButton() {
        HelloApplication.hideWindow((Stage) canvas.getScene().getWindow());
        HelloApplication.showWindow("MenuView");
    }

}