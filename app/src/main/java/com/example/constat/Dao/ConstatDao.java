package com.example.constat.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.constat.entity.Constat;

import java.util.List;

@Dao
public interface ConstatDao {

  // Insert a new constat
  @Insert
  void insertConstat(Constat constat);

  // Update an existing constat
  @Update
  void updateConstat(Constat constat);

  // Delete a constat
  @Delete
  void deleteConstat(Constat constat);

  // Get all constats from the database
  @Query("SELECT * FROM constat_table")
  List<Constat> getAllConstats();

  // Optionally, you can query a specific constat by its ID
  @Query("SELECT * FROM constat_table WHERE id = :id")
  LiveData<Constat> getConstatById(int id);

  // Delete a constat by its ID
  @Query("DELETE FROM constat_table WHERE id = :id")
  void deleteConstatById(int id);



}
