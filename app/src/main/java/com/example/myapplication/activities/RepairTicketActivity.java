package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Ticket;

public class RepairTicketActivity extends BaseActivity {
    private EditText etCarModel, etProblemDescription;
    private Button btnBooking, btnCreateTicket;
    private String selectedDate, selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_ticket);

        setupToolbar("Заявка на ремонт", true);
        initViews();
    }

    private void initViews() {
        etCarModel = findViewById(R.id.etCarModel);
        etProblemDescription = findViewById(R.id.etProblemDescription);
        btnBooking = findViewById(R.id.btnBooking);
        btnCreateTicket = findViewById(R.id.btnCreateTicket);

        btnBooking.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookingActivity.class);
            startActivityForResult(intent, 1);
        });

        btnCreateTicket.setOnClickListener(v -> createTicket());
    }

    private void createTicket() {
        String carModel = etCarModel.getText().toString().trim();
        String description = etProblemDescription.getText().toString().trim();

        if (carModel.isEmpty() || carModel.length() > 15) {
            etCarModel.setError("Модель автомобиля должна быть от 1 до 15 символов");
            return;
        }

        if (description.isEmpty() || description.length() > 200) {
            etProblemDescription.setError("Описание проблемы должно быть от 1 до 200 символов");
            return;
        }

        if (selectedDate == null || selectedTime == null) {
            Toast.makeText(this, "Выберите дату и время записи", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Ticket ticket = new Ticket();
            ticket.setType("repair");
            ticket.setCarModel(carModel);
            ticket.setDescription(description);
            ticket.setBookingDate(selectedDate);
            ticket.setBookingTime(selectedTime);
            ticket.setCreatedDate(new java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault()).format(new java.util.Date()));

            // Здесь нужно установить userId, userName, userPhone из текущего пользователя
            // Для демо используем временные данные
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