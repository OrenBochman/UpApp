package org.bochman.upapp.data.repository;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;

import com.github.javafaker.Bool;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.bochman.upapp.api.HttpPoiSearchIntentService;
import org.bochman.upapp.api.PoiIntentService;
import org.bochman.upapp.data.dao.PlacePhotoDao;
import org.bochman.upapp.data.dao.PoiDao;
import org.bochman.upapp.data.db.PoiDatabase;
import org.bochman.upapp.data.enteties.PlacePhoto;
import org.bochman.upapp.data.enteties.Poi;
import org.bochman.upapp.utils.SpUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

// Informs Dagger that this class should be constructed only once.
//@Singleton
//@OpenForTesting

public class PoiRepository {

    private Application mApplication;
    private PoiDao mPoiDao;
    private PlacePhotoDao mPlacePhotoDao;

//    private PhotoWS  mPhotoWS;
//    private ProximitySearchWS mProximitySearchWS;
//    private QuerySearchWS mQuerySearchWS;
//    private AutocompleteWS mAutocompleteWS;

    private LiveData<List<Poi>> mAllPois;
    private LiveData<List<Poi>> mAllFavs;
    private LiveData<List<PlacePhoto>> mAllPhotos;
    private LiveData<List<PlacePhoto>> mAllFavPhotos;
    private MutableLiveData<Boolean> mWifiOnFlag;
    private MutableLiveData<Boolean> mChargingFlag;
    private MutableLiveData<Optional<Location>> lastLocation;

    // this is need ed to unsubscribe
    private FusedLocationProviderClient fusedLocationClient;

    //@Inject
    public PoiRepository(Application application) {
        PoiDatabase db = PoiDatabase.getDatabase(application);
        mPoiDao = db.poiDao();
        mPlacePhotoDao = db.placePhotoDao();
        mAllPois = mPoiDao.getAllPlaces();
        mAllFavs = mPoiDao.getAllFavourites();
        mAllPhotos = mPlacePhotoDao.getAllPhotos();
        mAllFavPhotos = mPlacePhotoDao.getAllFavPhotos();
        mApplication = application;

    }

    public LiveData<List<PlacePhoto>> getAllPhotos() {
        return mAllPhotos;
    }

    public LiveData<List<PlacePhoto>> getAllFavPhotos() {
        return mAllFavPhotos;
    }

    public LiveData<List<Poi>> getAllPois() {
        return mAllPois;
    }

    public LiveData<List<Poi>> getAllFavs() {
        return mAllFavs;
    }

    public LiveData<Bitmap> getPhoto(String id) {
        return mPlacePhotoDao.getPhoto(id);
    }


    public int getPhotoCount(String id){
        return mPlacePhotoDao.getPhotoCount(id);
    }
    public Poi getPoi(String id){
        return mPoiDao.getById(id);
    }

    public void insertPhoto(PlacePhoto placePhoto) {
        new Thread(() -> mPlacePhotoDao.insertPhoto(placePhoto)).start();
//        new Thread(() -> {
//
//            ContentValues values = new ContentValues();
//            values.put(MediaStore.Images.Media.DISPLAY_NAME, placePhoto.id+".jpg");
//            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//            values.put(MediaStore.Images.Media.IS_PENDING, 1);
//
//            ContentResolver resolver = context.getContentResolver();
//            Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
//            Uri item = resolver.insert(collection, values);
//
//            try (ParcelFileDescriptor pfd = resolver.openFileDescriptor(item, "w", null)) {
//                // Write data into the pending image.
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            // Now that we're finished, release the "pending" status, and allow other apps
//            // to view the image.
//            values.clear();
//            values.put(MediaStore.Images.Media.IS_PENDING, 0);
//            resolver.update(item, values, null, null);
//
//            // update the uri... to a content://id.jpg in the poi
//        }).start();
    }

    public void delete(String id) {
        new Thread(() -> mPoiDao.delete(id)).start();
    }

    public void deleteAllPois() {
        new Thread(() -> mPoiDao.deleteAllPoi()).start();
    }

    public void deleteAllFavourites() {
        new Thread(() -> mPoiDao.deleteAllFavourites()).start();
    }

    public void insert(Poi poi) {
        new Thread(() -> mPoiDao.insert(poi)).start();
    }

    public void update(Poi poi) {
        new Thread(() -> mPoiDao.updatePoi(poi)).start();
    }

    /**
     * TODO: fetch places by query + radius etc.
     *
     * @param query
     */
    public LiveData<List<Poi>> fetchPoiByQuery(String query) {

        Intent intent = new Intent(mApplication, HttpPoiSearchIntentService.class);
        intent.putExtra(HttpPoiSearchIntentService.QUERY, query);
        mApplication.startService(intent);
        return mAllPois;
    }

    /**
     * fetch places by latlng + radius etc.
     *
     * @param latlng
     */
    public LiveData<List<Poi>> fetchPoiByLatLng(LatLng latlng) {
        Intent intent = new Intent(mApplication, PoiIntentService.class);
        intent.putExtra(PoiIntentService.QUERY, "");
        mApplication.startService(intent);
        return mAllPois;
    }

    public void saveSearchQuery(String query) {
        SpUtils.setLastSearch(query, mApplication);
    }

    public String loadSearchQuery() {
        return SpUtils.getLastSearch(mApplication);
    }
    
    public LiveData<Optional<Location>> fetchLastLocation() {
        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mApplication);
        fusedLocationClient.getLastLocation().addOnSuccessListener((Executor) mApplication,
                location -> {
                    // In some rare situations this can be null.
                    if (location != null) {
                        lastLocation.setValue(Optional.of(location));
                    }
                });
        return lastLocation;
    }



    /**
     * Subscribe to location updates.
     *
     * @return live data for the last known location.
     */
    public LiveData<Optional<Location>> subscribeLastKnownLocation() {
        //create if needed
        if(fusedLocationClient==null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(mApplication);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    (mApplication.checkSelfPermission(ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
                            || mApplication.checkSelfPermission(ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED)) {
                fusedLocationClient.getLastLocation().addOnSuccessListener((Executor) mApplication,
                        location -> lastLocation.setValue(Optional.of(location)));
            }
        }
        return lastLocation;
    }

    public LiveData<Boolean> fetchWifiState() {
        return this.mWifiOnFlag;
    }

    public LiveData<Boolean> fetchPowerState() {
        return this.mChargingFlag;
    }

}
