package com.fii.onlineshop.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fii.onlineshop.BaseActivity;
import com.fii.onlineshop.R;

public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.settings_container, new SettingsFragment())
                .commit();
    }
}
