package org.bochman.upapp.data.db;

import android.content.Context;

import org.bochman.upapp.data.dao.PlacePhotoDao;
import org.bochman.upapp.data.dao.PoiDao;
import org.bochman.upapp.data.enteties.BitmapConverter;
import org.bochman.upapp.data.enteties.DateConverter;
import org.bochman.upapp.data.enteties.PlacePhoto;
import org.bochman.upapp.data.enteties.Poi;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

// bump version number if your schema changes
@Database(entities = {Poi.class, PlacePhoto.class}, version = 3)
@TypeConverters({BitmapConverter.class, DateConverter.class})

public abstract class PoiDatabase extends RoomDatabase {

    // Declare your data access objects as abstract
    public abstract PoiDao poiDao();
    public abstract PlacePhotoDao placePhotoDao();

    // Database name to be used
    public static final String NAME = "PoiDatabase";

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE poi ADD COLUMN photoUri TEXT");
        }
    };

    //There must be only one database instance!
    private static volatile PoiDatabase INSTANCE;

    //The singleton accessor
    public static PoiDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PoiDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PoiDatabase.class, NAME)
                        //    .addMigrations(MIGRATION_1_2)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
