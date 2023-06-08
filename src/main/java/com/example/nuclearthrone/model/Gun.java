package com.example.nuclearthrone.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

public class Gun {
    private double x;
    private double y;
    private Image sprite;

    public Gun(double x, double y, String spritePath) {

        this.sprite = new Image(spritePath);
        randomizePosition(x,y);
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
}
