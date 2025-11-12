package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import com.example.myapplication.models.Part;
import java.util.List;

@Dao
public interface PartDao {
    @Insert
    void insertPart(Part part);

    @Query("SELECT * FROM parts")
    List<Part> getAllParts();

    @Query("SELECT * FROM parts WHERE name LIKE '%' || :search || '%'")
    List<Part> searchParts(String search);

    @Delete
    void deletePart(Part part);
}