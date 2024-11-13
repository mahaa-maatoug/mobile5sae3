package com.example.constat.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.constat.Dao.ConstatDao;
import com.example.constat.entity.Constat;

@Database(entities = {Constat.class}, version = 1)
public abstract class ConstatDatabase extends RoomDatabase {
  private static ConstatDatabase instance;

  public abstract ConstatDao constatDao();

  public static synchronized ConstatDatabase getDatabase(Context context) {
    if (instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(),
          ConstatDatabase.class, "constat_database")
        .fallbackToDestructiveMigration()
        .build();
    }
    return instance;
  }
}
