package com.example.nuclearthrone.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Gun {
    private double x;
    private double y;
    private Image sprite;
    private Canvas canvas;
    public Vector pos;
    private GraphicsContext gc;
    public Rectangle bounds;
    public Color color;

    public Gun(double x, double y, Image sprite, Canvas canvas, Vector pos, Color color) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        this.pos = pos;
        bounds = new Rectangle(pos.x, pos.y, 25, 25);
        this.x=x;
        this.y=y;
        this.color=color;
        this.sprite = sprite;
    }

    private boolean checkCollisions() {
        // Perform collision detection logic here
        // You can check for collisions with other objects in your game
        // and return true if a collision occurs, or false otherwise
        return false; // Placeholder, update with your collision detection logic
    }


    public void render( ) {
        gc.save();
        gc.translate(x, y);
        gc.setFill(color);
        gc.fillRect( bounds.getX(), bounds.getY(), 25, 25);
        gc.drawImage(sprite, bounds.getX(), bounds.getY(), 25, 25);
        gc.restore();
    }

    // Getters and Setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public void setPos(Vector pos) {
        this.pos = pos;
        bounds.setX(pos.x - 12.5);
        bounds.setY(pos.y - 12.5);
    }
}
