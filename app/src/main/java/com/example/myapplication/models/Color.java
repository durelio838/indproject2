package com.example.myapplication.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "colors")
public class Color {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String code;
    private String name;
    private String approximatePrice;

    public Color() {
    }

    public Color(String code, String name, String approximatePrice) {
        this.code = code;
        this.name = name;
        this.approximatePrice = approximatePrice;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getApproximatePrice() {
        return approximatePrice;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setApproximatePrice(String approximatePrice) {
        this.approximatePrice = approximatePrice;
    }
}
