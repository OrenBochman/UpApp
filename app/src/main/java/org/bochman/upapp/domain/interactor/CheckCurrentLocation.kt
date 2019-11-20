package org.bochman.upapp.domain.interactor;

import android.location.Location;

import org.bochman.upapp.data.repository.PoiRepository;
import org.bochman.upapp.domain.executor.PostExecutionThread;
import org.bochman.upapp.domain.executor.ThreadExecutor;
import org.bochman.upapp.domain.interactor.base.UseCase;

import java.util.Observable;
import java.util.Optional;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;

class CheckCurrentLocation (private val poiRepository: PoiRepository){

    suspend operator fun invoke(): LiveData<Optional<Location>> = poiRepository.fetchLastLocation();

}
