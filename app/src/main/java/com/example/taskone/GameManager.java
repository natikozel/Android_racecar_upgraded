package com.example.taskone;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.Arrays;

public class GameManager {

    @SuppressLint("StaticFieldLeak")

    public static GameManager game;

    private int lives = 3;
    private boolean isLost = false;
    private final Context context;

    private GameManager(Context context) {
        this.context = context;
    }


    public static void init(Context context) {
        if (game == null) {
            GameSignal.init(context);
            game = new GameManager(context.getApplicationContext());
        }
    }

    public static GameManager getInstance() {
        return game;
    }

    public int getLives() {
        return lives;
    }


    public GameManager setLives(int lives) {
        this.lives = lives;
        return this;
    }

    public boolean isLost() {
        return isLost;
    }

    public GameManager setLost(boolean lost) {
        isLost = lost;
        return this;
    }

    public void crash() {
        lives--;
        if (lives == 0) {
            setLost(true);
        } else {
            GameSignal.getInstance().toast("CRASHED\nYou have " + (lives > 1 ? lives + "lives" : "1 life") + " left");
        }
        GameSignal.getInstance().vibrate();

    }

    public void announceAsLost() {
        GameSignal.getInstance().toast("You lost! Restarting...");
        restart();
    }

    private void restart() {
        setLives(3);
        setLost(false);
    }

    public void vibrate() {
        GameSignal.getInstance().vibrate();
    }


}