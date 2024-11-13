package com.example.constat.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.constat.entity.Actualite;

import java.util.List;

@Dao
public interface ActualiteDao {

  // Insert a new actualite
  @Insert
  void insertActualite(Actualite actualite);

  // Update an existing actualite
  @Update
  void updateActualite(Actualite actualite);

  // Delete a actualite
  @Delete
  void deleteActualite(Actualite actualite);

  // Get all actualites from the database
  @Query("SELECT * FROM actualite_table")
  List<Actualite> getAllActualites();

  // Optionally, you can query a specific actualite by its ID
  @Query("SELECT * FROM actualite_table WHERE id = :id")
  LiveData<Actualite> getActualiteById(int id);

  // Delete a actualite by its ID
  @Query("DELETE FROM actualite_table WHERE id = :id")
  void deleteActualiteById(int id);



}
