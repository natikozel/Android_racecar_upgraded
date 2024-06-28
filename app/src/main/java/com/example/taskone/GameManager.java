package com.example.taskone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;

import java.util.Arrays;

public class GameManager {

    @SuppressLint("StaticFieldLeak")

    public static GameManager game;
    private GameMode mode;
    private final int ROWS = 9, COLUMNS = 5;
    private final int[][] roadMap = new int[ROWS][COLUMNS]; // Coin = 2, Obstacle = 1, Empty = 0
    private int carIndex = 2;
    private int lives = 3;
    private int odometer = 0;
    private int score = 0;
    private boolean isLost = false;
    private MediaPlayer crashSound;
    private final Context context;

    private GameManager(Context context, GameMode mode) {
        this.context = context;
        this.restartRoadMap();
        this.mode = mode;
        this.crashSound= MediaPlayer.create(context, R.raw.crash_sound);
    }


    public static void init(Context context, GameMode mode) {
        if (game == null) {
            GameSignal.init(context);
            game = new GameManager(context.getApplicationContext(), mode);
        }
    }

    public void restartRoadMap() {
        for (int[] array : this.roadMap) {
            Arrays.fill(array, 0); // Empty at start of the game
        }
    }

    public void moveCar(Direction direction) {

        if (Direction.LEFT == direction) {
            if (this.carIndex == 0)
                return;
            this.carIndex--;

        } else if (Direction.RIGHT == direction) {
            if (this.carIndex == 4)
                return;
            this.carIndex++;
        }
    }

    public void newSpawn() {
        int random = (int) (Math.random() * 5);
        int coinOrObstacle = (int) (Math.random() * 3);
        if (coinOrObstacle == 2) // Choose Coin
            this.roadMap[0][random] = 2;
        else                     // Choose Obstacle
            this.roadMap[0][random] = 1;
    }


    public void updateRoadMap() {
        odometer++;
        for (int i = getROWS() - 1; i >= 0; i--) {
            for (int j = 0; j < getCOLUMNS(); j++) {

                if (isSpawnInIndex(i, j)) {
                    if (i == getROWS() - 1) {
                        if (carIndex == j)
                            if (this.roadMap[i][j] == 1)
                                crash();
                            else if (this.roadMap[i][j] == 2)
                                incrementScore();

                    } else
                        this.roadMap[i + 1][j] = this.roadMap[i][j];
                }
                this.roadMap[i][j] = 0;
            }
        }
    }

    public int getOdometer() {
        return odometer;
    }

    public int getScore() {
        return score;
    }

    public int getDelay() {
        return mode.getDelay();
    }

    private void incrementScore() {
        score += 100;
        GameSignal.getInstance().toast("NEW SCORE:\n" + score);

    }

    public boolean isSpawnInIndex(int row, int column) {
        return this.roadMap[row][column] > 0;
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

    public int[][] getRoadMap() {
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
        crashSound.start();
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