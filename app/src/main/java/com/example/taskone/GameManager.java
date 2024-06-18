package com.example.taskone;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.Arrays;

public class GameManager {

    @SuppressLint("StaticFieldLeak")

    public static GameManager game;
    public final int ROWS = 5, COLUMNS = 3;
    private final boolean[][] roadMap = new boolean[ROWS][COLUMNS];
    private int carIndex = 1;
    private int lives = 3;
    private boolean isLost = false;
    private final Context context;

    private GameManager(Context context) {
        this.context = context;
        this.restartRoadMap();
    }


    public static void init(Context context) {
        if (game == null) {
            GameSignal.init(context);
            game = new GameManager(context.getApplicationContext());
        }
    }

    public void restartRoadMap() {
        for (boolean[] booleans : this.roadMap) {
            Arrays.fill(booleans, false);
        }
    }

    public void moveCar(Direction direction) {

        if (Direction.LEFT == direction) {
            if (this.carIndex == 0)
                return;
            this.carIndex--;

        } else if (Direction.RIGHT == direction) {
            if (this.carIndex == 2)
                return;
            this.carIndex++;
        }
    }

    public void newObstacle() {
        int random = (int) (Math.random() * 3);
        this.roadMap[0][random] = true;
    }

    public void updateRoadMap() {
        for (int i = getROWS() - 1; i >= 0; i--) {
            for (int j = 0; j < getCOLUMNS(); j++) {

                if (isObstacleInIndex(i, j)) {
                    if (i == getROWS() - 1) {
                        if (carIndex == j)
                            crash();
                    } else
                        this.roadMap[i + 1][j] = this.roadMap[i][j];
                }
                this.roadMap[i][j] = false;
            }
        }
    }

    public boolean isObstacleInIndex(int row, int column) {
        return this.roadMap[row][column];
    }

    public static GameManager getInstance() {
        return game;
    }

    public int getLives() {
        return lives;
    }


    public int getROWS() {
        return ROWS;
    }

    public int getCOLUMNS() {
        return COLUMNS;
    }

    public boolean[][] getRoadMap() {
        return roadMap;
    }

    public int getCarIndex() {
        return carIndex;
    }

    public GameManager setLives(int lives) {
        this.lives = lives;
        return this;
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean lost) {
        isLost = lost;
    }

    public void crash() {
        lives--;
        if (lives == 0)
            setLost(true);
        else
            GameSignal.getInstance().toast("CRASHED\nYou have " + (lives > 1 ? lives + "lives" : "1 life") + " left");

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