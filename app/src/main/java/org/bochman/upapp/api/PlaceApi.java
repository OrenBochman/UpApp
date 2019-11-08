package org.bochman.upapp.api;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.bochman.upapp.BuildConfig;
import org.bochman.upapp.R;
import org.bochman.upapp.data.enteties.Poi;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Class to encapsulate the logic of the api request from the handler thread.
 * <p>
 * This class is added to facilitate unit testing use of the API without the
 * the complexity introduced by the HandlerService threading.
 */
public class PlaceApi {

    PlacesClient placesClient;

    public PlaceApi(Context ctx) {
        //init places
        Places.initialize(ctx, BuildConfig.google_maps_key);
        placesClient = Places.createClient(ctx);

    }

    /**
     * for intial query
     */
    static final List<Place.Field> findCurrentPlaceRequestFields
        = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS,
                Place.Field.RATING);

    /**
     * for favourites
     */
     static final List<Place.Field> findPlaceById
            = Arrays.asList(
            Place.Field.PHONE_NUMBER,
            Place.Field.WEBSITE_URI);

    CompletableFuture<List<Poi>> findCurrentLocation() {
        return null;
    }

    CompletableFuture<Bitmap> getPhoto(Poi poi) {
        return null;
    }

    CompletableFuture<Poi> widenPlaceData(Poi poi) {
        return null;
    }

    int StoreResult(Poi poi) {
        return 0;
    }

    String parseStatusCode(ApiException exception) {
        return null;
    }

    public String getShareString(Context ctx) {
        return ctx.getResources().getString(R.string.share);
    }
}
