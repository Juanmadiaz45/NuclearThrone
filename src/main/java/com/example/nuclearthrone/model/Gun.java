package com.example.nuclearthrone.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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

    public Gun(double x, double y, Image sprite, Canvas canvas, Vector pos ) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        this.pos = pos;
        bounds = new Rectangle(pos.x, pos.y, 25, 25);
        this.sprite = sprite;
        randomizePosition(x, y);
    }

    private boolean checkCollisions() {
        // Perform collision detection logic here
        // You can check for collisions with other objects in your game
        // and return true if a collision occurs, or false otherwise
        return false; // Placeholder, update with your collision detection logic
    }

    public void render(GraphicsContext gc) {
        gc.save();
        gc.translate(x, y);
        gc.drawImage(sprite, -sprite.getWidth() / 2, -sprite.getHeight() / 2);
        gc.restore();
    }
    private void randomizePosition(double w, double h) {
        Random random = new Random();
        double gunWidth = sprite.getWidth();
        double gunHeight = sprite.getHeight();

        // Generate random x and y positions within the canvas limits
        x = random.nextDouble() * (w - gunWidth);
        y = random.nextDouble() * (h - gunHeight);

        // Check for collisions with other objects
        boolean collided = checkCollisions();
        if (collided) {
            // If a collision occurred, recursively generate new random positions
            randomizePosition(w,h);
        }
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
