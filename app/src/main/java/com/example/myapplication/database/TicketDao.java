package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.models.Ticket;
import java.util.List;

@Dao
public interface TicketDao {
    @Insert
    void insertTicket(Ticket ticket);

    @Query("SELECT * FROM tickets WHERE userId = :userId")
    List<Ticket> getTicketsByUserId(int userId);

    @Query("SELECT * FROM tickets")
    List<Ticket> getAllTickets();

    @Query("DELETE FROM tickets WHERE id = :id")
    void deleteTicket(int id);
}