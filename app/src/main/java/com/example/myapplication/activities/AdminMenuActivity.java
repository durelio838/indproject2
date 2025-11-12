package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import com.example.myapplication.R;

public class AdminMenuActivity extends BaseActivity {

    private MaterialButton btnTickets, btnProfiles, btnParts, btnColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        setupToolbar("Меню администратора", true);
        initViews();
    }

    private void initViews() {
        btnTickets = findViewById(R.id.btnTickets);
        btnProfiles = findViewById(R.id.btnProfiles);
        btnParts = findViewById(R.id.btnParts);
        btnColors = findViewById(R.id.btnColors);

        btnTickets.setOnClickListener(v ->
                startActivity(new Intent(this, AdminTicketsActivity.class))
        );

        btnProfiles.setOnClickListener(v ->
                startActivity(new Intent(this, AdminProfilesActivity.class))
        );

        btnParts.setOnClickListener(v ->
                startActivity(new Intent(this, AdminPartsActivity.class))
        );

        btnColors.setOnClickListener(v ->
                startActivity(new Intent(this, AdminColorsActivity.class))
        );
    }

    @Override
    public void onBackPressed() {
        // Переход на экран регистрации вместо выхода из приложения
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
