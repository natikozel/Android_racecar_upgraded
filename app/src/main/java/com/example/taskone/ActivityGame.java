package com.example.taskone;


import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.taskone.Interfaces.GameUpdater;
import com.example.taskone.Util.TiltDetector;
import com.example.taskone.enums.Direction;
import com.example.taskone.enums.GameMode;
import com.example.taskone.enums.Spawn;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class ActivityGame extends AppCompatActivity {

    private MaterialButton left, right;
    private AppCompatImageView[] car, lives;
    private AppCompatImageView[][] obstacles, coins;
    private TextView odometer;
    private Handler ticker;
    private Runnable runnable;
    private TiltDetector tiltDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
    }

    protected void onPause() {
        super.onPause();
        if (tiltDetector != null)
            tiltDetector.stop();
        stopTicking();
    }

    protected void onResume() {
        super.onResume();
        if (tiltDetector != null)
            tiltDetector.start();
        GameManager.getInstance().restart();
        startTicking(1000);

    }

    protected void onStop() {
        super.onStop();
        stopTicking();
    }


    public void init() {

        odometer = findViewById(R.id.odometer);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        left.setOnClickListener(this::move);
        right.setOnClickListener(this::move);

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

        GameManager.init(this, (GameMode) Objects.requireNonNull(getIntent().getExtras()).getSerializable("mode"), new GameUpdater() {

            @Override
            public void spawnAppears(int i, int j, Spawn spawn) {
                if (spawn == Spawn.COIN)
                    coins[i][j].setVisibility(View.VISIBLE);
                else
                    obstacles[i][j].setVisibility(View.VISIBLE);
            }

            @Override
            public void spawnDisappears(int i, int j, Spawn spawn) {
                if (spawn == Spawn.COIN && coins[i][j].isShown())
                    coins[i][j].setVisibility(View.INVISIBLE);
                else if (spawn == Spawn.OBSTACLE && obstacles[i][j].isShown())
                    obstacles[i][j].setVisibility(View.INVISIBLE);
            }

            @Override
            public void updateLives(int livesLength) {
                lives[0].setVisibility(livesLength >= 1 ? View.VISIBLE : View.INVISIBLE);
                lives[1].setVisibility(livesLength >= 2 ? View.VISIBLE : View.INVISIBLE);
                lives[2].setVisibility(livesLength >= 3 ? View.VISIBLE : View.INVISIBLE);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void updateOdometer(int odometerValue) {
                odometer.setText("Odometer: " + odometerValue);
            }

            @Override
            public void moveCar(int carIndex) {
                for (int i = 0; i < car.length; i++) {
                    car[i].setVisibility(i == carIndex ? View.VISIBLE : View.INVISIBLE);
                }
            }
        });

        if (GameManager.getInstance().getMode() == GameMode.SENSOR_MODE) {
            this.tiltDetector = new TiltDetector(getApplicationContext());
            triggerSensorMode();
        }
    }

    public void triggerSensorMode() {
        left.setVisibility(View.GONE);
        right.setVisibility(View.GONE);
        this.tiltDetector.start();
    }


    public <T extends View> void move(T view) throws IllegalArgumentException {
        GameManager.getInstance().moveCar(view == left ? Direction.LEFT : Direction.RIGHT);
    }


    public void tick() {
        GameManager.getInstance().updateRoadMap();
        if (GameManager.getInstance().isLost()) {
            GameManager.getInstance().announceAsLost();
            stopTicking();
            Intent intent = new Intent(this, ActivityGameOver.class);
            Bundle bundle = new Bundle();
            bundle.putInt("score", GameManager.getInstance().getScore());
            bundle.putInt("odometer", GameManager.getInstance().getOdometer());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    public void startTicking(int initialDelay) {
        ticker = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!GameManager.getInstance().isLost()) {
                    tick();
                    ticker.postDelayed(this, GameManager.getInstance().getDelay());
                }
            }
        };
        ticker.postDelayed(runnable, initialDelay); // Initial delay
    }

    public void stopTicking() {
        if (ticker != null)
            ticker.removeCallbacks(runnable);
    }


}
