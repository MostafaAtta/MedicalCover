package com.atta.medicalcover.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.atta.medicalcover.MainActivity;
import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            if (SessionManager.getInstance(this).isLogin()){
                intent = new Intent(SplashScreenActivity.this,
                        MainActivity.class);

            }else {
                intent = new Intent(SplashScreenActivity.this,
                        AuthActivity.class);
            }

            //Intent is used to switch from one activity to another.

            startActivity(intent);
            //invoke the SecondActivity.

            finish();
            //the current activity will get finished.
        }, SPLASH_SCREEN_TIME_OUT);
    }
}