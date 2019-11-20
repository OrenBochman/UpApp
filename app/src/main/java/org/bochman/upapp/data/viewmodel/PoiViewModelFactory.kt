package org.bochman.upapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.bochman.upapp.domain.Interactors

object PoiViewModelFactory : ViewModelProvider.Factory {

  lateinit var application: Application

  lateinit var dependencies: Interactors

  @JvmStatic
  fun inject(application: Application, dependencies: Interactors) {
    PoiViewModelFactory.application = application
    PoiViewModelFactory.dependencies = dependencies
  }

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if(PoiViewModel::class.java.isAssignableFrom(modelClass)) {
      return modelClass.getConstructor(Application::class.java, Interactors::class.java)
          .newInstance(
                  application,
                  dependencies)
    } else {
      throw IllegalStateException("ViewModel must extend PoiViewModel")
    }
  }



}