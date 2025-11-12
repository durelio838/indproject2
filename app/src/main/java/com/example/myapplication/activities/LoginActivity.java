package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.User;

public class LoginActivity extends BaseActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupToolbar("Вход", false);
        initViews();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> attemptLogin());
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Admin check
        if (username.equals("admin") && password.equals("admin")) {
            startActivity(new Intent(this, AdminMenuActivity.class));
            finish();
            return;
        }

        new Thread(() -> {
            // Удаляем пробелы и + из номера для проверки в БД
            String cleanUsername = username.replaceAll("[\\s+]", "");

            // Проверяем по имени и по номеру
            User user = database.userDao().loginUser(username, cleanUsername, password);

            runOnUiThread(() -> {
                if (user != null) {
                    if (user.isBlocked()) {
                        Toast.makeText(this, "Ваш профиль заблокирован", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Вход успешен!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MenuActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "Неверные данные", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
