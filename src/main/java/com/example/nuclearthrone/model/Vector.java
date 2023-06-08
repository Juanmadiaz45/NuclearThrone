package com.example.nuclearthrone.model;

public class Vector {

    public double x;
    public double y;

    public Vector(double x,double y){
        this.x = x;
        this.y = y;
    }

    public double getAngle(){
        return Math.toDegrees(Math.atan2(y,x));
    }

    public double getAmplitude(){
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

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
}