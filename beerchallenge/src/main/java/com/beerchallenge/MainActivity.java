package com.beerchallenge;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.beerchallenge.fragment.FrontFragment;
import com.beerchallenge.fragment.TimerFragment;


public class MainActivity extends Activity implements View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new FrontFragment());
        ft.commit();
        ImageView button = (ImageView) findViewById(R.id.trigger_button);
        button.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        int actionMasked = event.getActionMasked();
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            final View backGround = findViewById(R.id.background);

            backGround.setBackgroundColor(getResources().getColor(R.color.background_green));

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new TimerFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    if (getFragmentManager().getBackStackEntryCount() == 0) {
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
        }
        return false;
    }
}
