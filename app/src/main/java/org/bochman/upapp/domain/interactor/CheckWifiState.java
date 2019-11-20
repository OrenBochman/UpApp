package org.bochman.upapp.domain.interactor;

import org.bochman.upapp.data.repository.PoiRepository;

import androidx.lifecycle.LiveData;

public class CheckWifiState {

    private final PoiRepository repo;

    public CheckWifiState(PoiRepository repo){
        this.repo=repo;
    }

    LiveData<Boolean> invoke(){
        return repo.fetchWifiState();
    }
}
