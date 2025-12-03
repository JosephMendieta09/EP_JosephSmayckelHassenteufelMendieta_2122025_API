package com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView logo = findViewById(R.id.splashLogo);
        TextView appName = findViewById(R.id.appName);

        // Animaciones
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        logo.startAnimation(fadeIn);
        appName.startAnimation(fadeIn);

        // Navegar al menú después del splash
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}