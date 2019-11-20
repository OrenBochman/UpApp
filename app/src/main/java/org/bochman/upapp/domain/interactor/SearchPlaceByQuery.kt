package org.bochman.upapp.domain.interactor;

import com.google.android.gms.maps.model.LatLng
import org.bochman.upapp.data.repository.PoiRepository;

/**
 * Encapsulate a query search request.
 */
class SearchPlaceByQuery(private val poiRepository: PoiRepository){

    suspend operator fun invoke(query:String) =  poiRepository.fetchPoiByQuery(query)


}