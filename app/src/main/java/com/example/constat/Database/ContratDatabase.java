package com.example.constat.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.constat.Dao.ContratDao;
import com.example.constat.entity.Contrat;

@Database(entities = {Contrat.class}, version = 1, exportSchema = false)
public abstract class ContratDatabase extends RoomDatabase {
    private static ContratDatabase instance;

    public abstract ContratDao contratDao();

    public static synchronized ContratDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ContratDatabase.class, "contrat_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
