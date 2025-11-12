package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.R;

public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setupToolbar("Главное меню", true);
        initViews();
    }

    private void initViews() {
        Button btnCreateTicket = findViewById(R.id.btnCreateTicket);
        Button btnAskQuestion = findViewById(R.id.btnAskQuestion);
        Button btnMyTickets = findViewById(R.id.btnMyTickets);

        btnCreateTicket.setOnClickListener(v ->
                startActivity(new Intent(this, TicketTypeActivity.class)));

        btnAskQuestion.setOnClickListener(v ->
                startActivity(new Intent(this, QuestionActivity.class)));

        btnMyTickets.setOnClickListener(v ->
                startActivity(new Intent(this, MyTicketsActivity.class)));
    }
}