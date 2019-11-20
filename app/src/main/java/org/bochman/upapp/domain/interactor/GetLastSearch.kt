package org.bochman.upapp.domain.interactor;

import org.bochman.upapp.data.repository.PoiRepository

/**
 * Encapsulate a nearby search request.
 */
class GetLastSearch(private val poiRepository: PoiRepository) {

    suspend operator fun invoke(): String {
        return poiRepository.loadSearchQuery()
    }
}
