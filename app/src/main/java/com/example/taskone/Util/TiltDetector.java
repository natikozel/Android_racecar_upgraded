package com.example.taskone.Util;


import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.taskone.enums.Direction;
import com.example.taskone.GameManager;

public class TiltDetector implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    public TiltDetector(Context context) {
        this.sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER);


    }


    public void start() {
        sensorManager.registerListener(this, accelerometer, SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this, accelerometer);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] > 1)
            GameManager.getInstance().moveCar(Direction.LEFT);
        else if (event.values[0] < -1)
            GameManager.getInstance().moveCar(Direction.RIGHT);


        // BONUS - Handling tilt for speed adjustment
        int currentSpeed = GameManager.getInstance().getMode().getDelay();
        if (event.values[1] > 2.0f && currentSpeed < 850)
            GameManager.getInstance().getMode().setDelay(currentSpeed + 85);
        else if (event.values[1] < -2.0f && currentSpeed > 425)
            GameManager.getInstance().getMode().setDelay(currentSpeed - 85);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}

