package com.example.nuclearthrone.model;

import com.example.nuclearthrone.HelloApplication;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Obstacle {
    private Canvas canvas;
    private GraphicsContext gc;
    public Rectangle bounds;
    public int hits;
    private Image img;
    public int WIDTH = 40;
    public int HEIGHT = 40;
    private int hitCount;

    public Obstacle(Canvas canvas, double x, double y){
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        hits  = 3;
        bounds = new Rectangle(x, y, WIDTH, HEIGHT);
        img = new Image("file:"+ HelloApplication.class.getResource("wall.png").getPath());
        bounds.setFill(new ImagePattern(img));

        hitCount = 0;

    }
    public Obstacle(Canvas canvas, double x, double y, String imgString){
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        bounds = new Rectangle(x, y, 50, 50);
        img = new Image("file:"+ HelloApplication.class.getResource(imgString).getPath());
        bounds.setFill(new ImagePattern(img));
        System.out.println("PortalCreado");

    }


    public void draw(){
        gc.drawImage(img, bounds.getX(), bounds.getY(), HEIGHT, WIDTH);

    }

    public int getHitCount(){
        return hitCount;
    }

    public void incrementHitCount() {
        hitCount++;
    }

    public boolean shouldBeRemoved() {
        return hitCount >= 4;
    }

    public void render() {
        gc.drawImage(img, bounds.getX(), bounds.getY(), WIDTH, HEIGHT);
    }

}