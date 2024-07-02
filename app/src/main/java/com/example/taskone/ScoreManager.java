package com.example.taskone;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.taskone.Util.MyScores;
import com.example.taskone.Util.Score;
import com.google.gson.Gson;

import java.util.ArrayList;

// ScoreManager.java
public class ScoreManager {
    @SuppressLint("StaticFieldLeak")
    private static ScoreManager scoreManager;
    private static final String PREFS_NAME = "highScores";
    private static final String MY_DB = "myDb";
    private SharedPreferences myPrefs;
    private Gson gson;
    private MyScores myScores;
    private Context ctx;


    private ScoreManager(Context context) {
        myPrefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        ctx = context;
        gson = new Gson();
    }

    public static void init(Context context) {
        if (getInstance() == null || getInstance().getCtx() != context) {
            scoreManager = new ScoreManager(context.getApplicationContext());
        }
    }

    public Context getCtx() {
        return ctx;
    }

    public static ScoreManager getInstance() {
        return scoreManager;
    }

    public void saveScore(String name, int score, double latitude, double longitude) {
        this.myScores = gson.fromJson(myPrefs.getString(MY_DB, ""), MyScores.class);
        this.myScores.getAllScores().add(new Score(name, score, latitude, longitude));
        this.myScores.getAllScores().sort((o1, o2) -> o2.getScore() - o1.getScore());
        if (this.myScores.getAllScores().size() > 10)
            this.myScores.getAllScores().remove(10);
        GameSignal.getInstance().toast("Score saved");
        putString(gson.toJson(this.myScores));
    }

    public void putString(String value) {
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString(MY_DB, value);
        editor.apply();
    }

    public String loadDB() {
        return myPrefs.getString(MY_DB, "");
    }

    public SharedPreferences getMyPrefs() {
        return myPrefs;
    }
}