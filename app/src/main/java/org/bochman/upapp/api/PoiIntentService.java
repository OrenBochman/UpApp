package org.bochman.upapp.api;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.bochman.upapp.BuildConfig;
import org.bochman.upapp.UpApp;
import org.bochman.upapp.data.enteties.Poi;
import org.bochman.upapp.data.repository.PoiRepository;
import org.bochman.upapp.utils.Debug;
import org.bochman.upapp.utils.SpUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * An {@link IntentService} subclass for querying places api off the main thread
 * <p>
 */
public class PoiIntentService extends IntentService {

    public static final String QUERY = "query";
    String TAG = PoiIntentService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public PoiIntentService() {
        super("PoiIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        String query = intent.getStringExtra(QUERY); // gets the query from search buttons
        float userlat = (float) SpUtils.getLat(this);
        float userlng = (float) SpUtils.getLng(this);
        Log.e(Debug.getTag(), String.format("onHandleIntent: %s %f %f ", query, userlat, userlng));

        // Specify the fields to return.
        List<Place.Field> placeFields =
                Arrays.asList(Place.Field.ID,
                        Place.Field.NAME,
                        Place.Field.LAT_LNG,
                        Place.Field.ADDRESS,
                        //       Place.Field.PHONE_NUMBER,
                        //       Place.Field.WEBSITE_URI,
                        Place.Field.RATING);
        //init places
        Places.initialize(this, BuildConfig.google_maps_key);
        PlacesClient placesClient = Places.createClient(this);
        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                /// do this in another thread
                if (task.isSuccessful()) {

                    //process the response off main thread.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            FindCurrentPlaceResponse response = task.getResult();
                            if (response != null) {
                                PoiRepository mPoiRepository = new PoiRepository(getApplication());
                                mPoiRepository.deletePois();
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

                                    mPoiRepository.insert(poi);

                                    //((UpApp) getApplicationContext()).getPoiDatabase().poiDao().insert(poi);
                                    Log.i(Debug.getTag(), "Inserting into DB: " + poi.toString());

                                }
                            }
                        }
                    }).start();
                } else {
                    Exception exception = task.getException();
                    //better error reporting to troubleshoot the api
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        String msg;
                        switch (apiException.getStatusCode()) {
                            case 15:
                                msg = "Location timeout";
                                break;
                            case 9012:
                                msg = "INVALID_REQUEST - place_id or field request issue";
                                break;
                            case 9013:
                                msg = "NOT_FOUND - place_id no found in the db";
                                break;
                            case 9010:
                                msg = "OVER_QUERY_LIMIT";
                                break;
                            case 9011:
                                msg = "REQUEST_DENIED - Api missing or invalid";
                                break;
                        }
                        Log.e(Debug.getTag(), "msg " + apiException.getStatusCode(), apiException);

                    }
                }
            });

        }
    }
}
