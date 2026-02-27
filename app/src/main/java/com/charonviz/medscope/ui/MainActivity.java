/* Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.charonviz.medscope.R;
import com.charonviz.medscope.util.PrefsManager;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(new PrefsManager(this).getThemeMode());
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        // Feature cards
        ((MaterialCardView) findViewById(R.id.card_diagnosis))
            .setOnClickListener(v -> startActivity(new Intent(this, DiagnosisActivity.class)));
        ((MaterialCardView) findViewById(R.id.card_medicine))
            .setOnClickListener(v -> startActivity(new Intent(this, MedicineActivity.class)));
        ((MaterialCardView) findViewById(R.id.card_ai_chat))
            .setOnClickListener(v -> startActivity(new Intent(this, AiChatActivity.class)));
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
