package com.example.constat.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "offre_table")
public class Offre implements Serializable {

  @PrimaryKey(autoGenerate = true)
  private int id;

  @ColumnInfo(name = "price")
  private double price;

  @ColumnInfo(name = "type")
  private String type;

  @ColumnInfo(name = "description")
  private String description;

  @ColumnInfo(name = "date")
  private String date;

  // Constructor
  public Offre(double price, String type, String description, String date) {
    this.price = price;
    this.type = type;
    this.description = description;
    this.date = date;
  }

  // Getters and Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
