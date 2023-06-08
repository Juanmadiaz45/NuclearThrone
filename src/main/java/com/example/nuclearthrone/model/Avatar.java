package com.example.nuclearthrone.model;

import com.example.nuclearthrone.HelloApplication;
import com.example.nuclearthrone.HelloApplication;
import com.example.nuclearthrone.control.GameViewController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Avatar {

    private String name;
    private Canvas canvas;
    private GraphicsContext gc;
    public Rectangle bounds;
    private Image img;
    public Color color;
    public Vector pos;
    public Vector direction;
    private int hearts;
    public boolean isAlive;
    private List<Bullet> bullets;
    public int numBullets = GameViewController.RELOAD_FACTOR;

    public Avatar(String name, Canvas canvas, Image img, Color color, Vector pos, Vector direction){
        this.name = name;
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        this.img = img;
        this.color = color;
        this.pos = pos;
        this.direction = direction;
        bounds = new Rectangle(pos.x, pos.y, 50, 50);
        bullets = new ArrayList<>();
        hearts = 3;
        isAlive = true;
    }

    public void draw(){
        gc.save();
        gc.translate(pos.x, pos.y);
        gc.rotate(90+direction.getAngle());

        bounds.setX(pos.x-25);
        bounds.setY(pos.y-25);

        gc.drawImage(img, -25,-25, 50,50);
        gc.restore();
    }

    public void setPosition(double x, double y) {
        pos.x = (int) x - 25;
        pos.y = (int) y - 25;
    }

    public void changeAngle(double a){
        double amp = direction.getAmplitude();
        double angle = direction.getAngle();
        angle += a;
        direction.x = amp*Math.cos(Math.toRadians(angle));
        direction.y = amp*Math.sin(Math.toRadians(angle));
    }

    public void moveAvatar(){

    }
//    public void moveForward(){
//
//        if((pos.x + direction.x > factor && pos.x + direction.x < canvas.getWidth() - factor) &&
//                (pos.y + direction.y > factor && pos.y + direction.y < canvas.getHeight() - factor)){
//
//            pos.x += direction.x;
//            pos.y += direction.y;
//
//        } else if (name.equals("CPU")) {
//            changeAngle(180);
//        }
//
//    }

    public void moveBackward() {

        int factor = 20;

        if((pos.x - direction.x > factor && pos.x - direction.x < canvas.getWidth() - factor) &&
                (pos.y - direction.y > factor && pos.y - direction.y < canvas.getHeight() - factor)){

            pos.x -= direction.x;
            pos.y -= direction.y;
        }

    }

    public void manageBullets(List<Avatar> target){

        new Thread(() -> {

            for(int i = 0; i < bullets.size(); i++) {
                bullets.get(i).draw();

                for(int j = 0; j < target.size(); j++){

                    if(target.get(j).equals(this)) continue;

                    double c1 = bullets.get(i).pos.x - target.get(j).pos.x;
                    double c2 = bullets.get(i).pos.y - target.get(j).pos.y;
                    double distance = Math.sqrt(Math.pow(c1, 2) + Math.pow(c2, 2));
                    if (distance < 25) {
                        target.get(j).decreaseHearts();
                        bullets.remove(i);
                        return;
                    }

                }

                if (bullets.get(i).pos.x > canvas.getWidth() + 20 ||
                        bullets.get(i).pos.y > canvas.getHeight() + 20 ||
                        bullets.get(i).pos.y < -20 ||
                        bullets.get(i).pos.x < -20) {
                    bullets.remove(i);
                }

            }

        }).start();

    }

    public void shoot(){
        Bullet bullet = new Bullet(canvas, color, new Vector(pos.x, pos.y), new Vector(3 * direction.x, 3 * direction.y));
        bullets.add(bullet);
        numBullets--;
    }

    public Vector getCenter() {
        return new Vector(pos.x, pos.y);
    }



    public void reload(){
        numBullets = GameViewController.RELOAD_FACTOR;
    }

    public boolean hasBullets(){
        return numBullets > 1;
    }

    public void decreaseHearts(){

        if(hearts - 1 == 0){
            isAlive = false;
            pos.x = -Double.MAX_VALUE;
            pos.y = -Double.MAX_VALUE;
        }
        else
            hearts--;
    }

    public int getHearts(){
        return hearts;
    }

    public String getName(){
        return name;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public Vector getPos() {
        return pos;
    }

    public void setPos(Vector pos) {
        this.pos = pos;
    }
}