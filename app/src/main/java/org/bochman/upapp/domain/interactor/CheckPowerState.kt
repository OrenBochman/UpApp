package org.bochman.upapp.domain.interactor

import androidx.lifecycle.LiveData
import org.bochman.upapp.data.repository.PoiRepository

/**
 * Encapsulate a power level check.
 */
class CheckPowerState internal constructor(private val repo: PoiRepository) {

    suspend operator fun invoke(): LiveData<Boolean> {
        return repo.fetchPowerState()
    }

}