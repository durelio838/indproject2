package com.example.myapplication.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PartsAdapter;
import com.example.myapplication.models.Part;
import com.example.myapplication.models.Ticket;

import java.util.ArrayList;
import java.util.List;

public class PartsActivity extends BaseActivity {
    private RecyclerView recyclerViewParts;
    private EditText etSearch;
    private PartsAdapter adapter;
    private List<Part> allParts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts);

        setupToolbar("Покупка запчастей", true);
        initViews();
        loadParts();
    }

    private void initViews() {
        recyclerViewParts = findViewById(R.id.recyclerViewParts);
        etSearch = findViewById(R.id.etSearch);

        recyclerViewParts.setLayoutManager(new LinearLayoutManager(this));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterParts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadParts() {
        new Thread(() -> {
            allParts = database.partDao().getAllParts();
            if (allParts.isEmpty()) {
                // Добавляем демо данные
                allParts.add(new Part("Toyota", "Масло моторное", "800"));
                allParts.add(new Part("BMW", "Фильтр воздушный", "1200"));
                allParts.add(new Part("Mercedes", "Свечи зажигания", "600"));
                allParts.add(new Part("Audi", "Тормозные колодки", "1500"));

                for (Part part : allParts) {
                    database.partDao().insertPart(part);
                }
            }

            runOnUiThread(() -> {
                adapter = new PartsAdapter(allParts, this::showConfirmationDialog);
                recyclerViewParts.setAdapter(adapter);
            });
        }).start();
    }

    private void filterParts(String search) {
        if (adapter != null) {
            adapter.filter(search);
        }
    }

    private void showConfirmationDialog(Part part) {
        new AlertDialog.Builder(this)
                .setTitle("Вы уверены?")
                .setMessage("Вы хотите заказать запчасть: " + part.getName())
                .setPositiveButton("Да", (dialog, which) -> orderPart(part))
                .setNegativeButton("Нет", null)
                .show();
    }

    private void orderPart(Part part) {
        new Thread(() -> {
            Ticket ticket = new Ticket();
            ticket.setType("parts");
            ticket.setPartId(part.getId());
            ticket.setPartName(part.getName());
            ticket.setCreatedDate(new java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault()).format(new java.util.Date()));

            // Временные данные пользователя
            ticket.setUserId(1);
            ticket.setUserName("Тестовый пользователь");
            ticket.setUserPhone("+79999999999");

            database.ticketDao().insertTicket(ticket);

            runOnUiThread(() -> {
                Toast.makeText(this, "Запчасть заказана успешно!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}