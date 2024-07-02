package com.example.taskone.enums;

public enum GameMode {
    SLOW_MODE(850),
    FAST_MODE(425),
    SENSOR_MODE(425);

    private int delay;

    GameMode(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }
    public void setDelay(int delay) {
        this.delay = delay;
    }

}
