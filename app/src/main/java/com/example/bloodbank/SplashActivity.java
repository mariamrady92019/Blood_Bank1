package com.example.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodbank.activities.MainActivity;

/**
 * Created By Mohamed El Banna On 6/27/2020
 **/
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(this::setUp, SPLASH_TIME_OUT);
    }

    private void setUp() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
