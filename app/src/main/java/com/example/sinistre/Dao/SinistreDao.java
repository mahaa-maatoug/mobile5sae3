package com.example.sinistre.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sinistre.entity.Sinistre;

import java.util.List;

@Dao
public interface SinistreDao {

  // Insert a new sinistre
  @Insert
  void insertSinistre(Sinistre sinistre);

  // Update an existing sinistre
  @Update
  void updateSinistre(Sinistre sinistre);

  // Delete a sinistre
  @Delete
  void deleteSinistre(Sinistre sinistre);

  // Get all sinistres from the database
  @Query("SELECT * FROM sinistre_table")
  List<Sinistre> getAllSinistres();

  // Optionally, you can query a specific sinistre by its ID
  @Query("SELECT * FROM sinistre_table WHERE id = :id")
  LiveData<Sinistre> getSinistreById(int id);

  // Delete a sinistre by its ID
  @Query("DELETE FROM sinistre_table WHERE id = :id")
  void deleteSinistreById(int id);



}
