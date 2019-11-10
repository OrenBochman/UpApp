package org.bochman.upapp.data.repository;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import org.bochman.upapp.api.HttpPoiSearchIntentService;
import org.bochman.upapp.api.PhotoIntentService;
import org.bochman.upapp.data.dao.PlacePhotoDao;
import org.bochman.upapp.data.dao.PoiDao;
import org.bochman.upapp.data.db.PoiDatabase;
import org.bochman.upapp.data.enteties.PlacePhoto;
import org.bochman.upapp.data.enteties.Poi;

import java.io.IOException;
import java.util.List;

import androidx.lifecycle.LiveData;

public class PoiRepository {

    private PoiDao mPoiDao;
    private PlacePhotoDao mPlacePhotoDao;
    private LiveData<List<Poi>> mAllPois;
    private LiveData<List<Poi>> mAllFavs;
    private LiveData<List<PlacePhoto>> mAllPhotos;
    private LiveData<List<PlacePhoto>> mAllFavPhotos;

    public PoiRepository(Application application) {
        PoiDatabase db = PoiDatabase.getDatabase(application);
        mPoiDao = db.poiDao();
        mPlacePhotoDao = db.placePhotoDao();
        mAllPois = mPoiDao.getAllPlaces();
        mAllFavs = mPoiDao.getAllFavourites();
        mAllPhotos = mPlacePhotoDao.getAllPhotos();
        mAllFavPhotos = mPlacePhotoDao.getAllFavPhotos();

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

    public void insert(Poi poi) { new Thread(() -> mPoiDao.insert(poi)).start(); }

    public void update(Poi poi){ new Thread(() -> mPoiDao.updatePoi(poi)).start(); }


}
