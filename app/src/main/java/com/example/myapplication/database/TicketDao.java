package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.models.Ticket;

import java.util.List;

@Dao
public interface TicketDao {
    @Insert
    void insertTicket(Ticket ticket);

    @Update
    void updateTicket(Ticket ticket);

    @Delete
    void deleteTicket(Ticket ticket);

    @Query("SELECT * FROM tickets")
    List<Ticket> getAllTickets();

    @Query("SELECT * FROM tickets WHERE id = :id")
    Ticket getTicketById(int id);

    @Query("SELECT * FROM tickets WHERE userId = :userId")
    List<Ticket> getTicketsByUserId(int userId);

    @Query("SELECT * FROM tickets WHERE status = :status")
    List<Ticket> getTicketsByStatus(String status);

    @Query("SELECT * FROM tickets WHERE type = :type")
    List<Ticket> getTicketsByType(String type);

    @Query("SELECT * FROM tickets WHERE date = :date")
    List<Ticket> getTicketsByDate(String date);

    @Query("SELECT * FROM tickets WHERE date = :date AND time = :time")
    Ticket getTicketByDateTime(String date, String time);
}
