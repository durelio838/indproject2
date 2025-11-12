package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import com.example.myapplication.models.Color;
import java.util.List;

@Dao
public interface ColorDao {
    @Insert
    void insertColor(Color color);

    @Query("SELECT * FROM colors")
    List<Color> getAllColors();

    @Delete
    void deleteColor(Color color);
}