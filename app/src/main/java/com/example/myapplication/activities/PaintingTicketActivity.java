package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.myapplication.R;
import com.example.myapplication.models.Color;
import com.example.myapplication.models.Ticket;

import java.util.List;

public class PaintingTicketActivity extends BaseActivity {
    private TextView tvSelectedColor, tvSelectedDateTime;
    private Button btnSelectColor, btnSelectDate, btnSubmit;
    private Color selectedColor;
    private String selectedDate, selectedTime;

    private ActivityResultLauncher<Intent> bookingLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting_ticket);

        setupToolbar("Заявка на покраску", true);
        initViews();
        setupBookingLauncher();
    }

    private void initViews() {
        tvSelectedColor = findViewById(R.id.tvSelectedColor);
        tvSelectedDateTime = findViewById(R.id.tvSelectedDateTime);
        btnSelectColor = findViewById(R.id.btnSelectColor);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSelectColor.setOnClickListener(v -> showColorPicker());

        btnSelectDate.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookingActivity.class);
            bookingLauncher.launch(intent);
        });

        btnSubmit.setOnClickListener(v -> submitTicket());
    }

    private void setupBookingLauncher() {
        bookingLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedDate = result.getData().getStringExtra("selectedDate");
                        selectedTime = result.getData().getStringExtra("selectedTime");
                        tvSelectedDateTime.setText("Дата и время: " + selectedDate + " " + selectedTime);
                    }
                });
    }

    private void showColorPicker() {
        // Загружаем цвета из базы данных
        new Thread(() -> {
            List<Color> colors = database.colorDao().getAllColors();

            runOnUiThread(() -> {
                if (colors.isEmpty()) {
                    Toast.makeText(this, "Нет доступных цветов", Toast.LENGTH_SHORT).show();
                } else {
                    ColorSelectorDialog dialog = new ColorSelectorDialog(
                            this,
                            colors,
                            color -> {
                                selectedColor = color;
                                tvSelectedColor.setText("Выбран цвет: " + color.getName());
                            }
                    );
                    dialog.show();
                }
            });
        }).start();
    }

    private void submitTicket() {
        if (selectedColor == null) {
            Toast.makeText(this, "Выберите цвет", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate == null || selectedTime == null) {
            Toast.makeText(this, "Выберите дату и время", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Ticket ticket = new Ticket();
            ticket.setType("painting");
            ticket.setColorId(selectedColor.getId());
            ticket.setColorName(selectedColor.getName());
            ticket.setColorCode(selectedColor.getCode());
            ticket.setDate(selectedDate);
            ticket.setTime(selectedTime);
            ticket.setCreatedDate(new java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault()).format(new java.util.Date()));

            // Временные данные пользователя
            ticket.setUserId(1);
            ticket.setUserName("Тестовый пользователь");
            ticket.setUserPhone("+79999999999");

            database.ticketDao().insertTicket(ticket);

            runOnUiThread(() -> {
                Toast.makeText(this, "Заявка создана!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
