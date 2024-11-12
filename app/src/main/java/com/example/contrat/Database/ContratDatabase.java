package com.example.contrat.Database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import android.content.Context;

import com.example.contrat.Dao.ContratDao;
import com.example.contrat.entity.Contrat;

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
