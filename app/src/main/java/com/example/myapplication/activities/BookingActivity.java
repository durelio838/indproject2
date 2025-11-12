package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.CalendarAdapter;
import com.example.myapplication.adapters.TimeSlotAdapter;
import com.example.myapplication.models.Ticket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingActivity extends BaseActivity {
    private RecyclerView recyclerViewCalendar, recyclerViewTime;
    private TextView tvSelectedDate;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        setupToolbar("Выбор даты", true);
        initViews();
        setupCalendar();
    }

    private void initViews() {
        recyclerViewCalendar = findViewById(R.id.recyclerViewCalendar);
        recyclerViewTime = findViewById(R.id.recyclerViewTime);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);

        recyclerViewCalendar.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTime.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void setupCalendar() {
        List<Calendar> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 3; i++) {
            months.add((Calendar) calendar.clone());
            calendar.add(Calendar.MONTH, 1);
        }

        CalendarAdapter adapter = new CalendarAdapter(months, date -> {
            selectedDate = date;
            tvSelectedDate.setText("Выбранная дата: " + date);
            setupTimeSlots();
        });
        recyclerViewCalendar.setAdapter(adapter);
    }

    private void setupTimeSlots() {
        String[] allTimeSlots = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
                "14:00", "15:00", "16:00", "17:00", "18:00", "19:00",
                "20:00", "21:00", "22:00"};

        new Thread(() -> {
            // Получаем все заявки на выбранную дату
            List<Ticket> bookedTickets = database.ticketDao().getTicketsByDate(selectedDate);

            // Создаем список занятых времен
            List<String> bookedTimes = new ArrayList<>();
            if (bookedTickets != null) {
                for (Ticket ticket : bookedTickets) {
                    String ticketTime = ticket.getTime();
                    if (ticketTime != null && !ticketTime.isEmpty()) {
                        bookedTimes.add(ticketTime);
                    }
                }
            }

            // Фильтруем доступные слоты
            List<String> availableSlots = new ArrayList<>();
            for (String time : allTimeSlots) {
                if (!bookedTimes.contains(time)) {
                    availableSlots.add(time);
                }
            }

            runOnUiThread(() -> {
                if (availableSlots.isEmpty()) {
                    // Если нет свободных слотов, показываем сообщение
                    tvSelectedDate.setText("На " + selectedDate + " все время занято");
                } else {
                    TimeSlotAdapter adapter = new TimeSlotAdapter(availableSlots, time -> {
                        Intent result = new Intent();
                        result.putExtra("selectedDate", selectedDate);
                        result.putExtra("selectedTime", time);
                        setResult(RESULT_OK, result);
                        finish();
                    });
                    recyclerViewTime.setAdapter(adapter);
                }
            });
        }).start();
    }
}
