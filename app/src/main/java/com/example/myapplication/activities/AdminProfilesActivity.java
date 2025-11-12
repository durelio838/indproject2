package com.example.myapplication.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdminProfilesAdapter;
import com.example.myapplication.models.Question;
import com.example.myapplication.models.Ticket;
import com.example.myapplication.models.User;

import java.util.List;

public class AdminProfilesActivity extends BaseActivity {
    private RecyclerView recyclerViewProfiles;
    private EditText etSearch;
    private AdminProfilesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profiles);

        setupToolbar("Профили", true);
        initViews();
        loadProfiles();
    }

    private void initViews() {
        recyclerViewProfiles = findViewById(R.id.recyclerViewProfiles);
        etSearch = findViewById(R.id.etSearch);

        recyclerViewProfiles.setLayoutManager(new LinearLayoutManager(this));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProfiles(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadProfiles() {
        new Thread(() -> {
            List<User> users = database.userDao().getAllUsers();

            // Подсчитываем количество заявок для каждого пользователя
            for (User user : users) {
                List<Ticket> tickets = database.ticketDao().getTicketsByUserId(user.getId());
                List<Question> questions = database.questionDao().getQuestionsByUserId(user.getId());
                user.setTicketCount(tickets.size() + questions.size());
            }

            runOnUiThread(() -> {
                adapter = new AdminProfilesAdapter(users, this::blockUser);
                recyclerViewProfiles.setAdapter(adapter);
            });
        }).start();
    }

    private void filterProfiles(String search) {
        if (adapter != null) {
            adapter.filter(search);
        }
    }

    private void blockUser(int userId) {
        new Thread(() -> {
            User user = database.userDao().getUserById(userId);
            if (user != null) {
                user.setBlocked(true);
                database.userDao().updateUser(user);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Пользователь заблокирован", Toast.LENGTH_SHORT).show();
                    loadProfiles(); // Обновляем список
                });
            }
        }).start();
    }
}