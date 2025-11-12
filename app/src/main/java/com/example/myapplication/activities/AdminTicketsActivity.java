package com.example.myapplication.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdminTicketsAdapter;
import com.example.myapplication.models.Question;
import com.example.myapplication.models.Ticket;
import com.example.myapplication.models.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminTicketsActivity extends BaseActivity {
    private RecyclerView recyclerViewTickets;
    private EditText etSearch;
    private AdminTicketsAdapter adapter;
    private List<Object> allItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tickets);

        setupToolbar("Все заявки", true);
        initViews();
        loadTickets();
    }

    private void initViews() {
        recyclerViewTickets = findViewById(R.id.recyclerViewTickets);
        etSearch = findViewById(R.id.etSearch);

        recyclerViewTickets.setLayoutManager(new LinearLayoutManager(this));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTickets(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadTickets() {
        new Thread(() -> {
            List<Ticket> tickets = database.ticketDao().getAllTickets();
            List<Question> questions = database.questionDao().getAllQuestions();

            allItems = new ArrayList<>();
            allItems.addAll(tickets);
            allItems.addAll(questions);

            // Сортировка по дате создания (новые сверху)
            allItems.sort((o1, o2) -> {
                String date1 = getCreatedDate(o1);
                String date2 = getCreatedDate(o2);
                return date2.compareTo(date1);
            });

            runOnUiThread(() -> {
                adapter = new AdminTicketsAdapter(allItems, this::showAnswerDialog, this::blockUser);
                recyclerViewTickets.setAdapter(adapter);
            });
        }).start();
    }

    private String getCreatedDate(Object item) {
        if (item instanceof Ticket) {
            return ((Ticket) item).getCreatedDate();
        } else if (item instanceof Question) {
            return ((Question) item).getCreatedDate();
        }
        return "";
    }

    private void filterTickets(String search) {
        if (adapter != null) {
            adapter.filter(search);
        }
    }

    private void showAnswerDialog(Question question) {
        EditText input = new EditText(this);
        input.setHint("Введите ответ");
        input.setText(question.getAnswer());

        new AlertDialog.Builder(this)
                .setTitle("Ответить на вопрос")
                .setView(input)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    String answer = input.getText().toString().trim();
                    if (!answer.isEmpty()) {
                        saveAnswer(question, answer);
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void saveAnswer(Question question, String answer) {
        new Thread(() -> {
            question.setAnswer(answer);
            database.questionDao().updateQuestion(question);

            runOnUiThread(() -> {
                Toast.makeText(this, "Ответ сохранен", Toast.LENGTH_SHORT).show();
                loadTickets(); // Обновляем список
            });
        }).start();
    }

    private void blockUser(int userId) {
        new Thread(() -> {
            User user = database.userDao().getUserById(userId);
            if (user != null) {
                user.setBlocked(true);
                database.userDao().updateUser(user);

                runOnUiThread(() ->
                        Toast.makeText(this, "Пользователь заблокирован", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}