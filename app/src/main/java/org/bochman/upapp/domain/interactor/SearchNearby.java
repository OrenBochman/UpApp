package org.bochman.upapp.domain.interactor;

import com.google.android.gms.maps.model.LatLng;

import org.bochman.upapp.data.repository.PoiRepository;

/**
 * Encapsulate a nearby search request.
 */
public class SearchNearby {

    private final PoiRepository repo;

    SearchNearby(PoiRepository repo){
        this.repo=repo;
    }

    void invoke(LatLng latlng){
        repo.fetchPoiByLatLng(latlng);
        repo.saveSearchQuery("");
    }

}
