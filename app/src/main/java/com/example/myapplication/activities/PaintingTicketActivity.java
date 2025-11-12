package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Color;
import com.example.myapplication.models.Ticket;

import java.util.List;

public class PaintingTicketActivity extends BaseActivity {
    private EditText etCarModel;
    private Button btnColor, btnBooking, btnCreateTicket;
    private String selectedDate, selectedTime;
    private Color selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting_ticket);

        setupToolbar("Заявка на покраску", true);
        initViews();
    }

    private void initViews() {
        etCarModel = findViewById(R.id.etCarModel);
        btnColor = findViewById(R.id.btnColor);
        btnBooking = findViewById(R.id.btnBooking);
        btnCreateTicket = findViewById(R.id.btnCreateTicket);

        btnColor.setOnClickListener(v -> showColorPicker());
        btnBooking.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookingActivity.class);
            startActivityForResult(intent, 1);
        });
        btnCreateTicket.setOnClickListener(v -> createTicket());
    }

    private void showColorPicker() {
        new Thread(() -> {
            List<Color> colors = database.colorDao().getAllColors();
            runOnUiThread(() -> {
                ColorPickerDialog dialog = new ColorPickerDialog(this, colors, color -> {
                    selectedColor = color;
                    btnColor.setText(color.getName());
                });
                dialog.show();
            });
        }).start();
    }

    private void createTicket() {
        String carModel = etCarModel.getText().toString().trim();

        if (carModel.isEmpty() || carModel.length() > 15) {
            etCarModel.setError("Модель автомобиля должна быть от 1 до 15 символов");
            return;
        }

        if (selectedColor == null) {
            Toast.makeText(this, "Выберите цвет покраски", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate == null || selectedTime == null) {
            Toast.makeText(this, "Выберите дату и время записи", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Ticket ticket = new Ticket();
            ticket.setType("painting");
            ticket.setCarModel(carModel);
            ticket.setColor(selectedColor.getName());
            ticket.setColorCode(selectedColor.getCode());
            ticket.setBookingDate(selectedDate);
            ticket.setBookingTime(selectedTime);
            ticket.setCreatedDate(new java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault()).format(new java.util.Date()));

            // Временные данные пользователя
            ticket.setUserId(1);
            ticket.setUserName("Тестовый пользователь");
            ticket.setUserPhone("+79999999999");

            database.ticketDao().insertTicket(ticket);

            runOnUiThread(() -> {
                Toast.makeText(this, "Заявка создана успешно!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MenuActivity.class));
                finish();
            });
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selectedDate = data.getStringExtra("selectedDate");
            selectedTime = data.getStringExtra("selectedTime");
            btnBooking.setText(selectedDate + " в " + selectedTime);
        }
    }
}