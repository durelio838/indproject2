package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Color;

public class AddColorActivity extends BaseActivity {

    private EditText etColorCode, etColorName, etColorPrice;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_color);
        setupToolbar("Добавить цвет", true);
        initViews();
    }

    private void initViews() {
        etColorCode = findViewById(R.id.etColorCode);
        etColorName = findViewById(R.id.etColorName);
        etColorPrice = findViewById(R.id.etColorPrice);
        btnSave = findViewById(R.id.btnSaveColor);

        btnSave.setOnClickListener(v -> saveColor());
    }

    private void saveColor() {
        String code = etColorCode.getText().toString().trim();
        String name = etColorName.getText().toString().trim();
        String price = etColorPrice.getText().toString().trim();

        if (code.isEmpty() || name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        Color color = new Color(code, name, price);

        new Thread(() -> {
            try {
                database.colorDao().insertColor(color);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Цвет добавлен!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}
