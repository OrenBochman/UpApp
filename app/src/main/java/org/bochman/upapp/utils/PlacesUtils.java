package org.bochman.upapp.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.apache.commons.lang3.StringUtils;
import org.bochman.upapp.BuildConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Helper class to handle the Google Places API v2 boiler plate and keep the activities slimmer.
 *
 * Can be initilized in the App, The ViewModel or MainActivity.
 */
public class PlacesUtils {
    /**
     * THe places api client.
     */
    private PlacesClient placesClient;

    private PlacesUtils(Context ctx) {
        // Initialize Places.
        Places.initialize(ctx, BuildConfig.google_maps_key);

        // Create a new Places client instance.
        placesClient = Places.createClient(ctx);

    }
    private static PlacesUtils inst =null;

    public static PlacesUtils getInstance(Context ctx){
           if(inst==null)
               inst=new PlacesUtils(ctx);
           return inst;
    }

    /**
     * auto complete a query
     * // TODO: needs to move to a util class + unit tests
     * @param query - text to autocomplete.
     */
   public  void autocompleteQuery(String query){
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // TODO: needs to fetchBounds() based on fused location + distance .

        // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596));


        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setCountry("au")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Log.i(Debug.getTag(), prediction.getPlaceId());
                Log.i(Debug.getTag(), prediction.getPrimaryText(null).toString());
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(Debug.getTag(), "Place not found: " + apiException.getStatusCode());
            }
        });
    } // autocompleteQuery :-}~


    /**
     * TODO: figure out how to set the language to english
     * @return
     */
   public List<Poi> fetchCurrentPlaces(List<Poi> currentPlaces){
       Log.i(Debug.getTag(), "entry");

        //List<Poi> currentPlaces= new ArrayList<>();

        // Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.PHOTO_METADATAS);

        // Construct a request object, passing the place ID and fields array.
        //FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder( placeFields).build();


       // Call findCurrentPlace and handle the response (first check that the user has granted permission).
           placesClient.findCurrentPlace(request).addOnSuccessListener(((response) -> {
               for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                   currentPlaces.add(
                           new Poi(
                               placeLikelihood.getPlace().getId(),
                               placeLikelihood.getPlace().getName(),
                               placeLikelihood.getPlace().getAddress(),
                               placeLikelihood.getPlace().getLatLng().latitude,
                               placeLikelihood.getPlace().getLatLng().longitude
                           )
                   );
                   Log.i(Debug.getTag(), String.format("Place '%s' \t has likelihood: %f  - %s ",
                           StringUtils.rightPad(placeLikelihood.getPlace().getName(),26),
                           placeLikelihood.getLikelihood(),
                           placeLikelihood.getPlace().getLatLng() ));
               }
           })).addOnFailureListener((exception) -> {
               if (exception instanceof ApiException) {
                   ApiException apiException = (ApiException) exception;
                   Log.e(Debug.getTag(), "Place not found: " + apiException.getStatusCode());
               }else{
                   Log.e(Debug.getTag(), "Error: " + exception.toString());
               }
           });

       Log.e(Debug.getTag(), "currentPlaces size: " + currentPlaces.size());

       return currentPlaces;

    }


}
