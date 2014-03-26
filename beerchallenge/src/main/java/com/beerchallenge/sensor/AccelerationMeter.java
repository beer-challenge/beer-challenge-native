package com.beerchallenge.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerationMeter implements SensorEventListener {

    private Sensor sensor;
    private float[] latest = {0.0f, 0.0f, 0.0f};

    public AccelerationMeter(SensorManager sensorManager){
        Sensor linearAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if(linearAcc != null){
            sensor = linearAcc;
        }
    }

    public void start(SensorManager sensorManager){
        if(sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void stop(SensorManager sensorManager){
        if(sensor != null){
            sensorManager.unregisterListener(this);
        }
    }

    public float[] getLatest(){
        return latest;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        latest = event.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO
    }
}
