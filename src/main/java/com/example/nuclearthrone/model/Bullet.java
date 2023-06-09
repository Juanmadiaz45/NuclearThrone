package com.example.nuclearthrone.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Bullet {

    private Canvas canvas;
    private GraphicsContext gc;
    public Vector pos;
    public Vector direction;
    public Color color;

    public Bullet(Canvas canvas, Color color, Vector pos, Vector dir){
        this.color = color;
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.pos = pos;
        this.direction = dir;
    }
    public void draw(){

        gc.setFill(color);
        gc.fillOval(pos.x-5,pos.y-5, 10,10);
        pos.x += direction.x;
        pos.y += direction.y;
    }

}