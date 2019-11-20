package org.bochman.upapp.api;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.bochman.upapp.data.enteties.PlacePhoto;
import org.bochman.upapp.data.enteties.Poi;
import org.bochman.upapp.data.repository.PoiRepository;
import org.bochman.upapp.utils.Debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

/**
 *
 */
public class PhotoIntentService extends IntentService {

    public static final String TAG = PhotoIntentService.class.getSimpleName();
    public IPhotoStorageStrategy internalStorageStrategy = new InternalStorageStrategy();
    public IPhotoStorageStrategy externalStorageStrategy = new ExternalStorageStrategy();
    public IPhotoStorageStrategy roomStorageStrategy = new RoomStorageStrategy();

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
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mPoiRepository = new PoiRepository(getApplication());
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
        // TODO: check that the photo is available.

        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);

        // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);

        PlacesClient placesClient = Places.createClient(this);

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            //response handler is in a new thread.
            new Thread(() -> {

                Place place = response.getPlace();

                final List<PhotoMetadata> photoMetadatas = place.getPhotoMetadatas();
                if (photoMetadatas == null) {
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
                    //this response handler is also on a new thread.
                    new Thread(() -> {
                        Bitmap bitmap = fetchPhotoResponse.getBitmap();
                        PlacePhoto placePhoto = new PlacePhoto(placeId, bitmap);

                        //save photo to file
                        String path = savePhotoToInternalStorage(bitmap, placeId);

                        if (path.length() > 0) {

                            Poi poi = mPoiRepository.getPoi(placeId);

                            poi.photoUri = path;
                            mPoiRepository.insert(poi);
                            mPoiRepository.insertPhoto(placePhoto); //TODO: remove once loading from file.
                            Log.i(TAG, String.format("Added photo %d, %d : ", bitmap.getWidth(), bitmap.getHeight()));

                        }
                    }).start();

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
            }).start();
        });
    }


    /**
     * check if file exists in internal storage.
     *
     * @param placeId the placeID
     * @return if file exists
     */
    private Boolean isPhotoInInternalStorage(String placeId) {

        File storagePath = Environment.getDataDirectory();
        String path = storagePath + "/" + placeId + ".webp";
        File imageFile = new File(path);
        return imageFile.exists();
    }

    /**
     * store bitmap as webp.
     *
     * @param bitmap  the bitmap to store
     * @param placeId the placeId
     * @return destination filename to put in the db.
     */
    private String savePhotoToInternalStorage(Bitmap bitmap, String placeId) {
        if (bitmap == null || placeId == null)
            return "";
        // Use Activity method to create a file in the writeable directory
        try {
            File storagePath = Environment.getDataDirectory();
            String path = /**storagePath + "_" +*/placeId + ".webp";
            Log.e(TAG, String.format("savePhotoToInternalStorage, saving photo id:%s, to file to : %s", placeId, path));

            FileOutputStream out = openFileOutput(path, MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, out);
            out.close();
            Log.i(TAG, String.format("savePhotoToInternalStorage, saved file to: %s", path));
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * private String savePhotoToExternalStorage(Bitmap bitmap, String placeId) {
     * Log.i(TAG, String.format("savePhotoToExternalStorage, %s : ", placeId));
     * ContextWrapper apc = (ContextWrapper) this.getApplicationContext();
     * if (
     * Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
     * apc.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
     * apc.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
     * ) {
     * // Permission is granted
     * Log.i(TAG, String.format("savePhotoToExternalStorage, %s : ", "read/write permissions granted"));
     * }else{
     * Log.i(TAG, String.format("savePhotoToExternalStorage, %s : ", "read/write permissions note granted"));
     * <p>
     * }
     * <p>
     * <p>
     * String result="";
     * File storagePath = Environment.getExternalStorageDirectory();
     * FileOutputStream out = null;
     * try {
     * String path = storagePath + "/" + placeId+".webp";
     * out = new FileOutputStream(path);
     * bitmap.compress(Bitmap.CompressFormat.WEBP, 100, out);
     * out.close();
     * } catch (FileNotFoundException e) {
     * Log.e(Debug.getTag(),"",e);
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * return result;
     * }
     * <p>
     * /// Checks if external storage is available for read and write
     * public boolean isExternalStorageWritable() {
     * String state = Environment.getExternalStorageState();
     * if (Environment.MEDIA_MOUNTED.equals(state)) {
     * return true;
     * }
     * return false;
     * }
     * <p>
     * /// Checks if external storage is available to at least read
     * public boolean isExternalStorageReadable() {
     * String state = Environment.getExternalStorageState();
     * if (Environment.MEDIA_MOUNTED.equals(state) ||
     * Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
     * return true;
     * }
     * return false;
     * }
     **/

    interface IPhotoStorageStrategy {

        boolean isAvailable(String id);

        String save(Bitmap bitmap, String id);

        Bitmap load(String id);

        Uri share(String id);   //this should not be here as there is just one strategy gor this.

        void delete(String id);
    }

    /**
     * store photo using internal storage.
     */

    class InternalStorageStrategy implements IPhotoStorageStrategy {

        @Override
        public boolean isAvailable(String id) {
            File storagePath = Environment.getDataDirectory();
            String path = storagePath + "/" + id + ".webp";
            File imageFile = new File(path);
            return imageFile.exists();
        }

        @Override
        public String save(Bitmap bitmap, String id) {
            return null;
        }

        @Override
        public Bitmap load(String id) {
            return null;
        }

        @Override
        public Uri share(String id) {
            return null;
        }

        @Override
        public void delete(String id) {

        }
    }

    private class ExternalStorageStrategy implements IPhotoStorageStrategy {
        @Override
        public boolean isAvailable(String id) {
            File storagePath = Environment.getExternalStorageDirectory();
            String path = storagePath + "/" + id + ".webp";
            File imageFile = new File(path);
            return imageFile.exists();
        }

        @Override
        public String save(Bitmap bitmap, String id) {

            return null;
        }

        @Override
        public Bitmap load(String id) {
            return null;
        }

        @Override
        public Uri share(String id) {
            return null;
        }

        @Override
        public void delete(String id) {

        }
    }


    private class RoomStorageStrategy implements IPhotoStorageStrategy {
        @Override
        public boolean isAvailable(String id) {
            return mPoiRepository.getPhotoCount(id) > 0;
        }

        @Override
        public String save(Bitmap bitmap, String id) {
            mPoiRepository.insertPhoto(new PlacePhoto(id, bitmap));
            return id;
        }

        @Override
        public Bitmap load(String id) {
            return mPoiRepository.getPhoto(id).getValue();
        }

        @Override
        public Uri share(String id) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                return shareNPlus(id);
            else
                return sharePreN(id);
        }

        private Uri shareNPlus(String id) {
            assert Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;

            Bitmap bmp = load(id);
            Uri bmpUri = null;

            try {
                // Use methods on Context to access package-specific directories on external storage.
                // This way, you don't need to request external read/write permission.
                // See https://youtu.be/5xVh-7ywKpE?t=25m25s
                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
                FileOutputStream out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();

                // wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
                bmpUri = FileProvider.getUriForFile(PhotoIntentService.this, "org.bochman.fileprovider", file);  // use this version for API >= 24

                // **Note:** For API < 24, you may use bmpUri = Uri.fromFile(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmpUri;
        }

        /**
         * in pre N: Uri.fromFile(file) works
         *
         * @param id
         * @return
         */
        private Uri sharePreN(String id) {
            assert Build.VERSION.SDK_INT < Build.VERSION_CODES.N;

            Bitmap bmp = load(id);
            Uri bmpUri = null;

            try {
                // Use methods on Context to access package-specific directories on external storage.
                // This way, you don't need to request external read/write permission.
                // See https://youtu.be/5xVh-7ywKpE?t=25m25s
                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
                FileOutputStream out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.

                bmpUri = Uri.fromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmpUri;
        }

        @Override
        public void delete(String id) {
            mPoiRepository.delete(id);
        }
    }
}
