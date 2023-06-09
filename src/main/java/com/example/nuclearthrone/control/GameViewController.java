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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
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
    private static GameViewController instance;
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
    private Button endGameButton;
    @FXML
    private Button tryAgainButton;
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
    private boolean isReloading = false;

    public static final int RELOAD_FACTOR = 10;
    public static GameViewController getInstance() {
        return instance;
    }
    public GameViewController() {
        instance = this;
        avatars = new ArrayList<>();
        score=0;
        actualMap=0;
        level=1;
        actualMap=1;
        progressBars = new ArrayList<>();
        obstacles = new ArrayList<>();
        gunsInFloor = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("inicializado a nivel "+level);
        isRunning=true;
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(this::onKeyPressed);
        canvas.setOnKeyReleased(this::onKeyReleased);
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseMoved(this::onMouseMoved);
        //canvas.toFront();


        Image avatarImg = new Image("file:" + HelloApplication.class.getResource("RebelWalk1.png").getPath());
        avatar = new Avatar(Game.getInstance().getPlayer(), canvas, avatarImg, Color.YELLOW, new Vector(50, 50), new Vector(1, 1));
        avatars.add(avatar);
        progressBars.add(avatarLife);

        Image gun1Image = new Image("file:" + HelloApplication.class.getResource("revolver1.png").getPath());
        Image gun2Image = new Image("file:" + HelloApplication.class.getResource("weirdRevolver.png").getPath());
        Image gun3Image = new Image("file:" + HelloApplication.class.getResource("glock.png").getPath());

        gun1 = new Gun(0, 0, gun1Image, canvas, generateRandomPosition(),Color.GREEN);
        gun2 = new Gun(0, 0, gun2Image, canvas,generateRandomPosition(),Color.ORANGE);
        gun3 = new Gun(0, 0, gun3Image, canvas, generateRandomPosition(),Color.BLUE);

        gunsInFloor.add(gun1);
        gunsInFloor.add(gun2);
        gunsInFloor.add(gun3);
        Game.getInstance().setGunsOnFloor(gunsInFloor);

        createMap1();
        createMap2();
        createMap3();

        obstacles=map1;

        createRandomEnemies(level);

        draw();

        System.out.println(gun1Image.getUrl());
        System.out.println(gun2Image.getUrl());
        System.out.println(gun3Image.getUrl());

        System.out.println("Numero de armas en el suelo: " + gunsInFloor.size());
    }
    @FXML
    StackPane stackPane;
    private void onMouseMoved(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        System.out.println("Mouse Movement");
        gc.setStroke(avatar.color);
        gc.setLineWidth(1.0);
        double crossSize = 10.0;
        gc.strokeLine(x - crossSize, y, x + crossSize, y);
        gc.strokeLine(x, y - crossSize, x, y + crossSize);
    }
    @FXML
    private void onMousePressed(MouseEvent e) {
        System.out.println("X: " +e.getX() + "Y: "+e.getY());

        double diffX = e.getX() - avatar.pos.getX();
        double diffY = e.getY() - avatar.pos.getY();
        Vector diff = new Vector(diffX, diffY);
        diff.normalize();


    }

    private void createRandomEnemies(int enemyRange) {
        System.out.println("nivel "+level);
        numOfEnemies = level+random.nextInt((3)+1);
        System.out.println(numOfEnemies);
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

        int x = random.nextInt(((int)canvas.getWidth()-50)+25);
        int y = random.nextInt(((int)canvas.getHeight()-50)+25);
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
                        gun.render();
                    }

                    for (Gun gun : gunsInFloor) {
                        if (avatar.collidesWith(gun)) {
                            avatar.setGun(gun);
                            avatar.color=gun.color;
                            gunsInFloor.remove(gun);
                            break;
                        }
                    }

                    if (avatar.hasGun()) {
                        Gun currentGun = avatar.getGun();
                        gc.setFill(currentGun.color);
                        //Font font = new Font("Impact", 38);
                        //gc.setFont(font);
                        gc.fillText("Current Gun: ", 5,140);
                        currentGun.setPos(new Vector(15, 160));


                        currentGun.render();
                    }

                    drawObstacles();

                    renderAvatar(avatar, avatarLife, avatarBullets); // render Player

                    int deadEnemies = 0;

                    for (int i = 1; i < avatars.size(); i++) { // render Enemies
                        if (!renderAvatar(avatars.get(i), progressBars.get(i), null))
                            deadEnemies++;
                        //s
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
            onEndGameButton();//mostrar pantalla de endgame
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
                if( avatar.hasGun() && avatar.getNumBullets() > 0){
                    avatar.shoot();
                }
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
        map2.add(new Obstacle(canvas, canvas.getWidth()-40,canvas.getHeight()/2, "Portal.png"));

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
        map3.add(new Obstacle(canvas, canvas.getWidth()-40,canvas.getHeight()/2, "Portal.png"));

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
            if (bullets != null) {//Aqui solo entra el avatar del jugador, los enemigos tienen bullets null
                bullets.setProgress((double) avatar.numBullets / GameViewController.RELOAD_FACTOR);
                int deadEnemies=0;
                for (int i = 1; i < avatars.size(); i++) { // verificar enemigos matados
                    if (!avatars.get(i).isAlive) deadEnemies++;
                }
                score=deadEnemies*100;
                scoreLbl.setText("Puntaje: " + score);
                if(deadEnemies == avatars.size() - 1 && avatar.bounds.intersects(obstacles.get(0).bounds.getBoundsInParent())) {//cambiar de mapa
                    //Si todos los enemigos estan muertos y colisiona con el portal
                    actualMap++;
                    switch (actualMap){
                        case 2: obstacles = map2; createRandomEnemies(level+1);break;
                        case 3: obstacles=map3; createRandomEnemies(level+2); break;
                        case 4: isRunning = false;
                    }
                }
            }
        } else {
            life.setProgress(0);
            if (bullets != null)  bullets.setProgress(0);
            return false;
        }
        return true;
    }

    public boolean detectCollisionRight(Avatar avatar){

        for(int i = 1; i < obstacles.size(); i++){
            if (obstacles.get(i).bounds.intersects(avatar.bounds.getX()+3,avatar.bounds.getY(), 40, 40)) {
                System.out.println("collision r");
                return true;
            }
        }
        return false;
    }
    public boolean detectCollisionLeft(Avatar avatar){

        for(int i = 1; i < obstacles.size(); i++){
            if (obstacles.get(i).bounds.intersects(avatar.bounds.getX()-3,avatar.bounds.getY(), 40, 40)) {
                System.out.println("collision l");
                return true;
            }
        }
        return false;
    }
    public boolean detectCollisionUp(Avatar avatar) {

        for (int i = 1; i < obstacles.size(); i++) {
            //if (obstacles.get(i).bounds.intersects(avatar.pos.x - 20, avatar.pos.y -3- avatar.direction.y - 20, 50, 50)) {
            if (obstacles.get(i).bounds.intersects(avatar.bounds.getX(),avatar.bounds.getY()-3, 40, 40)) {
                System.out.println("collision up");
                return true;
            }
        }
        return false;
    }
    public boolean detectCollisionDown(Avatar avatar) {

        for (int i = 1; i < obstacles.size(); i++) {
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
    public void onTryAgainButton(ActionEvent actionEvent){
        System.out.println("Try Again button");
        HelloApplication.hideWindow((Stage) canvas.getScene().getWindow());
        HelloApplication.showWindow("MenuView");

    }

    @FXML
    public void onEndGameButton() {
        isRunning = false;
        System.out.println("EndGameButton");
        AtomicReference<String> message = new AtomicReference<>("GAME OVER");
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.RED);
        Font font = new Font("Impact", 38);
        gc.setFont(font);
        gc.fillText(message + "\n\nPuntaje final: " + score, canvas.getWidth() / 2, canvas.getHeight() / 2);

    }

    public List<Gun> getGunsInFloor() {
        return gunsInFloor;
    }

    public void setGunsInFloor(List<Gun> gunsInFloor) {
        this.gunsInFloor = gunsInFloor;
    }

    public void setLevel(int level) {
        GameViewController.level = level;
        System.out.println("nivel actualizado "+level);


    }
}