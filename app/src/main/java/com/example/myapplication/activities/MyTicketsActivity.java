package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.MyTicketsAdapter;
import com.example.myapplication.models.Question;
import com.example.myapplication.models.Ticket;

import java.util.ArrayList;
import java.util.List;

public class MyTicketsActivity extends BaseActivity {

    private RecyclerView recyclerViewTickets;
    private MyTicketsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);
        setupToolbar("Мои заявки", true);
        initViews();
        loadTickets();
    }

    private void initViews() {
        recyclerViewTickets = findViewById(R.id.recyclerViewTickets);

        // Null-safe проверка
        if (recyclerViewTickets == null) {
            Toast.makeText(this, "Ошибка загрузки интерфейса", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerViewTickets.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadTickets() {
        new Thread(() -> {
            // Для демо используем userId = 1
            int userId = 1;

            try {
                List<Ticket> tickets = database.ticketDao().getTicketsByUserId(userId);
                List<Question> questions = database.questionDao().getQuestionsByUserId(userId);

                List<Object> allItems = new ArrayList<>();
                if (tickets != null) {
                    allItems.addAll(tickets);
                }
                if (questions != null) {
                    allItems.addAll(questions);
                }

                runOnUiThread(() -> {
                    if (allItems.isEmpty()) {
                        Toast.makeText(this, "У вас пока нет заявок", Toast.LENGTH_SHORT).show();
                    }

                    adapter = new MyTicketsAdapter(allItems);
                    recyclerViewTickets.setAdapter(adapter);
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Ошибка загрузки данных: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}