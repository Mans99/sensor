package com.example.sensor;
import android.content.Context;

import android.hardware.Sensor;

import android.hardware.SensorEvent;

import android.hardware.SensorEventListener;

import android.hardware.SensorManager;


public class Accelerometer {
    // create an interface with one method
    private float x, y, z;

    public interface Listener {
        // create method with all 3
        // axis translation as argument
        void onTranslation(float tx, float ty, float tz);
    }

    // create an instance
    private Listener listener;


    // method to set the instance
    public void setListener(Listener l) {
        listener = l;
    }


    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;


    // create constructor with
    // context as argument
    Accelerometer(Context context) {
        // create instance of sensor manager
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        // create instance of sensor
        // with type linear acceleration
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // create the sensor listener
        sensorEventListener = new SensorEventListener() {
            // this method is called when the
            // device's position changes

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                x = sensorEvent.values[0];
                y = sensorEvent.values[1];
                z = sensorEvent.values[2];

                if (x < 0.5) {
                    x = 0;
                }

                if (y < 0.5) {
                    y = 0;
                }

                if (z < 0.5) {
                    z = 0;
                }
                // check if listener is
                // different from null
                if (listener != null) {
                    // pass the three floats in listener on translation of axis
                    listener.onTranslation(x,y,z);
                }
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    // create register method
    // for sensor notifications
    public void register() {
        // call sensor manger's register listener
        // and pass the required arguments
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    // create method to unregister
    // from sensor notifications
    public void unregister() {

        // call sensor manger's unregister listener
        // and pass the required arguments
        sensorManager.unregisterListener(sensorEventListener);
    }
}