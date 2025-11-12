package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdminColorsAdapter;
import com.example.myapplication.models.Color;

import java.util.ArrayList;
import java.util.List;

public class AdminColorsActivity extends BaseActivity {

    private RecyclerView recyclerViewColors;
    private Button btnAddColor;
    private SearchView searchView;
    private AdminColorsAdapter adapter;
    private List<Color> colorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_colors);
        setupToolbar("Управление цветами", true);
        initViews();
        loadColors();
    }

    private void initViews() {
        recyclerViewColors = findViewById(R.id.recyclerViewColors);
        btnAddColor = findViewById(R.id.btnAddColor);

        recyclerViewColors.setLayoutManager(new LinearLayoutManager(this));

        // ИСПРАВЛЕНО: добавлен обработчик для кнопки добавления цвета
        btnAddColor.setOnClickListener(v -> showAddColorDialog());
    }

    private void showAddColorDialog() {
        ColorPickerDialog dialog = new ColorPickerDialog(this, color -> {
            // Добавление цвета в базу данных
            new Thread(() -> {
                try {
                    database.colorDao().insertColor(color);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Цвет добавлен", Toast.LENGTH_SHORT).show();
                        loadColors(); // Обновляем список
                    });
                } catch (Exception e) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Ошибка добавления: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
        });
        dialog.show();
    }

    private void loadColors() {
        new Thread(() -> {
            try {
                List<Color> colors = database.colorDao().getAllColors();
                colorList = colors != null ? colors : new ArrayList<>();

                runOnUiThread(() -> {
                    if (colorList.isEmpty()) {
                        Toast.makeText(this, "Нет цветов", Toast.LENGTH_SHORT).show();
                    }
                    adapter = new AdminColorsAdapter(colorList, this::deleteColor);
                    recyclerViewColors.setAdapter(adapter);
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void deleteColor(Color color) {
        new Thread(() -> {
            try {
                database.colorDao().deleteColor(color);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Цвет удален", Toast.LENGTH_SHORT).show();
                    loadColors(); // Обновляем список после удаления
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Ошибка удаления: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}
