package com.example.nuclearthrone.model;

import com.example.nuclearthrone.HelloApplication;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Obstacle {
    private Canvas canvas;
    private GraphicsContext gc;
    public Rectangle bounds;
    private Image img;
    public int WIDTH = 40;
    public int HEIGHT = 40;

    public Obstacle(Canvas canvas, double x, double y){
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        bounds = new Rectangle(x, y, WIDTH, HEIGHT);
        img = new Image("file:"+ HelloApplication.class.getResource("wall.png").getPath());
        bounds.setFill(new ImagePattern(img));

    }

    public void draw(){
        gc.drawImage(img, bounds.getX(), bounds.getY(), HEIGHT, WIDTH);
    }
}
