package org.bochman.upapp.api;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;

import org.bochman.upapp.BuildConfig;
import org.bochman.upapp.UpApp;
import org.bochman.upapp.data.enteties.Poi;
import org.bochman.upapp.utils.SpUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * An {@link IntentService} subclass for quearing places api off the main thread
 * <p>
 */
public class PoiIntentService extends IntentService {

    public static final String QUERY =  "query" ;
    String TAG = PoiIntentService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public PoiIntentService() {
        super("PoiIntentService");
    }

    final static String API_KEY = BuildConfig.google_maps_key;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String query = intent.getStringExtra(QUERY); // gets the query from search buttons

        float userlat = (float) SpUtils.getLat(this);
        float userlng = (float) SpUtils.getLng(this);

        // Specify the fields to return.
        List<Place.Field> placeFields =
                Arrays.asList(Place.Field.ID,
                        Place.Field.NAME,
                        Place.Field.LAT_LNG,
                        Place.Field.ADDRESS,
                        Place.Field.PHONE_NUMBER,
                        Place.Field.WEBSITE_URI,
                        Place.Field.RATING);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = ((UpApp) getApplicationContext()).getPlacesClient().findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();
                    if(response!=null)
                        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                            Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                    placeLikelihood.getPlace().getName(),
                                    placeLikelihood.getLikelihood()));

                            // handling nullable types in the response
                            Optional<LatLng> optionalLatLng = Optional.ofNullable(placeLikelihood.getPlace().getLatLng());
                            Optional<Double> optionalRating = Optional.ofNullable(placeLikelihood.getPlace().getRating());
                            Optional<Uri> optionalWeb = Optional.ofNullable(placeLikelihood.getPlace().getWebsiteUri());

                            Poi poi = new Poi(
                                    placeLikelihood.getPlace().getId(),
                                    placeLikelihood.getPlace().getName(),
                                    optionalLatLng.map(x -> x.latitude).orElse(0.0),
                                    optionalLatLng.map(x -> x.longitude).orElse(0.0),
                                    placeLikelihood.getPlace().getAddress(),
                                    placeLikelihood.getPlace().getPhoneNumber(),
                                    optionalWeb.map(Uri::toString).orElse(""),
                                    optionalRating.orElse(0.0));

                            ((UpApp) getApplicationContext()).getPoiDatabase().poiDao().insertUser(poi);
                            Log.i(TAG, "Inserting into DB: " + poi.toString());
                        }

                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                }
            });

        }
    }
}
