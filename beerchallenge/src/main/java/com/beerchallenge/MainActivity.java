package com.beerchallenge;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beerchallenge.fragment.FrontFragment;
import com.beerchallenge.fragment.TimerFragment;
import com.beerchallenge.sensor.AccelerationMeter;
import com.beerchallenge.sensor.SoundMeter;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity implements View.OnTouchListener {

    public static final String TAG = "MainActivity";
    private Handler timerHandler = new Handler();
    private Stopwatch stopwatch;
    private SensorManager sensorManager;
    private AccelerationMeter accelerationMeter;
    private SoundMeter soundMeter;

    private Runnable timerUpdater = new Runnable() {
        @Override
        public void run() {
            long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

            int seconds = (int) (elapsed / 1000);
            int tenths = (int) (elapsed / 100) % 10;

            TextView secs = (TextView) findViewById(R.id.timer_secs);
            TextView ts = (TextView) findViewById(R.id.timer_tenths);

            secs.setText(Integer.toString(seconds));
            ts.setText(Integer.toString(tenths));

            if (smashDetected(accelerationMeter.getLatest(), soundMeter.getAmplitude())) {
                Log.d(TAG, "Done...");
                // TODO switch fragment to name input
            } else {
                timerHandler.postDelayed(this, 0);
            }

        }
    };

    private boolean smashDetected(float[] latestAccelerationData, double latestAmplitude) {
        if (Math.max(Math.abs(latestAccelerationData[1]), Math.abs(latestAccelerationData[2])) > 0.35 && latestAmplitude > 12) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new FrontFragment());
        ft.commit();
        ImageView button = (ImageView) findViewById(R.id.trigger_button);
        button.setOnTouchListener(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerationMeter = new AccelerationMeter(sensorManager);
        soundMeter = new SoundMeter();

    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerUpdater);
        soundMeter.stop();
        accelerationMeter.stop(sensorManager);

    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        int actionMasked = event.getActionMasked();
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            final View backGround = findViewById(R.id.background);
            backGround.setBackgroundColor(getResources().getColor(R.color.background_green));
            switchToTimerFragment();

            getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    if (getFragmentManager().getBackStackEntryCount() == 0) {
                        timerHandler.removeCallbacks(timerUpdater);
                        backGround.setBackgroundColor(getResources().getColor(R.color.background_red));
                        v.setOnTouchListener(MainActivity.this);
                        v.setBackground(getResources().getDrawable(R.drawable.selector_trigger_button));
                    }
                }
            });

        } else if (actionMasked == MotionEvent.ACTION_UP) {
            Log.d("MainActivity", "Start the clock!");

            v.setOnTouchListener(null);
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.slam_to_stop));
            TextView cheerText = (TextView) findViewById(R.id.cheer_text);
            cheerText.setText("GO!");

            stopwatch = Stopwatch.createStarted();
            timerHandler.postDelayed(timerUpdater, 0);
            soundMeter.start();
            accelerationMeter.start(sensorManager);
        }

        return false;
    }

    private void switchToTimerFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new TimerFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
