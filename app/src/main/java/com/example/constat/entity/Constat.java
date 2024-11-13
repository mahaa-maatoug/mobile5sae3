package com.example.constat.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "constat_table")
public class Constat implements Serializable {

  @PrimaryKey(autoGenerate = true)
  private int id;

  @ColumnInfo(name = "date_accident")
  private String dateAccident;

  @ColumnInfo(name = "lieu_accident")
  private String lieuAccident;

  @ColumnInfo(name = "nom_conducteur")
  private String nomConducteur;

  @ColumnInfo(name = "prenom_conducteur")
  private String prenomConducteur;

  @ColumnInfo(name = "numero_contrat_assurance")
  private String numeroContratAssurance;

  @ColumnInfo(name = "description_accident")
  private String descriptionAccident;

  @ColumnInfo(name = "photo_path")
  private String photoPath;

  // Constructor
  public Constat(String dateAccident, String lieuAccident, String nomConducteur, String prenomConducteur, String numeroContratAssurance, String descriptionAccident) {
    this.dateAccident = dateAccident;
    this.lieuAccident = lieuAccident;
    this.nomConducteur = nomConducteur;
    this.prenomConducteur = prenomConducteur;
    this.numeroContratAssurance = numeroContratAssurance;
    this.descriptionAccident = descriptionAccident;
    this.photoPath = photoPath;  // Add the new field in the constructor

  }

  // Getters and Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDateAccident() {
    return dateAccident;
  }

  public void setDateAccident(String dateAccident) {
    this.dateAccident = dateAccident;
  }

  public String getLieuAccident() {
    return lieuAccident;
  }

  public void setLieuAccident(String lieuAccident) {
    this.lieuAccident = lieuAccident;
  }

  public String getNomConducteur() {
    return nomConducteur;
  }

  public void setNomConducteur(String nomConducteur) {
    this.nomConducteur = nomConducteur;
  }

  public String getPrenomConducteur() {
    return prenomConducteur;
  }

  public void setPrenomConducteur(String prenomConducteur) {
    this.prenomConducteur = prenomConducteur;
  }

  public String getNumeroContratAssurance() {
    return numeroContratAssurance;
  }

  public void setNumeroContratAssurance(String numeroContratAssurance) {
    this.numeroContratAssurance = numeroContratAssurance;
  }

  public String getDescriptionAccident() {
    return descriptionAccident;
  }

  public void setDescriptionAccident(String descriptionAccident) {
    this.descriptionAccident = descriptionAccident;
  }

  public String getPhotoPath() {
    return photoPath;
  }

  public void setPhotoPath(String photoPath) {
    this.photoPath = photoPath;
  }
}
