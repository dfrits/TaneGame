package com.daniel.tanegame;

import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daniel.tanegame.animation.AirAnimationFragment;
import com.daniel.tanegame.animation.AnimationFragment;
import com.daniel.tanegame.animation.EarthAnimationFragment;
import com.daniel.tanegame.animation.FireAnimationFragment;
import com.daniel.tanegame.animation.WaterAnimationFragment;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private AnimationFragment fragment;
    private ImageView airButton;
    private ImageView waterButton;
    private ImageView fireButton;
    private ImageView earthButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_main);
        fireButton = findViewById(R.id.bFire);
        waterButton = findViewById(R.id.bWater);
        airButton = findViewById(R.id.bAir);
        earthButton = findViewById(R.id.bEarth);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        resetButtons();
    }

    public void resetButtons() {
        if (airButton != null) {
            airButton.setImageResource(R.mipmap.button_air);
        }
        if (earthButton != null) {
            earthButton.setImageResource(R.mipmap.button_earth);
        }
        if (fireButton != null) {
            fireButton.setImageResource(R.mipmap.button_fire);
        }
        if (waterButton != null) {
            waterButton.setImageResource(R.mipmap.button_water);
        }
    }

    public void dropAirElement(View view) {
        fragment = new AirAnimationFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.animationPanel, fragment);
        ft.commit();

    }

    public void setAirButton() {
        airButton.setImageResource(R.mipmap.button_air_pressed);
        fireButton.setImageResource(R.mipmap.button_fire);
        waterButton.setImageResource(R.mipmap.button_water);
        earthButton.setImageResource(R.mipmap.button_earth);
    }

    public void dropEarthElement(View view) {
        fragment = new EarthAnimationFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.animationPanel, fragment);
        ft.commit();

    }

    public void setEarthButton() {
        earthButton.setImageResource(R.mipmap.button_earth_pressed);
        airButton.setImageResource(R.mipmap.button_air);
        fireButton.setImageResource(R.mipmap.button_fire);
        waterButton.setImageResource(R.mipmap.button_water);
    }

    public void dropFireElement(View view) {
        fragment = new FireAnimationFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.animationPanel, fragment);
        ft.commit();

    }

    public void setFireButton() {
        fireButton.setImageResource(R.mipmap.button_fire_pressed);
        waterButton.setImageResource(R.mipmap.button_water);
        airButton.setImageResource(R.mipmap.button_air);
        earthButton.setImageResource(R.mipmap.button_earth);
    }

    public void dropWaterElement(View view) {
        fragment = new WaterAnimationFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.animationPanel, fragment);
        ft.commit();

    }

    public void setWaterButton() {
        waterButton.setImageResource(R.mipmap.button_water_pressed);
        fireButton.setImageResource(R.mipmap.button_fire);
        airButton.setImageResource(R.mipmap.button_air);
        earthButton.setImageResource(R.mipmap.button_earth);
    }
}
