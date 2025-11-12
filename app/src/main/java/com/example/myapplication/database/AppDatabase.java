package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.models.Color;
import com.example.myapplication.models.Part;
import com.example.myapplication.models.Question;
import com.example.myapplication.models.Ticket;
import com.example.myapplication.models.User;

@Database(entities = {User.class, Ticket.class, Color.class, Part.class, Question.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract TicketDao ticketDao();
    public abstract ColorDao colorDao();
    public abstract PartDao partDao();
    public abstract QuestionDao questionDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration() // Пересоздаст БД при изменении схемы
                    .build();
        }
        return instance;
    }
}
