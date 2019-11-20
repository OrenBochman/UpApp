package org.bochman.upapp.presentation;

import android.app.Application;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.bochman.upapp.BuildConfig;
import org.bochman.upapp.data.viewmodel.PoiViewModelFactory;
import org.bochman.upapp.domain.Interactors;
import org.bochman.upapp.data.repository.PoiRepository;
import org.bochman.upapp.domain.interactor.CheckCurrentLocation;
import org.bochman.upapp.domain.interactor.CheckPowerState;
import org.bochman.upapp.domain.interactor.CheckWifiState;
import org.bochman.upapp.domain.interactor.GetLastSearch;
import org.bochman.upapp.domain.interactor.SearchNearby;
import org.bochman.upapp.domain.interactor.SearchPlaceByQuery;
import org.bochman.upapp.utils.CacheUtils;

import timber.log.Timber;

import static timber.log.Timber.*;

public class UpApp extends Application {

    private static final String TAG = UpApp.class.getSimpleName();
    /**
     * The FusedLocation client.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    //PoiDatabase poiDatabase;
    PlacesClient placesClient;
    PoiRepository mPoiRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            plant(new DebugTree());
        }
        Timber.tag("UpApp");
        Timber.d("UpApp.onCreate()");

        CacheUtils.initializeCache(this);

        // Initialize FusedLocation APIs
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mPoiRepository = new PoiRepository(this);

        //init places
        Places.initialize(this,BuildConfig.google_maps_key);
        placesClient= Places.createClient(this);

        // TODO: replace with dagger based dependency injection
        // Manually Injecting interactor dependencies into view model
        PoiViewModelFactory.inject(this,new Interactors(
                new CheckCurrentLocation(mPoiRepository),
                new CheckPowerState(mPoiRepository),
                new CheckWifiState(mPoiRepository),
                new GetLastSearch(mPoiRepository),
                new SearchNearby(mPoiRepository),
                new SearchPlaceByQuery(mPoiRepository)
        ));

    }

}
