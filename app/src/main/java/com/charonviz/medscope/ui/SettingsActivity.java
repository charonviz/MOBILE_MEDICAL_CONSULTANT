/* Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.charonviz.medscope.R;
import com.charonviz.medscope.util.PrefsManager;
import com.google.android.material.button.MaterialButton;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        PrefsManager prefs = new PrefsManager(this);
        EditText etKey = findViewById(R.id.et_api_key);
        etKey.setText(prefs.getApiKey());

        RadioGroup rg = findViewById(R.id.rg_theme);
        switch (prefs.getThemeMode()) {
            case AppCompatDelegate.MODE_NIGHT_NO:  rg.check(R.id.rb_light); break;
            case AppCompatDelegate.MODE_NIGHT_YES: rg.check(R.id.rb_dark); break;
            default: rg.check(R.id.rb_system);
        }

        ((MaterialButton) findViewById(R.id.btn_save)).setOnClickListener(v -> {
            prefs.setApiKey(etKey.getText().toString().trim());
            int checked = rg.getCheckedRadioButtonId();
            int mode = checked == R.id.rb_light ? AppCompatDelegate.MODE_NIGHT_NO
                     : checked == R.id.rb_dark  ? AppCompatDelegate.MODE_NIGHT_YES
                     : AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            prefs.setThemeMode(mode);
            AppCompatDelegate.setDefaultNightMode(mode);
            finish();
        });
    }
    @Override public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
