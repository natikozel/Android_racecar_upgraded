package com.example.taskone;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatImageView;

public class Obstacle {

    private AppCompatImageView obstacle;

    public Obstacle(Context context) {
        this.obstacle = new AppCompatImageView(context);
        this.obstacle.setImageResource(R.mipmap.ic_rock_foreground);
        float density = context.getResources().getDisplayMetrics().density;
        int sizeInPixels = (int) (85 * density);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(sizeInPixels, sizeInPixels);
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        this.obstacle.setLayoutParams(params);

    }

    public AppCompatImageView getObstacle() {
        return obstacle;
    }

    public Rect getUpdatedRect() {
        int[] location = new int[2];
        obstacle.getLocationOnScreen(location);
        return new Rect(location[0], location[1], location[0] + obstacle.getWidth(), location[1] + obstacle.getHeight());
    }

}

