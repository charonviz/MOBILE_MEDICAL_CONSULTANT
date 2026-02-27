/* MedScope — Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.charonviz.medscope.R;
import com.charonviz.medscope.util.PrefsManager;

/**
 * Premium splash screen with MedScope caduceus logo.
 * Shows the brand for 2 seconds with a fade-in animation,
 * then transitions smoothly to the main screen.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(new PrefsManager(this).getThemeMode());
        setContentView(R.layout.activity_splash);

        // Fade-in animation for logo and text
        ImageView logo = findViewById(R.id.iv_logo);
        TextView title = findViewById(R.id.tv_splash_title);
        TextView subtitle = findViewById(R.id.tv_splash_subtitle);

        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(1200);
        fadeIn.setFillAfter(true);
        logo.startAnimation(fadeIn);

        AlphaAnimation fadeInDelayed = new AlphaAnimation(0f, 1f);
        fadeInDelayed.setDuration(800);
        fadeInDelayed.setStartOffset(600);
        fadeInDelayed.setFillAfter(true);
        title.startAnimation(fadeInDelayed);
        subtitle.startAnimation(fadeInDelayed);

        // Navigate to main after 2.5 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 2500);
    }
}
