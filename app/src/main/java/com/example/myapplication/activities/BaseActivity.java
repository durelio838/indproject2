package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;

import android.content.res.Configuration;

import android.os.Bundle;

import android.view.Menu;

import android.view.MenuItem;

import android.content.Intent;

import com.example.myapplication.database.AppDatabase;

import com.example.myapplication.R;

public class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    protected AppDatabase database;

    protected SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ ИСПРАВЛЕНО: Инициализировать preferences ПЕРЕД его использованием
        preferences = getSharedPreferences("app_settings", MODE_PRIVATE);

        // Теперь applyTheme() может безопасно использовать preferences
        applyTheme();

        // Затем инициализировать базу данных
        database = AppDatabase.getInstance(this);
    }

    protected void setupToolbar(String title, boolean showBack) {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
            getSupportActionBar().setDisplayShowHomeEnabled(showBack);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void applyTheme() {
        // ✅ БЕЗОПАСНО: preferences уже инициализирован
        String theme = preferences.getString("theme", "system");

        switch (theme) {
            case "light":
                setTheme(R.style.AppTheme_Light);
                break;
            case "dark":
                setTheme(R.style.AppTheme_Dark);
                break;
            default:
                // system theme - применять в зависимости от системных настроек
                int nightModeFlags = getResources().getConfiguration().uiMode
                        & Configuration.UI_MODE_NIGHT_MASK;
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    setTheme(R.style.AppTheme_Dark);
                } else {
                    setTheme(R.style.AppTheme_Light);
                }
                break;
        }
    }
}