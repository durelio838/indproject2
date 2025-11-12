package com.example.myapplication.activities;

import android.content.Intent; // Add this import
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Question;

public class QuestionActivity extends BaseActivity {
    private EditText etTheme, etQuestionText;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        setupToolbar("Задать вопрос", true);
        initViews();
    }

    private void initViews() {
        etTheme = findViewById(R.id.etTheme);
        etQuestionText = findViewById(R.id.etQuestionText);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> submitQuestion());
    }

    private void submitQuestion() {
        String theme = etTheme.getText().toString().trim();
        String text = etQuestionText.getText().toString().trim();

        if (theme.isEmpty() || theme.length() > 20) {
            etTheme.setError("Тема вопроса должна быть от 1 до 20 символов");
            return;
        }

        if (text.isEmpty() || text.length() > 150) {
            etQuestionText.setError("Текст вопроса должен быть от 1 до 150 символов");
            return;
        }

        new Thread(() -> {
            Question question = new Question();
            question.setTheme(theme);
            question.setText(text);
            question.setCreatedDate(new java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault()).format(new java.util.Date()));

            // Временные данные пользователя
            question.setUserId(1);
            question.setUserName("Тестовый пользователь");
            question.setUserPhone("+79999999999");

            database.questionDao().insertQuestion(question);

            runOnUiThread(() -> {
                Toast.makeText(this, "Вопрос отправлен успешно!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MenuActivity.class));
                finish();
            });
        }).start();
    }
}