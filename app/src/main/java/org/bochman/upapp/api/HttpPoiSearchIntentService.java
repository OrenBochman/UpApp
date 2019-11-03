package org.bochman.upapp.api;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.bochman.upapp.BuildConfig;
import org.bochman.upapp.UpApp;
import org.bochman.upapp.data.enteties.Poi;
import org.bochman.upapp.utils.SpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * Deprecated api in use here.
 */
public class HttpPoiSearchIntentService extends IntentService {

    public static final String ACTION_JSON = "action_json";
    String TAG = PoiIntentService.class.getSimpleName();

    final static String API_KEY = BuildConfig.google_maps_key;

    public HttpPoiSearchIntentService() {
        super("SearchIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String keyword = intent.getStringExtra("keyword"); // gets the search keyword from intent in Results Frag
        if (keyword.equals("")) { // * for general search that returns results of all places around not by specific keyword
            keyword = "";
            Log.i(TAG, "General search");
        } else {
            Log.i(TAG, "Search for " + keyword);
        }
        float userLat = (float) SpUtils.getLat(this);
        float userLng = (float) SpUtils.getLng(this);
        String radius = "1000";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + userLat + "," + userLng + "&radius=" + radius + "&keyword=" + keyword + "&key=" + API_KEY + "\n").build();
        Log.i(TAG, "maling request: " + request.url());
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.i(TAG, "Parsing response");
                String result = response.body().string();

                JSONObject rootObj = new JSONObject(result);
                JSONArray array = rootObj.getJSONArray("results");

                ((UpApp) getApplicationContext()).getPoiDatabase().poiDao().deleteAllPoi();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject place = array.getJSONObject(i);
                    String placeId = place.getString("place_id");
                    String name = place.getString("name");
                    String address = place.getString("vicinity");
                    float rating = 0;
                    if (place.has("rating")) {
                        rating = (float) place.getDouble("rating");
                    }
                    double lat = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    double lng = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                    request = new Request.Builder().url("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + keyword + "&fields=formatted_phone_number,website&key=" + API_KEY + "\n").build();

                    Response response2 = client.newCall(request).execute();
                    String result2 = response2.body().string();

                    JSONObject rootObj2 = new JSONObject(result2);
                    JSONObject obj = rootObj2.getJSONObject("result");

                    String phone = "phone not found"; // default in case there is no phone number
                    if (obj.has("formatted_phone_number")) { // check if there is a phone number
                        phone = obj.getString("formatted_phone_number");
                    }

                    String website = "website not found"; // default in case there is no website
                    if (obj.has("website")) { // check if there is a website
                        website = obj.getString("website");
                    }

                    Log.i(TAG, "Parsing done: ");

                    // insert the search results to DB

                    Poi poi = new Poi(placeId, name, lat, lng, address, phone, website, rating);
                    ((UpApp) getApplicationContext()).getPoiDatabase().poiDao().insert(poi);
                    Log.i(TAG, "Inserting item +" + i + "+into DB: " + poi.toString());
                }
                Intent broadcastIntent = new Intent(ACTION_JSON);

                // send the broadcast to the receiver in Results Frag in order to notify the adapter that there are new places in the DB
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

            } else { // in case the response is unsuccessful - no results from JSON
                Log.i(TAG, "error: no results: ");
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "error: no results", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (IOException e) {
            Log.e(TAG, "IO Exception", e);
            // e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
            // e.printStackTrace();
        }
    }
}
