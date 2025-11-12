package com.example.myapplication.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdminPartsAdapter;
import com.example.myapplication.models.Part;

import java.util.List;

public class AdminPartsActivity extends BaseActivity {
    private RecyclerView recyclerViewParts;
    private Button btnAddPart;
    private AdminPartsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_parts);

        setupToolbar("Управление запчастями", true);
        initViews();
        loadParts();
    }

    private void initViews() {
        recyclerViewParts = findViewById(R.id.recyclerViewParts);
        btnAddPart = findViewById(R.id.btnAddPart);

        recyclerViewParts.setLayoutManager(new LinearLayoutManager(this));

        btnAddPart.setOnClickListener(v -> showAddPartDialog());
    }

    private void loadParts() {
        new Thread(() -> {
            List<Part> parts = database.partDao().getAllParts();
            runOnUiThread(() -> {
                adapter = new AdminPartsAdapter(parts, this::showDeleteConfirmation);
                recyclerViewParts.setAdapter(adapter);
            });
        }).start();
    }

    private void showAddPartDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_part, null);
        dialogBuilder.setView(dialogView);

        EditText etCarModel = dialogView.findViewById(R.id.etCarModel);
        EditText etPartName = dialogView.findViewById(R.id.etPartName);
        EditText etPrice = dialogView.findViewById(R.id.etPartPrice);

        dialogBuilder.setTitle("Добавить запчасть");
        dialogBuilder.setPositiveButton("Сохранить", (dialog, which) -> {
            String carModel = etCarModel.getText().toString().trim();
            String partName = etPartName.getText().toString().trim();
            String price = etPrice.getText().toString().trim();

            if (carModel.isEmpty() || partName.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            addPart(carModel, partName, price);
        });
        dialogBuilder.setNegativeButton("Отмена", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void addPart(String carModel, String partName, String price) {
        new Thread(() -> {
            Part part = new Part(carModel, partName, price);
            database.partDao().insertPart(part);

            runOnUiThread(() -> {
                Toast.makeText(this, "Запчасть добавлена", Toast.LENGTH_SHORT).show();
                loadParts();
            });
        }).start();
    }

    private void showDeleteConfirmation(Part part) {
        new AlertDialog.Builder(this)
                .setTitle("Удаление")
                .setMessage("Удалить запчасть " + part.getName() + "?")
                .setPositiveButton("Удалить", (dialog, which) -> deletePart(part))
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void deletePart(Part part) {
        new Thread(() -> {
            database.partDao().deletePart(part);
            runOnUiThread(() -> {
                Toast.makeText(this, "Запчасть удалена", Toast.LENGTH_SHORT).show();
                loadParts();
            });
        }).start();
    }
}