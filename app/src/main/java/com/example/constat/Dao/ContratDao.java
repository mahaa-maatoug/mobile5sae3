package com.example.constat.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.constat.entity.Contrat;

import java.util.List;

@Dao
public interface ContratDao {

    // Insérer un nouveau contrat
    @Insert
    void insertContrat(Contrat contrat);

    // Mettre à jour un contrat existant
    @Update
    void updateContrat(Contrat contrat);

    // Supprimer un contrat
    @Delete
    void deleteContrat(Contrat contrat);

    // Récupérer tous les contrats de la base de données
    @Query("SELECT * FROM contrat_table")
    List<Contrat> getAllContrats();

    // Récupérer un contrat spécifique par son ID
    @Query("SELECT * FROM contrat_table WHERE id = :id")
    LiveData<Contrat> getContratById(int id);

    // Récupérer des contrats par type (filtrer par type de contrat)
    @Query("SELECT * FROM contrat_table WHERE contract_type = :type")
    List<Contrat> getContratsByType(String type);

    // Récupérer des contrats par titre (filtrer par titre de contrat)
    @Query("SELECT * FROM contrat_table WHERE contract_title LIKE '%' || :title || '%'")
    List<Contrat> getContratsByTitle(String title);

    // Récupérer des contrats dans une plage de dates spécifique
    @Query("SELECT * FROM contrat_table WHERE start_date BETWEEN :startDate AND :endDate")
    List<Contrat> getContratsInDateRange(String startDate, String endDate);

    // Récupérer tous les contrats triés par prix en ordre décroissant
    @Query("SELECT * FROM contrat_table ORDER BY price DESC")
    List<Contrat> getContratsSortedByPriceDesc();

    // Delete a constat by its ID
    @Query("DELETE FROM contrat_table WHERE id = :id")
    void deleteConstatById(int id);

}
