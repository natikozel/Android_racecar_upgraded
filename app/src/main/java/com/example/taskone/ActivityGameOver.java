package com.example.taskone;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.taskone.Util.LocationTracker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class ActivityGameOver extends AppCompatActivity {

    private TextView score;
    private EditText editText;
    private MaterialButton save, back;
    private LocationTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        tracker = new LocationTracker(this);
        init();
    }


    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    public void init() {

        score = findViewById(R.id.scoreValue);
        save = findViewById(R.id.saveButton);
        editText = findViewById(R.id.nameText);
        back = findViewById(R.id.backButton);
        score.setText(String.valueOf(getIntent().getIntExtra("score", 0)));
        save.setOnClickListener(this::saveScore);
        back.setOnClickListener(this::goBack);
        ScoreManager.init(this);

    }

    public <T extends View> void saveScore(T button) {

        if (Objects.equals(editText.getText().toString(), "")) {
            GameSignal.getInstance().toast("Please enter a name");
            return;
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            tracker.getLocationCurrent().observe(this, location -> {
                ScoreManager.getInstance().saveScore(editText.getText().toString(), getIntent().getIntExtra("score", 0), location.getLatitude(), location.getLongitude());
                tracker.getLocationCurrent().removeObservers(this);
            });
        }

        Intent intent = new Intent(getApplicationContext(), ActivityMenu.class);
        startActivity(intent);
    }

    public void goBack(View view) {
        Intent intent = new Intent(getApplicationContext(), ActivityMenu.class);
        startActivity(intent);
    }
}
