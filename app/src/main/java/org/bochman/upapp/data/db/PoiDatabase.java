package org.bochman.upapp.data.db;

import android.content.Context;

import org.bochman.upapp.data.dao.PoiDao;
import org.bochman.upapp.data.enteties.Poi;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// bump version number if your schema changes
@Database(entities = {Poi.class}, version = 1)
public abstract class PoiDatabase extends RoomDatabase {

    // Declare your data access objects as abstract
    public abstract PoiDao poiDao();

    // Database name to be used
    public static final String NAME = "PoiDatabase";

    //There must be only one database instance!
    private static volatile PoiDatabase INSTANCE;

    //The singleton accessor
    public static PoiDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PoiDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PoiDatabase.class, NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
