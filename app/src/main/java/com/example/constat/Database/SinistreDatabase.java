package com.example.constat.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.constat.Dao.SinistreDao;
import com.example.constat.entity.Sinistre;

@Database(entities = {Sinistre.class}, version = 1)
public abstract class SinistreDatabase extends RoomDatabase {
  private static SinistreDatabase instance;

  public abstract SinistreDao sinistreDao();

  public static synchronized SinistreDatabase getDatabase(Context context) {
    if (instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(),
          SinistreDatabase.class, "sinistre_database")
        .fallbackToDestructiveMigration()
        .build();
    }
    return instance;
  }
}
