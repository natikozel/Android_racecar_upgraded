package com.example.taskone;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.example.taskone.Interfaces.MapUpdater;
import com.example.taskone.Util.MyScores;
import com.example.taskone.Util.Score;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HighScoresFragment extends Fragment {


    private MapUpdater cb;

    public void setCb(MapUpdater cb) {
        this.cb = cb;
    }

    public MapUpdater getCb() {
        return cb;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_high_scores, container, false);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutCompat allScoreViews = view.findViewById(R.id.scorePanel);
        if (ScoreManager.getInstance() == null) {
            ScoreManager.init(this.requireContext());
        }
        ArrayList<Score> allScores = new Gson().fromJson(ScoreManager.getInstance().loadDB(), MyScores.class).getAllScores();
        if (allScores.isEmpty())
            allScoreViews.findViewById(R.id.noScoresYet).setVisibility(View.VISIBLE);
        for (int i = 0; i < allScores.size(); i++) {
            Score score = allScores.get(i);
            MaterialButton btn = (MaterialButton) allScoreViews.getChildAt(i+1);
            btn.setVisibility(View.VISIBLE);
            btn.setText(score.getName() + ": " + score.getScore());
            LatLng loc = new LatLng(score.getLatitude(), score.getLongitude());
            btn.setOnClickListener(v -> {
                cb.addMarker(loc, score.getName());
                cb.zoom(loc);
            });
        }

    }
}

