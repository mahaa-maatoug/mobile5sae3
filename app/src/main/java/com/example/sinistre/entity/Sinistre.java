package com.example.sinistre.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "sinistre_table")
public class Sinistre implements Serializable {

  @PrimaryKey(autoGenerate = true)
  private int id;

  @ColumnInfo(name = "date_sinistre")
  private String dateSinistre;

  @ColumnInfo(name = "nom_sinistre")
  private String nomSinistre;



  @ColumnInfo(name = "description_sinistre")
  private String descriptionSinistre;

  @ColumnInfo(name = "photo_path")
  private String photoPath;

  // Constructor
  public Sinistre(String dateSinistre, String nomSinistre, String descriptionSinistre) {
    this.dateSinistre = dateSinistre;
    this.nomSinistre = nomSinistre;

    this.descriptionSinistre = descriptionSinistre;
    this.photoPath = photoPath;  // Add the new field in the constructor

  }



  // Getters and Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDateSinistre() {
    return dateSinistre;
  }

  public void setdateSinistre(String dateSinistre) {
    this.dateSinistre = dateSinistre;
  }

  public String getNomSinistre() {
    return nomSinistre;
  }

  public void setNomSinistre(String nomSinistre) {
    this.nomSinistre = nomSinistre;
  }


  public String getDescriptionSinistre() {
    return descriptionSinistre;
  }

  public void setDescriptionSinistre(String descriptionSinistre) {
    this.descriptionSinistre = descriptionSinistre;
  }

  public String getPhotoPath() {
    return photoPath;
  }

  public void setPhotoPath(String photoPath) {
    this.photoPath = photoPath;
  }
}
