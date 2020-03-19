package com.fii.onlineshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String DARK_MODE_KEY = "dark-mode";
    private static boolean globalIsDarkMode = false;
    private boolean isDarkMode = false;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        globalIsDarkMode = prefs.getBoolean(DARK_MODE_KEY, false);
        onActivityCreateSetTheme();
        preferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(DARK_MODE_KEY)) {
                onDarkModePreferenceSwitch(sharedPreferences.getBoolean(key, false));
            }
        };
    }

    private void onDarkModePreferenceSwitch(boolean isDarkMode) {
        BaseActivity.globalIsDarkMode = isDarkMode;
        finish();
        startActivity(new Intent(this, super.getClass()));
    }

    private void onActivityCreateSetTheme() {
        if (globalIsDarkMode) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemeLight);
        }
        isDarkMode = globalIsDarkMode;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs != null) {
            prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
            if(globalIsDarkMode != isDarkMode) {
                onDarkModePreferenceSwitch(globalIsDarkMode);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (prefs != null) {
            prefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        }
    }
}
