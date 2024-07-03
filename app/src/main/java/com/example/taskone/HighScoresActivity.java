package com.example.taskone;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskone.Interfaces.MapUpdater;
import com.google.android.gms.maps.model.LatLng;


public class HighScoresActivity extends AppCompatActivity {

    private HighScoresFragment highScoresFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        mapFragment = new MapFragment();
        highScoresFragment = new HighScoresFragment();
        highScoresFragment.setCb(new MapUpdater() {
            @Override
            public void zoom(LatLng location) {
                mapFragment.zoomIntoMarker(location);
            }

            @Override
            public void addMarker(LatLng location, String title) {
                mapFragment.addMarker(location, title);
            }
        });

        findViewById(R.id.back).setOnClickListener(v -> {
            Intent i = new Intent(this, ActivityMenu.class);
            startActivity(i);
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameScores, highScoresFragment)
                .add(R.id.frameMap, mapFragment)
                .commit();

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


}
