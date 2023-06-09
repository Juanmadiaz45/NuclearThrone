package com.example.nuclearthrone.control;

import com.example.nuclearthrone.HelloApplication;
import com.example.nuclearthrone.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class GameViewController implements Initializable {
    @FXML
    private Canvas canvas;
    @FXML
    private Button returnButton;
    @FXML
    private Label scoreLbl;
    private GraphicsContext gc;
    private boolean isRunning = true;
    private Avatar avatar;
    @FXML
    private ProgressBar avatarLife;

    @FXML
    private VBox progressBarContainer;

    @FXML
    private ProgressBar avatarBullets;
    private List<ProgressBar> progressBars; // List to store the ProgressBar elements


    private List<Avatar> avatars;
    private List<Obstacle> obstacles;
    private boolean up = false;
    private boolean left = false;
    private boolean down = false;
    private boolean right = false;
    private ArrayList<Obstacle> map1;
    private ArrayList<Obstacle> map2;
    private ArrayList<Obstacle> map3;
    private int score;
    private static int level;
    private int actualMap;
    private int numOfEnemies;
    private Gun gun1;
    private Gun gun2;
    private Gun gun3;
    private List<Gun> gunsInFloor;

    private static final Random random = new Random();



    public static final int RELOAD_FACTOR = 10;

    public GameViewController() {

        avatars = new ArrayList<>();
        score=0;
        actualMap=0;
        level=1;
        progressBars = new ArrayList<>();
        obstacles = new ArrayList<>();
        gunsInFloor = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(this::onKeyPressed);
        canvas.setOnKeyReleased(this::onKeyReleased);

        Image avatarImg = new Image("file:" + HelloApplication.class.getResource("RebelWalk1.png").getPath());
        avatar = new Avatar(Game.getInstance().getPlayer(), canvas, avatarImg, Color.YELLOW, new Vector(50, 50), new Vector(1, 1));
        avatars.add(avatar);
        progressBars.add(avatarLife);

        Image gun1Image = new Image("file:" + HelloApplication.class.getResource("revolver1.png").getPath());
        Image gun2Image = new Image("file:" + HelloApplication.class.getResource("weirdRevolver.png").getPath());
        Image gun3Image = new Image("file:" + HelloApplication.class.getResource("glock.png").getPath());

        gun1 = new Gun(0, 0, gun1Image, canvas, new Vector(canvas.getWidth() - 25, canvas.getHeight() - 25));
        gun2 = new Gun(0, 0, gun2Image, canvas, new Vector(25, 25));
        gun3 = new Gun(0, 0, gun3Image, canvas, new Vector(25, -25));


        gunsInFloor.add(gun1);
        gunsInFloor.add(gun2);
        gunsInFloor.add(gun3);
        Game.getInstance().setGunsOnFloor(gunsInFloor);


        createMap1();
        createMap2();
        createMap3();

        obstacles=map3;

        if(level==1)level(3);
        if(level==2)level(4);
        if(level==3)level(5);

        draw();

        System.out.println(gun1Image.getUrl());
        System.out.println(gun2Image.getUrl());
        System.out.println(gun3Image.getUrl());

        System.out.println("Numero de armas en el suelo: " + gunsInFloor.size());
    }

    private void level(int enemyRange) {
        numOfEnemies = random.nextInt((enemyRange)+1);
        progressBarContainer.getChildren().removeAll();

        for (int i = 1; i <= numOfEnemies; i++) {
            int n=(i-1)%3+1;
            Image enemy1Img = new Image("file:" + HelloApplication.class.getResource("Enemy"+n+".gif").getPath());
            Avatar enemy = new Avatar("Enemy"+i, canvas, enemy1Img, Color.RED, generateRandomPosition(), new Vector(-2, -2));
            avatars.add(enemy); //creates and adds the enemy
            System.out.println("Enemigo"+i+"creado");

            ProgressBar progressBar = new ProgressBar();
            progressBar.setPrefWidth(200.0);
            progressBar.setProgress(1);
            progressBarContainer.getChildren().add(progressBar);
            progressBars.add(i,progressBar); // Add the ProgressBar to the list
            enemyAI(enemy);
        }
        Game.getInstance().setAvatars(avatars);

        avatarBullets.setProgress(1);
        avatarBullets.setStyle("-fx-accent: green;");
    }
    private Vector generateRandomPosition(){

        int x = random.nextInt(((int)canvas.getWidth()-40)+25);
        int y = random.nextInt(((int)canvas.getHeight()-40)+25);
        if(!checkColission(x,y)) return new Vector(x,y);
        return generateRandomPosition();
    }
    private boolean checkColission(int x, int y){//true si colisionan
        for(int i = 0; i < obstacles.size(); i++){
            if (obstacles.get(i).bounds.intersects(x-20,y-20, 45, 45)) {
                System.out.println("colides");
                return true;
            }
        }
        for(int i = 0; i < avatars.size(); i++){
            if (avatars.get(i).bounds.intersects(x-20,y-20, 45, 45)) {
                System.out.println("colides");
                return true;
            }
        }
        return false;
    }

    public void draw() {
        AtomicReference<String> message = new AtomicReference<>("GAME OVER"); // por si pierde

        new Thread(() -> {
            while (isRunning) {
                Platform.runLater(() -> {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    for (Gun gun : gunsInFloor) {
                        gun.render(gc);
                    }

                    for (Gun gun : gunsInFloor) {
                        if (avatar.collidesWith(gun)) {
                            avatar.setGun(gun);
                            gunsInFloor.remove(gun);
                            break;
                        }
                    }

                    if (avatar.hasGun()) {
                        Gun currentGun = avatar.getGun();
                        currentGun.setPos(new Vector(10, 10));
                        currentGun.render(gc);
                    }

                    drawObstacles();

                    renderAvatar(avatar, avatarLife, avatarBullets); // render Player

                    int deadEnemies = 0;

                    for (int i = 1; i < avatars.size(); i++) { // render Enemies
                        if (!renderAvatar(avatars.get(i), progressBars.get(i), null))
                            deadEnemies++;
                        //s
                    }
                    if (deadEnemies == avatars.size() - 1) { // si mato a todos los enemigos
                        isRunning = false;
                        gc.setFill(Color.GREEN);
                        message.set("WINNER");
                    }
                    doKeyboardActions();
                });
                // Sleep
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFill(Color.RED);
            Font font = new Font("Impact", 38);
            gc.setFont(font);
            gc.fillText(message + "\n\nPuntaje final: " + score, canvas.getWidth() / 2, canvas.getHeight() / 2);
        }).start();
    }

    private void doKeyboardActions() {
        //mover avatar
        if (up && avatar.pos.getY() - 15 > 0 && !detectCollisionUp(avatar)) {
            avatar.pos.setY(avatar.pos.getY() - 3);
        }
        if (left && avatar.pos.getX() - 20 > 0 && !detectCollisionLeft(avatar)) {
            avatar.pos.setX(avatar.pos.getX() - 3);
        }
        if (down && avatar.pos.getY() + 20 <= canvas.getHeight() && !detectCollisionDown(avatar)) {
            avatar.pos.setY(avatar.pos.getY() + 3);
        }
        if (right && avatar.pos.getX() + 20 < canvas.getWidth() && !detectCollisionRight(avatar)) {
            avatar.pos.setX(avatar.pos.getX() + 3);
        }
    } //mover avatar
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
                if (avatar.pos.getX() - 3 > 0) {
                    left = true;
                }
                break;
            case UP, W:
                if (avatar.pos.getY() - 3 > 0) {
                    up = true;
                }
                break;
            case RIGHT, D:
                if (avatar.pos.getX() + 3 < canvas.getWidth()) {
                    right = true;
                }
                break;
            case DOWN, S:
                if (avatar.pos.getY() + 3 < canvas.getHeight()) {
                    down = true;
                }
                break;
            case R:
                avatar.reload();
                break;
            case SPACE:
                avatar.shoot();
                break;
        }
    }

    private void createMap1(){
        map1= new ArrayList<>();

        //portal obstaculo 0 en el arreglo
        map1.add(new Obstacle(canvas, canvas.getWidth()-40,canvas.getHeight()/2, "Portal.png"));

        Obstacle obstacle1 = (new Obstacle(canvas, 150, 150));
        Obstacle obstacle2 = new Obstacle(canvas, 150, obstacle1.bounds.getY() + obstacle1.HEIGHT);
        map1.add(obstacle1);
        map1.add(obstacle2);
        map1.add(new Obstacle(canvas, 150, obstacle2.bounds.getY() + obstacle2.HEIGHT));
        map1.add(new Obstacle(canvas, 190, obstacle1.bounds.getY()));
        map1.add(new Obstacle(canvas, 230, obstacle1.bounds.getY()));

        map1.add(new Obstacle(canvas,150,450));
        map1.add(new Obstacle(canvas, 150, 410));
        map1.add(new Obstacle(canvas, 150, 370));
        map1.add(new Obstacle(canvas, 190, 450));
        map1.add(new Obstacle(canvas, 230, 450));
        map1.add(new Obstacle(canvas, 604, 150));
        map1.add(new Obstacle(canvas, 604, 190));
        map1.add(new Obstacle(canvas, 604, 230));
        map1.add(new Obstacle(canvas, 564, 150));
        map1.add(new Obstacle(canvas, 524, 150));
        map1.add(new Obstacle(canvas, 604, 450));
        map1.add(new Obstacle(canvas, 604, 410));
        map1.add(new Obstacle(canvas, 604, 370));
        map1.add(new Obstacle(canvas, 564, 450));
        map1.add(new Obstacle(canvas, 524, 450));
        map1.add(new Obstacle(canvas, 377, 300));
        map1.add(new Obstacle(canvas, 377, 260));
        map1.add(new Obstacle(canvas, 377, 340));
        map1.add(new Obstacle(canvas, 337, 300));
        map1.add(new Obstacle(canvas, 417, 300));


    }
    private void createMap2(){
        map2= new ArrayList<>();
        //posicion 0 es el portal
        map1.add(new Obstacle(canvas, canvas.getWidth()-40,canvas.getHeight()/2, "Portal.png"));

        map2.add(new Obstacle(canvas,140,300));
        map2.add(new Obstacle(canvas, 180, 300));
        map2.add(new Obstacle(canvas, 220, 300));
        map2.add(new Obstacle(canvas, 260, 300));
        map2.add(new Obstacle(canvas, 300, 300));
        map2.add(new Obstacle(canvas, 340, 300));
        map2.add(new Obstacle(canvas, 380, 300));
        map2.add(new Obstacle(canvas, 420, 300));
        map2.add(new Obstacle(canvas, 460, 300));
        map2.add(new Obstacle(canvas, 500, 300));

        map2.add(new Obstacle(canvas, 140, 320));
        map2.add(new Obstacle(canvas, 140, 360));
        map2.add(new Obstacle(canvas, 140, 400));
        map2.add(new Obstacle(canvas, 140, 440));
        map2.add(new Obstacle(canvas, 140, 480));
        map2.add(new Obstacle(canvas, 140, 520));

        map2.add(new Obstacle(canvas, 140, 280));
        map2.add(new Obstacle(canvas, 140, 240));
        map2.add(new Obstacle(canvas, 140, 200));
        map2.add(new Obstacle(canvas, 140, 160));
        map2.add(new Obstacle(canvas, 140, 120));
        map2.add(new Obstacle(canvas, 140, 80));

        map2.add(new Obstacle(canvas, 500, 320));
        map2.add(new Obstacle(canvas, 500, 360));
        map2.add(new Obstacle(canvas, 500, 400));
        map2.add(new Obstacle(canvas, 500, 440));
        map2.add(new Obstacle(canvas, 500, 480));
        map2.add(new Obstacle(canvas, 500, 520));

        map2.add(new Obstacle(canvas, 500, 280));
        map2.add(new Obstacle(canvas, 500, 240));
        map2.add(new Obstacle(canvas, 500, 200));
        map2.add(new Obstacle(canvas, 500, 160));
        map2.add(new Obstacle(canvas, 500, 120));
        map2.add(new Obstacle(canvas, 500, 80));

    }
    private void createMap3(){
        map3= new ArrayList<>();
        //posicion 0 es el portal
        map1.add(new Obstacle(canvas, canvas.getWidth()-40,canvas.getHeight()/2, "Portal.png"));

        map3.add(new Obstacle(canvas, 340, 300));
        map3.add(new Obstacle(canvas, 380, 300));
        map3.add(new Obstacle(canvas, 420, 300));
        map3.add(new Obstacle(canvas, 460, 300));

        map3.add(new Obstacle(canvas, 140, 320));
        map3.add(new Obstacle(canvas, 140, 360));
        map3.add(new Obstacle(canvas, 140, 400));

        map3.add(new Obstacle(canvas, 140, 280));
        map3.add(new Obstacle(canvas, 140, 240));

        map3.add(new Obstacle(canvas, 140, 120));
        map3.add(new Obstacle(canvas, 140, 80));

        map3.add(new Obstacle(canvas, 500, 480));
        map3.add(new Obstacle(canvas, 500, 520));

        map3.add(new Obstacle(canvas, 500, 280));

        map3.add(new Obstacle(canvas, 500, 160));
        map3.add(new Obstacle(canvas, 500, 120));
        map3.add(new Obstacle(canvas, 500, 80));

        map3.add(new Obstacle(canvas, 600, 100));
        map3.add(new Obstacle(canvas, 640, 140));
        map3.add(new Obstacle(canvas, 680, 180));

        map3.add(new Obstacle(canvas, 600, 500));
        map3.add(new Obstacle(canvas, 640, 540));
        map3.add(new Obstacle(canvas, 680, 580));
    }

    private void drawObstacles(){
        for(int i = 0; i < obstacles.size(); i++){
            obstacles.get(i).draw();
        }
    }


    private void renderGun(Gun gun){

    }

    private boolean renderAvatar(Avatar avatar, ProgressBar life, ProgressBar bullets) {
        //retorna false si esta muerto
        //Si muere el jugador principal (El que no tenga color rojo)
        if(!avatar.isAlive && avatar.color!=Color.RED) isRunning=false;

        if (avatar.isAlive) {
            avatar.draw();
            avatar.manageBullets(avatars);
            life.setProgress((double) avatar.getHearts() / 3.0);
            if (bullets != null) {
                bullets.setProgress((double) avatar.numBullets / GameViewController.RELOAD_FACTOR);
            }
        } else {
            life.setProgress(0);
            if (bullets != null)  bullets.setProgress(0);

            return false;
        }
        return true;
    }
