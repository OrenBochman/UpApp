package org.bochman.upapp.domain.interactor;

import com.google.android.gms.maps.model.LatLng;

import org.bochman.upapp.data.repository.PoiRepository;

/**
 * Encapsulate a nearby search request.
 */
public class GetLastSearch {

    private final PoiRepository repo;

    GetLastSearch(PoiRepository repo){
        this.repo=repo;
    }

    String invoke(){
         return repo.loadSearchQuery();
    }

}
