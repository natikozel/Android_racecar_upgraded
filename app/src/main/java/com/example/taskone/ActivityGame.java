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
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.button.MaterialButton;

public class ActivityGame extends AppCompatActivity {

    private MaterialButton left;
    private MaterialButton right;
    private AppCompatImageView car;
    private FrameLayout lane1, lane2, lane3, currentLane;
    private AppCompatImageView[] lives;
    private Handler obstacleGenerator;
    private Runnable runnable;
    private boolean isRunning = false;

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
        car = findViewById(R.id.car);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        lane1 = findViewById(R.id.lane1);
        lane2 = findViewById(R.id.lane2);
        lane3 = findViewById(R.id.lane3);
        currentLane = lane2; // Default beginning lane
        GameManager.init(this);
        left.setOnClickListener(this::move);
        right.setOnClickListener(this::move);
        startObstacleGeneration(1000);


    }


    private void updateLivesUI() {
        int livesLength = GameManager.getInstance().getLives();
        lives[0].setVisibility(livesLength >= 1 ? View.VISIBLE : View.INVISIBLE);
        lives[1].setVisibility(livesLength >= 2 ? View.VISIBLE : View.INVISIBLE);
        lives[2].setVisibility(livesLength >= 3 ? View.VISIBLE : View.INVISIBLE);
    }

    public <T extends View> void move(T view) throws IllegalArgumentException {
        if (view == left) {
            if (currentLane != lane1) {
                currentLane.removeView(car);
                if (currentLane == lane2) {
                    currentLane = lane1;
                } else if (currentLane == lane3) {
                    currentLane = lane2;
                }
                currentLane.addView(car);
            }
        } else if (view == right) {
            if (currentLane != lane3) {
                currentLane.removeView(car);
                if (currentLane == lane2) {
                    currentLane = lane3;
                } else if (currentLane == lane1) {
                    currentLane = lane2;
                }
                currentLane.addView(car);
            }
        } else {
            throw new IllegalArgumentException("Invalid button");
        }
    }


    public void generateObstacle() {
        Obstacle obstacle = new Obstacle(this);
        int randomLaneIndex = (int) (Math.random() * 3);
        int laneId;
        switch (randomLaneIndex) {
            case 0:
                laneId = R.id.lane1;
                break;
            case 1:
                laneId = R.id.lane2;
                break;
            case 2:
                laneId = R.id.lane3;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + randomLaneIndex);
        }

        FrameLayout lane = findViewById(laneId);
        lane.addView(obstacle.getObstacle());
        ObjectAnimator animation = ObjectAnimator.ofFloat(obstacle.getObstacle(), "translationY", 75f, 2000f);
        animation.setDuration(3000);
        animation.setInterpolator(new LinearInterpolator());
        animation.addUpdateListener(animator -> checkCollision(obstacle));
        animation.addListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        lane.removeView(obstacle.getObstacle());
                    }
                });
        animation.start();
    }

    public void startObstacleGeneration(int initialDelay) {
        obstacleGenerator = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                generateObstacle();
                obstacleGenerator.postDelayed(this, 850);
            }
        };
        obstacleGenerator.postDelayed(runnable, initialDelay); // Initial delay
        isRunning = true;
    }

    public void stopObstacleGeneration() {
        if (obstacleGenerator != null) {
            obstacleGenerator.removeCallbacks(runnable);
            isRunning = false;
        }
    }

    public boolean isObstacleGenerationRunning() {
        return isRunning;
    }

    public void checkCollision(Obstacle obstacle) {
        // Get the coordinates of the car and the obstacle
        Rect carRect = getCarCoordinates();
        Rect obstacleRect = obstacle.getUpdatedRect();

        // Check if the rectangles intersect
        if (Rect.intersects(carRect, obstacleRect)) { // Collision detected
            GameManager.getInstance().crash();
            ((FrameLayout) obstacle.getObstacle().getParent()).removeView(obstacle.getObstacle()); // Remove the obstacle
            if (GameManager.getInstance().isLost()) {
                GameManager.getInstance().announceAsLost();
                clearObstacles();
            } else {
                GameManager.getInstance().vibrate();
            }
            updateLivesUI();
        }
    }


    public Rect getCarCoordinates() {
        int[] location = new int[2];
        car.getLocationOnScreen(location);
        return new Rect(location[0], location[1], location[0] + car.getWidth(), location[1] + car.getHeight());
    }

    public void clearObstacles() {
        clearLane(lane1);
        clearLane(lane2);
        clearLane(lane3);
    }

    private void clearLane(FrameLayout lane) {
        lane.removeAllViews();
        if (lane == currentLane) {
            lane.addView(car); // Add the car back to its lane
        }
    }
}
