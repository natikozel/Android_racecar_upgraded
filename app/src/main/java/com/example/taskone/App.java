package com.example.taskone;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.taskone.Util.MyScores;
import com.example.taskone.Util.Score;
import com.google.gson.Gson;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ScoreManager.init(this);
        Gson gson = new Gson();
        MyScores scores = gson.fromJson(ScoreManager.getInstance().loadDB(), MyScores.class);

        if (scores == null) {
            scores = new MyScores();
            ScoreManager.getInstance().putString(gson.toJson(scores));
        }
    }
}