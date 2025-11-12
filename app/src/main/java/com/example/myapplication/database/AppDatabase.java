package com.example.myapplication.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.myapplication.models.User;
import com.example.myapplication.models.Ticket;
import com.example.myapplication.models.Part;
import com.example.myapplication.models.Color;
import com.example.myapplication.models.Question;

@Database(
        entities = {
                User.class,
                Ticket.class,
                Part.class,
                Color.class,
                Question.class
        },
        version = 1,
        exportSchema = false  // Add this to avoid schema export issues
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract TicketDao ticketDao();
    public abstract PartDao partDao();
    public abstract ColorDao colorDao();
    public abstract QuestionDao questionDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "auto_service.db"
                            ).fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}