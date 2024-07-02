package com.example.taskone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.taskone.enums.GameMode;
import com.google.android.material.button.MaterialButton;

public class ActivityMenu extends AppCompatActivity {


    private MaterialButton fastNormalMode, slowNormalMode, sensorMode, highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void init() {
        fastNormalMode = findViewById(R.id.fastNormalMode);
        slowNormalMode = findViewById(R.id.slowNormalMode);
        sensorMode = findViewById(R.id.sensorMode);
        highScores = findViewById(R.id.highScores);

        fastNormalMode.setOnClickListener(this::startGame);
        slowNormalMode.setOnClickListener(this::startGame);
        sensorMode.setOnClickListener(this::startGame);
        highScores.setOnClickListener(this::goToHighScores);

    }

    private void goToHighScores(View view) {
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);

    }

    private <T extends View> void startGame(T button) {
        Intent intent = new Intent(getApplicationContext(), ActivityGame.class);

        if (button == slowNormalMode)
            intent.putExtra("mode", GameMode.SLOW_MODE);
        else if (button == fastNormalMode)
            intent.putExtra("mode", GameMode.FAST_MODE);
        else if (button == sensorMode)
            intent.putExtra("mode", GameMode.SENSOR_MODE);

        startActivity(intent);
    }
}