package com.example.taskone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

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
                findViewById(R.id.dragLeft),
                findViewById(R.id.dragCenter),
                findViewById(R.id.dragRight)
        };
        obstacles = new AppCompatImageView[][]{
                {findViewById(R.id.obstacle0), findViewById(R.id.obstacle1), findViewById(R.id.obstacle2)},
                {findViewById(R.id.obstacle3), findViewById(R.id.obstacle4), findViewById(R.id.obstacle5)},
                {findViewById(R.id.obstacle6), findViewById(R.id.obstacle7), findViewById(R.id.obstacle8)},
                {findViewById(R.id.obstacle9), findViewById(R.id.obstacle10), findViewById(R.id.obstacle11)},
                {findViewById(R.id.obstacle12), findViewById(R.id.obstacle13), findViewById(R.id.obstacle14)}
        };
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);

        GameManager.init(this);
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

        if (view == left)
            GameManager.getInstance().moveCar(Direction.LEFT);
        if (view == right)
            GameManager.getInstance().moveCar(Direction.RIGHT);


        switch (GameManager.getInstance().getCarIndex()) {
            case 0:
                if (!car[0].isShown()) {
                    car[0].setVisibility(View.VISIBLE);
                    car[1].setVisibility(View.INVISIBLE);
                    car[2].setVisibility(View.INVISIBLE);
                }
                break;
            case 1:
                if (!car[1].isShown()) {
                    car[0].setVisibility(View.INVISIBLE);
                    car[1].setVisibility(View.VISIBLE);
                    car[2].setVisibility(View.INVISIBLE);
                }
                break;
            case 2:
                if (!car[2].isShown()) {
                    car[0].setVisibility(View.INVISIBLE);
                    car[1].setVisibility(View.INVISIBLE);
                    car[2].setVisibility(View.VISIBLE);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + GameManager.getInstance().getCarIndex());
        }
    }


    public void tick() {
        GameManager.getInstance().updateRoadMap();
        GameManager.getInstance().newObstacle();
        updateRoadMapUI();

        if (GameManager.getInstance().isLost())
            GameManager.getInstance().announceAsLost();

        updateLivesUI();
    }


    public void updateRoadMapUI() {
        boolean[][] roadMap = GameManager.getInstance().getRoadMap();
        for (int i = 0; i < roadMap.length; i++) {
            for (int j = 0; j < roadMap[i].length; j++) {
                if (roadMap[i][j])
                    obstacles[i][j].setVisibility(View.VISIBLE);
                else
                    obstacles[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    public void startTicking(int initialDelay) {
        ticker = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                tick();
                ticker.postDelayed(this, 850);
            }
        };
        ticker.postDelayed(runnable, initialDelay); // Initial delay
    }

    public void stopTicking() {
        if (ticker != null)
            ticker.removeCallbacks(runnable);
    }


}
