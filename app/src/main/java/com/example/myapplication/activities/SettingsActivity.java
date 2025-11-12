package com.example.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.User;

public class SettingsActivity extends BaseActivity {
    private RadioGroup radioGroupTheme;
    private EditText etOldPassword, etNewPassword;
    private Button btnSavePassword;
    private boolean showPasswordChange = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupToolbar("Настройки", true);

        // Проверяем, откуда открыты настройки
        if (getIntent().hasExtra("fromLogin")) {
            showPasswordChange = !getIntent().getBooleanExtra("fromLogin", false);
        }

        initViews();
        loadCurrentTheme();
    }

    private void initViews() {
        radioGroupTheme = findViewById(R.id.radioGroupTheme);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnSavePassword = findViewById(R.id.btnSavePassword);

        if (!showPasswordChange) {
            findViewById(R.id.passwordSection).setVisibility(View.GONE);
        }

        radioGroupTheme.setOnCheckedChangeListener((group, checkedId) -> {
            String theme;
            if (checkedId == R.id.radioSystem) {
                theme = "system";
            } else if (checkedId == R.id.radioLight) {
                theme = "light";
            } else {
                theme = "dark";
            }
            saveTheme(theme);
        });

        btnSavePassword.setOnClickListener(v -> changePassword());
    }

    private void loadCurrentTheme() {
        String currentTheme = preferences.getString("theme", "system");
        switch (currentTheme) {
            case "system":
                radioGroupTheme.check(R.id.radioSystem);
                break;
            case "light":
                radioGroupTheme.check(R.id.radioLight);
                break;
            case "dark":
                radioGroupTheme.check(R.id.radioDark);
                break;
        }
    }

    private void saveTheme(String theme) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("theme", theme);
        editor.apply();

        // Перезапускаем активность для применения темы
        recreate();
    }

    private void changePassword() {
        String oldPassword = etOldPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();

        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // В реальном приложении здесь должна быть проверка текущего пользователя
        // Для демо используем временную логику

        if (!validatePassword(newPassword)) {
            etNewPassword.setError("Пароль должен содержать минимум 8 символов, 1 заглавную букву и 1 символ");
            return;
        }

        // Здесь должен быть код обновления пароля в базе данных
        // Для демо просто показываем сообщение
        Toast.makeText(this, "Пароль изменен", Toast.LENGTH_SHORT).show();

        // После смены пароля переходим на экран входа
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private boolean validatePassword(String password) {
        boolean hasUpperCase = !password.equals(password.toLowerCase());
        boolean hasSymbol = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        return password.length() >= 8 && password.length() <= 14 && hasUpperCase && hasSymbol;
    }
}