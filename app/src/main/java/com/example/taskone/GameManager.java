package com.example.taskone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;

import com.example.taskone.Interfaces.GameUpdater;
import com.example.taskone.enums.Direction;
import com.example.taskone.enums.GameMode;
import com.example.taskone.enums.Spawn;

import java.util.Arrays;

public class GameManager {

    @SuppressLint("StaticFieldLeak")

    public static GameManager game;
    private GameMode mode;
    private final int ROWS = 9, COLUMNS = 5;
    private final int[][] roadMap = new int[ROWS][COLUMNS]; // Coin = 2, Obstacle = 1, Empty = 0
    private int carIndex = 2, lives = 3, odometer = 0, score = 0;
    private boolean isLost = false;
    private final MediaPlayer crashSound;
    private GameUpdater gameUpdater;
    private final Context context;

    private GameManager(Context context, GameMode mode, GameUpdater roadUpdater) {
        this.context = context;
        this.mode = mode;
        this.crashSound = MediaPlayer.create(context, R.raw.crash_sound);
        this.gameUpdater = roadUpdater;
        restartRoadMap();
    }


    public static void init(Context context, GameMode mode, GameUpdater roadUpdater) {
        if (getInstance() == null || getInstance().getContext() != context) {
            GameSignal.init(context);
            game = new GameManager(context.getApplicationContext(), mode, roadUpdater);
        }
    }

    public Context getContext() {
        return context;
    }

    public GameMode getMode() {
        return mode;
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

        gameUpdater.moveCar(getCarIndex());
    }

    public void newSpawn() {
        int random = (int) (Math.random() * 5);
        int coinOrObstacle = (int) (Math.random() * 3);
        this.roadMap[0][random] = coinOrObstacle == 2 ? 2 : 1;
        gameUpdater.spawnAppears(0, random, coinOrObstacle == 2 ? Spawn.COIN : Spawn.OBSTACLE);
    }


    public void updateRoadMap() {
        if (isLost())
            return;
        gameUpdater.updateOdometer(++this.odometer);
        for (int i = getROWS() - 1; i >= 0; i--) {
            for (int j = 0; j < getCOLUMNS(); j++) {

                if (isSpawnInIndex(i, j)) {
                    if (i == getROWS() - 1) {
                        if (carIndex == j)
                            if (this.roadMap[i][j] == 1) {
                                crash();
                                gameUpdater.updateLives(getLives());
                            } else if (this.roadMap[i][j] == 2) {
                                incrementScore();

                            }

                    } else {
                        this.roadMap[i + 1][j] = this.roadMap[i][j];
                        gameUpdater.spawnAppears(i + 1, j, this.roadMap[i][j] == 1 ? Spawn.OBSTACLE : Spawn.COIN);
                    }
                }
                this.roadMap[i][j] = 0;
                gameUpdater.spawnDisappears(i, j, Spawn.COIN);
                gameUpdater.spawnDisappears(i, j, Spawn.OBSTACLE);
            }
        }
        newSpawn();
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


    public int getCarIndex() {
        return carIndex;
    }

    public void setLives(int lives) {
        this.lives = lives;
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

    public int getOdometer() {
        return odometer;
    }

    public void announceAsLost() {
        GameSignal.getInstance().toast("You lost!");
        restartRoadMap();
    }

    public void restart() {
        setLives(3);
        setLost(false);
    }

}