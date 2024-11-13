package com.example.actualite.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.actualite.Dao.ActualiteDao;
import com.example.actualite.entity.Actualite;

@Database(entities = {Actualite.class}, version = 1)
public abstract class ActualiteDatabase extends RoomDatabase {
  private static ActualiteDatabase instance;

  public abstract ActualiteDao actualiteDao();

  public static synchronized ActualiteDatabase getDatabase(Context context) {
    if (instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(),
          ActualiteDatabase.class, "actualite_database")
        .fallbackToDestructiveMigration()
        .build();
    }
    return instance;
  }
}
