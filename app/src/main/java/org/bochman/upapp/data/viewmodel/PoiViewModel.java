package org.bochman.upapp.data.viewmodel;

import android.app.Application;

import org.bochman.upapp.data.enteties.Poi;
import org.bochman.upapp.data.repository.PoiRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PoiViewModel extends AndroidViewModel {

    private PoiRepository mRepository;

    private LiveData<List<Poi>> mAllPois;
    private LiveData<List<Poi>> mAllFavs;

    public PoiViewModel (Application application) {
        super(application);
        mRepository = new PoiRepository(application);
        mAllPois = mRepository.getAllPois();
        mAllFavs = mRepository.getmAllFavs();

    }
    public void insert(Poi poi) { mRepository.insert(poi); }

}
