package org.bochman.upapp.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import org.bochman.upapp.data.dao.PoiDao;
import org.bochman.upapp.data.db.PoiDatabase;
import org.bochman.upapp.data.enteties.Poi;

import java.util.List;

import androidx.lifecycle.LiveData;

public class PoiRepository {

    private PoiDao mPoiDao;
    private LiveData<List<Poi>> mAllPois;
    private LiveData<List<Poi>> mAllFavs;

    public PoiRepository(Application application) {
        PoiDatabase db = PoiDatabase.getDatabase(application);
        mPoiDao = db.poiDao();
        mAllPois = mPoiDao.getAllPlaces();
        mAllFavs = mPoiDao.getAllFavourites();
    }

    public LiveData<List<Poi>> getAllPois() {
        return mAllPois;
    }

    public LiveData<List<Poi>> getAllFavs() {
        return mAllFavs;
    }


    public void delete(String id) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                mPoiDao.delete(id);
            }
        }).start();
    }
    public void deleteAllPois() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                mPoiDao.deleteAllPoi();
            }
        }).start();
    }

    public void deleteAllFavourites() {

        new Thread(new Runnable(){
            @Override
            public void run() {
                mPoiDao.deleteAllFavourites();
            }
        }).start();
    }

    public void insert (Poi Poi) {
        new insertAsyncTask(mPoiDao).execute(Poi);
    }

    private static class insertAsyncTask extends AsyncTask<Poi, Void, Void> {

        private PoiDao mAsyncTaskDao;

        insertAsyncTask(PoiDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Poi... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
