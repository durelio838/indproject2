package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.R;

public class TicketTypeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_type);

        setupToolbar("Создание заявки", true);
        initViews();
    }

    private void initViews() {
        Button btnRepair = findViewById(R.id.btnRepair);
        Button btnPainting = findViewById(R.id.btnPainting);
        Button btnParts = findViewById(R.id.btnParts);

        btnRepair.setOnClickListener(v ->
                startActivity(new Intent(this, RepairTicketActivity.class)));

        btnPainting.setOnClickListener(v ->
                startActivity(new Intent(this, PaintingTicketActivity.class)));

        btnParts.setOnClickListener(v ->
                startActivity(new Intent(this, PartsActivity.class)));
    }
}
