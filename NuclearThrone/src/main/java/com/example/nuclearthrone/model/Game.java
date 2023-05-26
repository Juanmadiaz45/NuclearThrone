package com.example.nuclearthrone.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Game {
    private Canvas canvas;
    private Avatar avatar;

    public Game() {
        // initialize
        canvas = new Canvas(800, 600);
        avatar = new Avatar(canvas, new Vector(400, 300));

        // add event for shooting
        canvas.setOnMouseClicked(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            Vector playerPosition = avatar.getPosition();
            Vector direction = new Vector(mouseX - playerPosition.getX(), mouseY - playerPosition.getY());
            direction.normalize();

            avatar.shoot(direction);
        });
    }
}