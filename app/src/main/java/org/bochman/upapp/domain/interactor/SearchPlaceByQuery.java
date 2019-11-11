package org.bochman.upapp.domain.interactor;

import org.bochman.upapp.data.repository.PoiRepository;

/**
 * Encapsulate a query search request.
 */
class SearchPlaceByQuery  {

    private final PoiRepository repo;

    SearchPlaceByQuery(PoiRepository repo){
        this.repo=repo;
    }

    void invoke(String query){
        repo.fetchPoiByQuery(query);
    }
}