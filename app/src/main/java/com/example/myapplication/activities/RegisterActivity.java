package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.User;

public class RegisterActivity extends BaseActivity {

    private EditText etName, etPhone, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupToolbar("Регистрация", false);
        initViews();
        setupInputFilters();
        setupPhoneFormatting();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> attemptRegistration());
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void setupInputFilters() {
        // Name - только русские буквы, до 12 символов
        etName.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(12),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char c = source.charAt(i);
                        // Русские и английские буквы
                        if (!Character.isLetter(c) ||
                                (c >= '0' && c <= '9') ||
                                (c >= '!' && c <= '/') ||
                                (c >= ':' && c <= '@')) {
                            return "";
                        }
                    }
                    return null;
                }
        });

        // Password - могут быть большие буквы, цифры и символы
        etPassword.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(14)
        });

        etConfirmPassword.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(14)
        });
    }

    private void setupPhoneFormatting() {
        etPhone.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                String input = s.toString().replaceAll("[^0-9]", "");

                // Ограничиваем только цифры
                if (input.length() > 13) {
                    input = input.substring(0, 13);
                }

                if (input.length() == 0) {
                    isUpdating = true;
                    etPhone.setText("");
                    isUpdating = false;
                    return;
                }

                // Форматируем: + XXXXXXXXXXX (от 11 до 13 цифр)
                String formatted;
                if (input.length() <= 13) {
                    formatted = "+ " + input;
                } else {
                    formatted = "+ " + input.substring(0, 13);
                }

                if (!formatted.equals(s.toString())) {
                    isUpdating = true;
                    etPhone.setText(formatted);
                    etPhone.setSelection(formatted.length());
                    isUpdating = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void attemptRegistration() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (validateInputs(name, phone, password, confirmPassword)) {
            new Thread(() -> {
                if (database.userDao().getUserByName(name) == null) {
                    User user = new User(name, phone, password);
                    database.userDao().insertUser(user);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MenuActivity.class));
                        finish();
                    });
                } else {
                    runOnUiThread(() ->
                            etName.setError("Это имя уже занято"));
                }
            }).start();
        }
    }

    private boolean validateInputs(String name, String phone, String password, String confirmPassword) {
        boolean isValid = true;

        // Validate Name - только буквы, до 12 символов
        if (name.isEmpty() || name.length() > 12) {
            etName.setError("Имя должно быть до 12 символов");
            isValid = false;
        } else if (!name.matches("[а-яА-ЯёЁa-zA-Z]+")) {
            etName.setError("Имя должно содержать только буквы");
            isValid = false;
        }

        // Validate Phone - только цифры (11-13), без +  и пробелов для проверки
        String cleanPhone = phone.replaceAll("[^0-9]", "");
        if (cleanPhone.length() < 11 || cleanPhone.length() > 13) {
            etPhone.setError("Номер должен содержать 11-13 цифр");
            isValid = false;
        }

        // Validate Password - минимум 8, максимум 14, 1 заглавная буква, 1 символ
        if (password.length() < 8 || password.length() > 14) {
            etPassword.setError("Пароль: 8-14 символов");
            isValid = false;
        } else if (!password.matches(".*[A-ZА-ЯЁ].*")) {
            etPassword.setError("Пароль должен содержать заглавную букву");
            isValid = false;
        } else if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};:'\"|,.<>?/\\\\].*")) {
            etPassword.setError("Пароль должен содержать символ");
            isValid = false;
        }

        // Validate Confirm Password
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Пароли не совпадают");
            isValid = false;
        }

        return isValid;
    }
}
