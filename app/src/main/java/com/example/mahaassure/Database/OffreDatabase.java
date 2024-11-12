package com.example.mahaassure.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mahaassure.Dao.OffreDao;
import com.example.mahaassure.entity.Offre;


@Database(entities = {Offre.class}, version = 1)
public abstract class OffreDatabase extends RoomDatabase {
  private static OffreDatabase instance;

  public abstract OffreDao offreDao();

  public static synchronized OffreDatabase getDatabase(Context context) {
    if (instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(),
          OffreDatabase.class, "offre_database")
        .fallbackToDestructiveMigration()
        .build();
    }
    return instance;
  }
}
