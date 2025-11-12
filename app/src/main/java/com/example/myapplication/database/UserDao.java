package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.models.User;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE name = :name")
    User getUserByName(String name);

    @Query("SELECT * FROM users WHERE phone = :phone")
    User getUserByPhone(String phone);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM users WHERE (name = :username OR phone = :phone) AND password = :password")
    User loginUser(String username, String phone, String password);

    @Query("SELECT * FROM users WHERE id = :id")
    User getUserById(int id);
}