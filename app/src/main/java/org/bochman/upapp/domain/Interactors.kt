package org.bochman.upapp.domain

import org.bochman.upapp.domain.interactor.*


/**
 * Wrapper for injecting each interactor into the PoiViewModel
 *
 * TODO: add interactor for the following features:
 *   setting units
 *   deleting favourites
 * TODO: add interactor for the following extra features:
 *   deleting network cache
 *   deleting app photo storage + add setting option
 *   setting the map type
 *   setting radius
 *   setting place category
 *
 */
data class Interactors (
        val mCheckCurrentLocation: CheckCurrentLocation,
        val mCheckPowerState : CheckPowerState,
        val mCheckWifiState : CheckWifiState,
        val mGetLastSearch:GetLastSearch,
        val mSearchNearby:SearchNearby,
        val mSearchPlaceByQuery:SearchPlaceByQuery

        )