//    score += 10;
//    scoreLbl.setText("Puntaje: " + score);
    public boolean detectCollisionRight(Avatar avatar){

        for(int i = 0; i < obstacles.size(); i++){
            if (obstacles.get(i).bounds.intersects(avatar.bounds.getX()+3,avatar.bounds.getY(), 40, 40)) {
                System.out.println("collision r");
                return true;
            }
        }
        return false;
    }
    public boolean detectCollisionLeft(Avatar avatar){

        for(int i = 0; i < obstacles.size(); i++){
            if (obstacles.get(i).bounds.intersects(avatar.bounds.getX()-3,avatar.bounds.getY(), 40, 40)) {
                System.out.println("collision l");
                return true;
            }
        }
        return false;
    }
    public boolean detectCollisionUp(Avatar avatar) {

        for (int i = 0; i < obstacles.size(); i++) {
            //if (obstacles.get(i).bounds.intersects(avatar.pos.x - 20, avatar.pos.y -3- avatar.direction.y - 20, 50, 50)) {
            if (obstacles.get(i).bounds.intersects(avatar.bounds.getX(),avatar.bounds.getY()-3, 40, 40)) {
                System.out.println("collision up");
                return true;
            }
        }
        return false;
    }
    public boolean detectCollisionDown(Avatar avatar) {

        for (int i = 0; i < obstacles.size(); i++) {
            if (obstacles.get(i).bounds.intersects(avatar.bounds.getX(),avatar.bounds.getY()+3, 40, 40)) {
                System.out.println("collision d");
                return true;
            }
        }
        return false;
    }

    public boolean detectCollisionForward(Avatar avatar){
        double newX = avatar.pos.getX() + avatar.direction.x;
        double newY = avatar.pos.getY() + avatar.direction.y;

        if (newX + 20 >= canvas.getWidth() || newX - 20 <= 0 ||
                newY + 20 >= canvas.getHeight() || newY - 20 <= 0) {
            return true;
        }

        for(int i = 0; i < obstacles.size(); i++){

            if(obstacles.get(i).bounds.intersects(avatar.pos.x + avatar.direction.x - 25, avatar.pos.y + avatar.direction.y - 25, 50, 50))
                return true;

        }

        return false;

    }

    private void enemyAI(Avatar enemy1) {
        new Thread(() -> {
            while (enemy1.isAlive) {
                if (!enemy1.hasBullets()) enemy1.reload();

                // (1-3)
                int random = (int) (Math.random() * (3 - 1 + 1) + 1);

                if (random == 1) {
                    for (int i = 0; i < 20; i++) {
                        if (!detectCollisionForward(enemy1))
                            enemy1.moveForward();
                        else
                            enemy1.changeAngle(180);
                    }
                }

                if (random == 2) {
                    enemy1.changeAngle(-25);

                    // Verificar si hay colisión con el jugador antes de disparar
                    if (!detectCollisionForward(enemy1)) {
                        enemy1.shoot();
                    }
                }

                if (random == 3) {
                    enemy1.changeAngle(25);

                    // Verificar si hay colisión con el jugador antes de disparar
                    if (!detectCollisionForward(enemy1)) {
                        enemy1.shoot();
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


    @FXML
    public void onEndGameButton(ActionEvent actionEvent) {
        System.out.println("EndGameButton");
        HelloApplication.hideWindow((Stage) canvas.getScene().getWindow());
        HelloApplication.showWindow("MenuView");
    }

    public List<Gun> getGunsInFloor() {
        return gunsInFloor;
    }

    public void setGunsInFloor(List<Gun> gunsInFloor) {
        this.gunsInFloor = gunsInFloor;
    }


    public static void setLevel(int level) {
        GameViewController.level = level;

    }
}