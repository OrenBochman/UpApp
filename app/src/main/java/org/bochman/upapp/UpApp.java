package org.bochman.upapp;

import android.app.Application;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.bochman.upapp.data.db.PoiDatabase;
import org.bochman.upapp.utils.CacheUtils;

import androidx.room.Room;

public class UpApp extends Application {

    private static final String TAG = UpApp.class.getSimpleName();

    PoiDatabase poiDatabase;
    PlacesClient placesClient;
    @Override
    public void onCreate() {
        super.onCreate();
        CacheUtils.initializeCache(this);

        //init DB  - when upgrading versions, kill the original tables by using fallbackToDestructiveMigration()
        poiDatabase = Room.databaseBuilder(this, PoiDatabase.class, PoiDatabase.NAME).fallbackToDestructiveMigration().build();

        //init places
        Places.initialize(this,BuildConfig.google_maps_key);
        placesClient= Places.createClient(this);
    }

    public PoiDatabase getPoiDatabase() {
        return poiDatabase;
    }

    public PlacesClient  getPlacesClient(){ return placesClient; }

}
