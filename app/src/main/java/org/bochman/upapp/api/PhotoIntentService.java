package org.bochman.upapp.api;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.bochman.upapp.data.enteties.PlacePhoto;
import org.bochman.upapp.data.repository.PoiRepository;
import org.bochman.upapp.utils.Debug;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

/**
 *
 */
public class PhotoIntentService extends IntentService {

    public static final String TAG = PhotoIntentService.class.getSimpleName();

    /**
     * intent keys
     */
    public static final String PLACE_ID = "placeId";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";

    int width = 300;
    int height = 300;
    String id;
    PoiRepository mPoiRepository;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public PhotoIntentService() {
        super("PhotoIntentService");
        mPoiRepository = new PoiRepository(getApplication());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String photoId = processIntent(intent);
        Log.i(Debug.getTag(), String.format("onHandleIntent: %s ", photoId));

        getPhoto(photoId);

    }

    private String processIntent(@Nullable Intent intent) {
        assert intent != null;
        width = intent.getIntExtra(PhotoIntentService.WIDTH, 300); // gets the place id from the intent
        height = intent.getIntExtra(PhotoIntentService.HEIGHT, 300); // gets the place id from the intent

        String id = intent.getStringExtra(PLACE_ID); // gets the place id from the intent
        return id;
    }

    void getPhoto(String placeId) {
        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);

        // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);

        PlacesClient placesClient = Places.createClient(this);

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            Place place = response.getPlace();


            final List<PhotoMetadata> photoMetadatas = place.getPhotoMetadatas();
            if(photoMetadatas==null){
                //insert a null into the db
                //PlacePhoto placePhoto = new PlacePhoto(placeId, null);
                Log.i(TAG, "Place photo not found: ");
                //mPoiRepository.insertPhoto(placePhoto);
                return;
            }
            // Get the photo metadata.
            PhotoMetadata photoMetadata = photoMetadatas.get(0);

            // Get the attribution text.
            String attributions = photoMetadata.getAttributions();

            // Create a FetchPhotoRequest.
            FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(width)   // Optional.
                    .setMaxHeight(height)  // Optional.
                    .build();
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                PlacePhoto placePhoto = new PlacePhoto(placeId, bitmap);
                mPoiRepository.insertPhoto(placePhoto);
                Log.i(TAG, String.format("Added photo %d, %d : ", bitmap.getWidth(),bitmap.getHeight()));

                //imageView.setImageBitmap(bitmap);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
                    // Handle error with given status code.
                    Log.i(TAG, "Place photo not found: ");
                    PlacePhoto placePhoto = new PlacePhoto(placeId, null);

                }
            });
        });
    }
}
