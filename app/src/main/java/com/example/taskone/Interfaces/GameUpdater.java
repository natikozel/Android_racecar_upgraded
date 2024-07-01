package com.example.taskone.Interfaces;

import com.example.taskone.enums.Spawn;

public interface GameUpdater {

    void spawnAppears(int i, int j, Spawn spawn);

    void spawnDisappears(int i, int j, Spawn spawn);

    void updateLives(int lives);

    void updateOdometer(int odometer);

    void moveCar(int carIndex);

}
