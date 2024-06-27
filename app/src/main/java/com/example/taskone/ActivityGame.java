package com.example.taskone;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.button.MaterialButton;

public class ActivityGame extends AppCompatActivity {

    private MaterialButton left;
    private MaterialButton right;
    private AppCompatImageView[] car;
    private AppCompatImageView[] lives;
    private AppCompatImageView[][] obstacles;
    private AppCompatImageView[][] coins;
    private TextView odometer;
    private Handler ticker;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
    }


    public void init() {

        lives = new AppCompatImageView[]{
                findViewById(R.id.life3),
                findViewById(R.id.life2),
                findViewById(R.id.life1)
        };
        car = new AppCompatImageView[]{
                findViewById(R.id.car0),
                findViewById(R.id.car1),
                findViewById(R.id.car2),
                findViewById(R.id.car3),
                findViewById(R.id.car4)
        };
        obstacles = new AppCompatImageView[][]{
                {findViewById(R.id.obstacle0), findViewById(R.id.obstacle1), findViewById(R.id.obstacle2), findViewById(R.id.obstacle3), findViewById(R.id.obstacle4)},
                {findViewById(R.id.obstacle5), findViewById(R.id.obstacle6), findViewById(R.id.obstacle7), findViewById(R.id.obstacle8), findViewById(R.id.obstacle9)},
                {findViewById(R.id.obstacle10), findViewById(R.id.obstacle11), findViewById(R.id.obstacle12), findViewById(R.id.obstacle13), findViewById(R.id.obstacle14)},
                {findViewById(R.id.obstacle15), findViewById(R.id.obstacle16), findViewById(R.id.obstacle17), findViewById(R.id.obstacle18), findViewById(R.id.obstacle19)},
                {findViewById(R.id.obstacle20), findViewById(R.id.obstacle21), findViewById(R.id.obstacle22), findViewById(R.id.obstacle23), findViewById(R.id.obstacle24)},
                {findViewById(R.id.obstacle25), findViewById(R.id.obstacle26), findViewById(R.id.obstacle27), findViewById(R.id.obstacle28), findViewById(R.id.obstacle29)},
                {findViewById(R.id.obstacle30), findViewById(R.id.obstacle31), findViewById(R.id.obstacle32), findViewById(R.id.obstacle33), findViewById(R.id.obstacle34)},
                {findViewById(R.id.obstacle35), findViewById(R.id.obstacle36), findViewById(R.id.obstacle37), findViewById(R.id.obstacle38), findViewById(R.id.obstacle39)},
                {findViewById(R.id.obstacle40), findViewById(R.id.obstacle41), findViewById(R.id.obstacle42), findViewById(R.id.obstacle43), findViewById(R.id.obstacle44)}
        };

        coins = new AppCompatImageView[][]{
                {findViewById(R.id.coin0), findViewById(R.id.coin1), findViewById(R.id.coin2), findViewById(R.id.coin3), findViewById(R.id.coin4)},
                {findViewById(R.id.coin5), findViewById(R.id.coin6), findViewById(R.id.coin7), findViewById(R.id.coin8), findViewById(R.id.coin9)},
                {findViewById(R.id.coin10), findViewById(R.id.coin11), findViewById(R.id.coin12), findViewById(R.id.coin13), findViewById(R.id.coin14)},
                {findViewById(R.id.coin15), findViewById(R.id.coin16), findViewById(R.id.coin17), findViewById(R.id.coin18), findViewById(R.id.coin19)},
                {findViewById(R.id.coin20), findViewById(R.id.coin21), findViewById(R.id.coin22), findViewById(R.id.coin23), findViewById(R.id.coin24)},
                {findViewById(R.id.coin25), findViewById(R.id.coin26), findViewById(R.id.coin27), findViewById(R.id.coin28), findViewById(R.id.coin29)},
                {findViewById(R.id.coin30), findViewById(R.id.coin31), findViewById(R.id.coin32), findViewById(R.id.coin33), findViewById(R.id.coin34)},
                {findViewById(R.id.coin35), findViewById(R.id.coin36), findViewById(R.id.coin37), findViewById(R.id.coin38), findViewById(R.id.coin39)},
                {findViewById(R.id.coin40), findViewById(R.id.coin41), findViewById(R.id.coin42), findViewById(R.id.coin43), findViewById(R.id.coin44)}
        };


        odometer = findViewById(R.id.odometer);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);

        GameManager.init(this, GameMode.FAST_MODE);
        left.setOnClickListener(this::move);
        right.setOnClickListener(this::move);
        startTicking(1000);


    }


    private void updateLivesUI() {
        int livesLength = GameManager.getInstance().getLives();
        lives[0].setVisibility(livesLength >= 1 ? View.VISIBLE : View.INVISIBLE);
        lives[1].setVisibility(livesLength >= 2 ? View.VISIBLE : View.INVISIBLE);
        lives[2].setVisibility(livesLength >= 3 ? View.VISIBLE : View.INVISIBLE);
    }

    public <T extends View> void move(T view) throws IllegalArgumentException {

        GameManager.getInstance().moveCar(view == left ? Direction.LEFT : Direction.RIGHT);
        int carIndex = GameManager.getInstance().getCarIndex();
        for (int i = 0; i < car.length; i++) {
            car[i].setVisibility(i == carIndex ? View.VISIBLE : View.INVISIBLE);
        }
    }


    public void tick() {
        GameManager.getInstance().updateRoadMap();
        GameManager.getInstance().newSpawn();
        updateRoadMapUI();

        if (GameManager.getInstance().isLost())
            GameManager.getInstance().announceAsLost();

        updateLivesUI();
    }


    @SuppressLint("SetTextI18n")
    public void updateRoadMapUI() {
        this.odometer.setText("Odometer: " + GameManager.getInstance().getOdometer());
        int[][] roadMap = GameManager.getInstance().getRoadMap();
        for (int i = 0; i < roadMap.length; i++) {
            for (int j = 0; j < roadMap[i].length; j++) {
                if (roadMap[i][j] > 0) {
                    if (roadMap[i][j] == 1) {
                        obstacles[i][j].setVisibility(View.VISIBLE);
                    } else {
                        coins[i][j].setVisibility(View.VISIBLE);
                    }
                } else {
                    obstacles[i][j].setVisibility(View.INVISIBLE);
                    coins[i][j].setVisibility(View.INVISIBLE);

                }
            }
        }
    }

    public void startTicking(int initialDelay) {
        ticker = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                tick();
                ticker.postDelayed(this, GameManager.getInstance().getDelay());
            }
        };
        ticker.postDelayed(runnable, initialDelay); // Initial delay
    }

    public void stopTicking() {
        if (ticker != null)
            ticker.removeCallbacks(runnable);
    }


}
