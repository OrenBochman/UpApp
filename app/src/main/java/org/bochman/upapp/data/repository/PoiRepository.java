package org.bochman.upapp.data.repository;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;

import org.bochman.upapp.api.HttpPoiSearchIntentService;
import org.bochman.upapp.api.PhotoIntentService;
import org.bochman.upapp.data.dao.PlacePhotoDao;
import org.bochman.upapp.data.dao.PoiDao;
import org.bochman.upapp.data.db.PoiDatabase;
import org.bochman.upapp.data.enteties.PlacePhoto;
import org.bochman.upapp.data.enteties.Poi;

import java.util.List;

import androidx.lifecycle.LiveData;

public class PoiRepository {

    private PoiDao mPoiDao;
    private PlacePhotoDao mPlacePhotoDao;
    private LiveData<List<Poi>> mAllPois;
    private LiveData<List<Poi>> mAllFavs;

    public PoiRepository(Application application) {
        PoiDatabase db = PoiDatabase.getDatabase(application);
        mPoiDao = db.poiDao();
        mPlacePhotoDao = db.placePhotoDao();
        mAllPois = mPoiDao.getAllPlaces();
        mAllFavs = mPoiDao.getAllFavourites();
    }

    public LiveData<List<Poi>> getAllPois() {
        return mAllPois;
    }

    public LiveData<List<Poi>> getAllFavs() {
        return mAllFavs;
    }

    public Bitmap getPhoto(String id) {
        return mPlacePhotoDao.getPhoto(id);
    }

    public void insertPhoto(PlacePhoto placePhoto) {
        new Thread(() -> mPlacePhotoDao.insertPhoto(placePhoto)).start();
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
}
