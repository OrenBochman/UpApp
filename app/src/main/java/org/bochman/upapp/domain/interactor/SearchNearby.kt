package org.bochman.upapp.domain.interactor;

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng;
import org.bochman.upapp.data.enteties.Poi

import org.bochman.upapp.data.repository.PoiRepository;

/**
 * Encapsulate a nearby search request.
 */
class SearchNearby(private val poiRepository: PoiRepository){

        suspend operator fun invoke(latlng : LatLng) =  poiRepository.fetchPoiByLatLng(latlng)
}
