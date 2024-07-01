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

public class TiltDetector {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener sensorEventListener;

    public TiltDetector(Context context) {
        this.sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER);

        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if (event.values[0] > 1)
                    GameManager.getInstance().moveCar(Direction.LEFT);
                else if (event.values[0] < -1)
                    GameManager.getInstance().moveCar(Direction.RIGHT);

            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // pass
            }
        };
    }


    public void start() {
        sensorManager.registerListener(sensorEventListener, accelerometer, SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(sensorEventListener, accelerometer);
    }

}

