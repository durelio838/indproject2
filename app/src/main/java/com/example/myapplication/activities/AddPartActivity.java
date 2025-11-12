package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Part;

public class AddPartActivity extends BaseActivity {

    private EditText etCarModel, etPartName, etPartPrice;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_part);
        setupToolbar("Добавить запчасть", true);
        initViews();
    }

    private void initViews() {
        etCarModel = findViewById(R.id.etCarModel);
        etPartName = findViewById(R.id.etPartName);
        etPartPrice = findViewById(R.id.etPartPrice);
        btnSave = findViewById(R.id.btnSavePart);

        btnSave.setOnClickListener(v -> savePart());
    }

    private void savePart() {
        String carModel = etCarModel.getText().toString().trim();
        String name = etPartName.getText().toString().trim();
        String price = etPartPrice.getText().toString().trim();

        if (carModel.isEmpty() || name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        Part part = new Part(carModel, name, price);

        new Thread(() -> {
            try {
                database.partDao().insertPart(part);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Запчасть добавлена!", Toast.LENGTH_SHORT).show();
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
