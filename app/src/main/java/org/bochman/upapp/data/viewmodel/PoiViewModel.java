package org.bochman.upapp.data.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;

import org.bochman.upapp.data.enteties.PlacePhoto;
import org.bochman.upapp.data.enteties.Poi;
import org.bochman.upapp.data.repository.PoiRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PoiViewModel extends AndroidViewModel {

    private PoiRepository mRepository;

    private LiveData<List<Poi>> mAllPois;
    private LiveData<List<Poi>> mAllFavs;
    private LiveData<List<PlacePhoto>> mAllPhotos;
    private LiveData<List<PlacePhoto>> mAllFavPhotos;

    public PoiViewModel(Application application) {
        super(application);
        mRepository = new PoiRepository(application);
        mAllPois = mRepository.getAllPois();
        mAllFavs = mRepository.getAllFavs();
        mAllPhotos = mRepository.getAllPhotos();
        mAllFavPhotos = mRepository.getAllFavPhotos();

    }
    public LiveData<Bitmap> getPhoto(String id){
        return mRepository.getPhoto(id);
    }

    public LiveData<List<Poi>> getAllPois(){return mAllPois;}

    public LiveData<List<Poi>> getAllFavs(){return mAllFavs;}

    public LiveData<List<PlacePhoto>> getAllImages() { return mAllPhotos; }

    public LiveData<List<PlacePhoto>> getFavImages() { return mAllFavPhotos; }

    public void insert(Poi poi) { mRepository.insert(poi); }

    public void delete(String id) { mRepository.delete(id); }

    public void update(Poi poi) { mRepository.update(poi); }


}
