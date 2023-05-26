package com.example.nuclearthrone.model;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Avatar {
    private Vector position;
    private int size;
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private List<Bullet> bullets;

    public Avatar(Canvas canvas, Vector position) {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.position = position;
        this.size = 20;
        this.bullets = new ArrayList<>();
    }

    public void paint() {
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillOval(position.getX(), position.getY(), size, size);

        for (Bullet bullet : bullets) {
            bullet.paint();
        }
    }

    public void shoot(Vector direction) {
        Vector bulletPosition = new Vector(position.getX() + size / 2.0, position.getY() + size / 2.0);
        Bullet bullet = new Bullet(canvas, bulletPosition, direction);
        bullets.add(bullet);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }
}
