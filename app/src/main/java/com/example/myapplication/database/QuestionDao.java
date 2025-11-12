package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.models.Question;
import java.util.List;

@Dao
public interface QuestionDao {
    @Insert
    void insertQuestion(Question question);

    @Query("SELECT * FROM questions WHERE userId = :userId")
    List<Question> getQuestionsByUserId(int userId);

    @Query("SELECT * FROM questions")
    List<Question> getAllQuestions();

    @Update
    void updateQuestion(Question question);
}