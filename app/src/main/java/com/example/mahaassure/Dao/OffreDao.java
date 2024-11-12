package com.example.mahaassure.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mahaassure.entity.Offre;

import java.util.List;

@Dao
public interface OffreDao {

  // Insert a new offer
  @Insert
  void insertOffre(Offre offre);

  // Update an existing offer
  @Update
  void updateOffre(Offre offre);

  // Delete an offer
  @Delete
  void deleteOffre(Offre offre);

  // Get all offers from the database
  @Query("SELECT * FROM offre_table")
  List<Offre> getAllOffres();

  // Optionally, you can query a specific offer by its ID
  @Query("SELECT * FROM offre_table WHERE id = :id")
  Offre getOffreById(int id);
  @Query("DELETE FROM offre_table WHERE id = :id")
  void deleteconstatById(int id);
}
