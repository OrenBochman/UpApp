package org.bochman.upapp;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.bochman.upapp.data.db.PoiDatabase;
import org.bochman.upapp.data.repository.PoiRepository;
import org.bochman.upapp.utils.CacheUtils;
import org.bochman.upapp.utils.Debug;

import androidx.room.Room;

public class UpApp extends Application {

    private static final String TAG = UpApp.class.getSimpleName();
    /**
     * The FusedLocation client.
     */
    private FusedLocationProviderClient fusedLocationClient;

    //PoiDatabase poiDatabase;
    PlacesClient placesClient;
    PoiRepository mPoiRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Debug.getTag(), String.format("UpApp.onCreate()"));

        CacheUtils.initializeCache(this);

        // Initialize FusedLocation APIs
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mPoiRepository = new PoiRepository(this);

        //init places
        Places.initialize(this,BuildConfig.google_maps_key);
        placesClient= Places.createClient(this);
    }

//    public PoiDatabase getPoiDatabase() {
//        return poiDatabase;
//    }
    public PoiRepository getPoiRepository(){
        return mPoiRepository;}
    public PlacesClient  getPlacesClient(){ return placesClient; }

}
