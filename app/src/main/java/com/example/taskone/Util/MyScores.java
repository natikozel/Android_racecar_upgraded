package com.example.taskone.Util;

import java.util.ArrayList;


public class MyScores {
    private ArrayList<Score> allScores;

    public MyScores() {
    }

    public ArrayList<Score> getAllScores() {
        if (this.allScores == null) {
            this.allScores = new ArrayList<>();
        }
        return this.allScores;
    }
}
