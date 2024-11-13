package com.example.constat.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "actualite_table")
public class Actualite implements Serializable {

  @PrimaryKey(autoGenerate = true)
  private int id;

  @ColumnInfo(name = "date_actualite")
  private String dateActualite;



  @ColumnInfo(name = "titre_actualite")
  private String titreActualite;

  @ColumnInfo(name = "description_actualite")
  private String descriptionActualite;

  @ColumnInfo(name = "photo_path")
  private String photoPath;

  public Actualite(String descriptionActualite, String titreActualite, String dateActualite) {


    this.descriptionActualite = descriptionActualite;
    this.titreActualite = titreActualite;
    this.dateActualite = dateActualite;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPhotoPath() {
    return photoPath;
  }

  public void setPhotoPath(String photoPath) {
    this.photoPath = photoPath;
  }

  public String getDescriptionActualite() {
    return descriptionActualite;
  }

  public void setDescriptionActualite(String descriptionActualite) {
    this.descriptionActualite = descriptionActualite;
  }

  public String getTitreActualite() {
    return titreActualite;
  }

  public void setTitreActualite(String titreActualite) {
    this.titreActualite = titreActualite;
  }

  public String getDateActualite() {
    return dateActualite;
  }

  public void setDateActualite(String dateActualite) {
    this.dateActualite = dateActualite;
  }

  // Constructor

}
