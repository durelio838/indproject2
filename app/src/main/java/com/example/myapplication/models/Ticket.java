package com.example.myapplication.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tickets")
public class Ticket {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type; // "repair", "painting", "parts"
    private String status; // "pending", "in_progress", "completed"
    private String createdDate;
    private String date; // Дата записи (бронирования)
    private String time; // Время записи (бронирования)

    // Для repair
    private String description;

    // Для painting
    private int colorId;
    private String colorName;
    private String colorCode;
    private String color; // Для обратной совместимости

    // Для parts
    private int partId;
    private String partName;
    private String carModel; // Модель автомобиля

    // User info
    private int userId;
    private String userName;
    private String userPhone;

    // Booking info (даты бронирования для отображения)
    private String bookingDate;
    private String bookingTime;

    public Ticket() {
        this.status = "pending";
    }

    // ==================== GETTERS ====================

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public int getColorId() {
        return colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getColor() {
        return color != null ? color : colorName;
    }

    public int getPartId() {
        return partId;
    }

    public String getPartName() {
        return partName;
    }

    public String getCarModel() {
        return carModel;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getBookingDate() {
        return bookingDate != null ? bookingDate : date;
    }

    public String getBookingTime() {
        return bookingTime != null ? bookingTime : time;
    }

    // ==================== SETTERS ====================

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }
}
