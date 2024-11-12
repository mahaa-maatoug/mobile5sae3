package com.example.contrat.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.contrat.entity.Contrat;

import java.util.List;

@Dao
public interface ContratDao {

    // Insert a new contract
    @Insert
    void insertContrat(Contrat contrat);

    // Update an existing contract
    @Update
    void updateContrat(Contrat contrat);

    // Delete a contract
    @Delete
    void deleteContrat(Contrat contrat);

    // Get all contracts from the database
    @Query("SELECT * FROM contrat_table")
    List<Contrat> getAllContrats();

    // Optionally, you can query a specific contract by its ID
    @Query("SELECT * FROM contrat_table WHERE id = :id")
    Contrat getContratById(int id);
}
