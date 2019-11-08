package org.bochman.upapp.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.bochman.upapp.BuildConfig;
import org.bochman.upapp.R;
import org.bochman.upapp.UpApp;
import org.bochman.upapp.data.enteties.Poi;
import org.bochman.upapp.data.repository.PoiRepository;
import org.bochman.upapp.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import androidx.annotation.NonNull;

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
        mPoiRepository = new PoiRepository((UpApp) ctx.getApplicationContext());
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

    PoiRepository mPoiRepository;

    List<Poi> result = new ArrayList<>();

    Task<FindCurrentPlaceResponse>  findCurrentLocation() {
        result.clear();

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(findCurrentPlaceRequestFields);
        Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(new MyCurrentPlaceListener());

        return placeResponse;
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

    String parseStatusCode(Exception exception) {
        if (exception instanceof ApiException) {
            ApiException apiException = (ApiException) exception;

            switch (apiException.getStatusCode()) {
                case 15:
                    return "Location timeout";

                case 9012:
                    return "INVALID_REQUEST - place_id or field request issue";

                case 9013:
                    return "NOT_FOUND - place_id no found in the db";

                case 9010:
                    return "OVER_QUERY_LIMIT";

                case 9011:
                    return "REQUEST_DENIED - Api missing or invalid";
            }
        }
        return "not api Exption";
    }


    class MyCurrentPlaceListener implements OnCompleteListener<FindCurrentPlaceResponse> {

        @Override
        public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
            if (task.isSuccessful()) {
                FindCurrentPlaceResponse response = task.getResult();
                if (response != null ) {

                    result.clear();

                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Log.i(Debug.getTag(), String.format("Place '%s' has likelihood: %f",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));

                        // handling nullable types in the response
                        Optional<LatLng> optionalLatLng = Optional.ofNullable(placeLikelihood.getPlace().getLatLng());
                        Optional<Double> optionalRating = Optional.ofNullable(placeLikelihood.getPlace().getRating());
                        Optional<Uri> optionalWebsiteUri = Optional.ofNullable(placeLikelihood.getPlace().getWebsiteUri());

                        Poi poi = new Poi(placeLikelihood.getPlace().getId(),
                                placeLikelihood.getPlace().getName(),
                                optionalLatLng.map(x -> x.latitude).orElse(0.0),
                                optionalLatLng.map(x -> x.longitude).orElse(0.0),
                                placeLikelihood.getPlace().getAddress(),
                                "", //        placeLikelihood.getPlace().getPhoneNumber(),
                                optionalWebsiteUri.map(Uri::toString).orElse(""),
                                optionalRating.orElse(0.0));

                        //this needs to happen off the main/ui thread
                        result.add(poi);
                    }
                }
            } else {
                String msg = parseStatusCode(task.getException());
                Log.e(Debug.getTag(), msg);

                //better error reporting to troubleshoot the api

            }
        }
    }

    public String getShareString(Context ctx) {
        return ctx.getResources().getString(R.string.share);
    }
//
//    //process the response off main thread.
//    new Thread(new Runnable() {
//        @Override
//        public void run(){
//
//        }
//                }).run();

}
