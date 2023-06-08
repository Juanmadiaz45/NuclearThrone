package com.example.nuclearthrone.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.*;

public class Game {
    private static Game instance = new Game();
    public final String DATABASE_PATH = "db/score.txt";
    private String player;

    private List<Avatar> avatars;

    private Game(){};

    public static Game getInstance(){
        return instance;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public List<Avatar> getAvatars() {
        return avatars;
    }

    public void setAvatars(List<Avatar> avatars) {
        this.avatars = avatars;
    }

    public void winner (Avatar avatar) {

    }

    public void update() {

    }

}