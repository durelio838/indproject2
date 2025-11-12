package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.myapplication.R;
import com.example.myapplication.models.Ticket;

public class RepairTicketActivity extends BaseActivity {
    private EditText etDescription;
    private TextView tvSelectedDateTime;
    private Button btnSelectDate, btnSubmit;
    private String selectedDate, selectedTime;

    private ActivityResultLauncher<Intent> bookingLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_ticket);

        setupToolbar("Заявка на ремонт", true);
        initViews();
        setupBookingLauncher();
    }

    private void initViews() {
        etDescription = findViewById(R.id.etDescription);
        tvSelectedDateTime = findViewById(R.id.tvSelectedDateTime);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSubmit = findViewById(R.id.btnSubmit);

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

    private void submitTicket() {
        String description = etDescription.getText().toString().trim();

        if (description.isEmpty()) {
            etDescription.setError("Опишите проблему");
            return;
        }

        if (selectedDate == null || selectedTime == null) {
            Toast.makeText(this, "Выберите дату и время", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Ticket ticket = new Ticket();
            ticket.setType("repair");
            ticket.setDescription(description);
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
