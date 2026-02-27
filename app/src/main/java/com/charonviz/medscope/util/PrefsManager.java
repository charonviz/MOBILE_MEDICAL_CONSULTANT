/* Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

/** Persists user settings: API key, theme preference. */
public class PrefsManager {
    private static final String PREFS = "medscope_prefs";
    private final SharedPreferences sp;

    public PrefsManager(Context c) {
        sp = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public String getApiKey() { return sp.getString("api_key", ""); }
    public void setApiKey(String k) { sp.edit().putString("api_key", k).apply(); }

    public int getThemeMode() {
        return sp.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
    public void setThemeMode(int m) { sp.edit().putInt("theme", m).apply(); }
}
