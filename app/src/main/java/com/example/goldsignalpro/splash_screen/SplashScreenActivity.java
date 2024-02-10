package com.example.goldsignalpro.splash_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.goldsignalpro.R;
import com.example.goldsignalpro.home.HomeActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

public class SplashScreenActivity extends AppCompatActivity {

    ShimmerFrameLayout sfl_splash;
    private long SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sfl_splash = findViewById(R.id.sfl_splash);
        sfl_splash.startShimmer();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                startHome();
            }
        }, SPLASH_TIME_OUT);
    }

    private void startHome() {
        sfl_splash.stopShimmer();
        startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
        finish();
    }
}