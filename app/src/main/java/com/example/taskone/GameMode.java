package com.example.taskone;

public enum GameMode {
    SLOW_MODE(850),
    FAST_MODE(425);

    private final int delay;

    GameMode(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }
}
