package org.bochman.upapp.masterDetail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import org.bochman.upapp.R;
import org.bochman.upapp.api.PoiIntentService;
import org.bochman.upapp.utils.Debug;
import org.bochman.upapp.utils.LocationUtils;
import org.bochman.upapp.utils.PlacesUtils;
import org.bochman.upapp.data.enteties.Poi;
import org.bochman.upapp.utils.SpUtils;
import org.bochman.upapp.wifi.ConnectivityWatcher;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static org.bochman.upapp.utils.SpUtils.*;

/**
 * An activity representing a list of Places. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link POIDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class POIMasterActivity extends AppCompatActivity {

    /**
     * the list of places of interest
     */
    private final List<Poi> placesList = new ArrayList<>();
    POIsAdapter adapter;                    // the places adapter

    Button searchButton;
    Button nearbyButton;
    EditText queryText;


    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    final static int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean mTwoPane;

    /**
     * Places singleton
     */
    private PlacesUtils placesUtils;

    Context myContext;


    /**
     * Activity entry point.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_master);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        startLocationUpdates();
        placesList.add(new Poi("123", "Name",  0.0, 0.0,"Address","0544320000","http://goggle.com",4.0f));
        String query = getLastSearch(this);
        queryText = findViewById(R.id.editTextQuery);
        searchButton = findViewById(R.id.buttonSearch);
        nearbyButton = findViewById(R.id.buttonNearBy);

        // Feature: restoring saved search.
        queryText.setText(query);
        searchButton.setOnClickListener(v -> {

            //Do the search
            Intent intent = new Intent(this, PoiIntentService.class);
            intent.putExtra(PoiIntentService.QUERY, queryText.getText());
            startService(intent);
        });
        SearchView searchView = findViewById(R.id.action_search);
        // searchView.setQuery(query,true);
        PoiSearch(query);

        LocationUtils locationutils = new LocationUtils(getApplicationContext());

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        myContext = this;

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new POIsAdapter(this, placesList, mTwoPane);
        recyclerView.setAdapter(adapter);
    }


    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        long UPDATE_INTERVAL = 10 * 1000;  // 10 secs - put into shared preferences
        long FASTEST_INTERVAL = 2000;      // 2 sec  shared preferences

        LocationRequest mLocationRequest;

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        //store in SharedPreferences for all to use with distance calculations.
        SpUtils.setLat(location.getLatitude(),this);
        SpUtils.setLng(location.getLongitude(),this);

        // New location has now been determined
        StringBuilder msg = new StringBuilder()
                .append("Updated Location: ")
                .append(location.getLatitude())
                .append(",").append(location.getLongitude());
        Log.i(Debug.getTag(),msg.toString());
    }

    /**
     * TODO: figure out how to incorporate query in search.
     *
     * @param query
     */
    void PoiSearch(String query) {
        // get places nearby
        placesUtils = PlacesUtils.getInstance(getApplicationContext());
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            placesUtils.fetchCurrentPlaces(placesList);

        } else {
            getLocationPermission();
            Toast.makeText(this, "Permission Issue in PoiSearch", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * resume lifecycle handler
     * Non-MVP-TODO: test onResume.
     */
    @Override
    public void onResume() {
        super.onResume();

        // Feature: restoring saved search.
        queryText.setText(getLastSearch(this));

        //Feature start wifi monitoring service - will not work for api >= 28 pie
        registerReceiver( connectivityWatcher,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    /**
     *
     */
    private ConnectivityWatcher connectivityWatcher=new ConnectivityWatcher();

    /**
     * pause lifecycle handler
     * Non-MVP-TODO: test onPause.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Feature: saving last search.
        setLastSearch(queryText.getText().toString(), getApplicationContext());

        //Feature: suspend wifi monitoring service
        unregisterReceiver(connectivityWatcher);
    }


    void getLocationPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(Debug.getTag(), "ACCESS_FINE_LOCATION not granted !");
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.e(Debug.getTag(), "ACCESS_FINE_LOCATION not granted !");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            placesUtils.fetchCurrentPlaces(placesList);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {// If request is cancelled, the result arrays are empty.
            if (permissions.length == 1 &&
                    permissions[0].equals(ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                placesUtils.fetchCurrentPlaces(placesList);
            } else {
                Log.e(Debug.getTag(), "permissions.length=" + permissions.length);
                Log.e(Debug.getTag(), "permissions[0]=" + permissions[0]);
                Log.e(Debug.getTag(), "grantResults[0]=" + PackageManager.PERMISSION_GRANTED);
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.places_list_activity_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        MenuItemCompat.getActionProvider(searchItem);


        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //text changed apply filtering.

                        if (query.length() == 0) {
                            //Save search in shared preferences
                            setLastSearch("", myContext);
                            // run a search with the current location.
                            PoiSearch("");
                            //Toast.makeText(getApplicationContext(),"todo: local search ",Toast.LENGTH_SHORT).show();
                        } else {

                            //Save search in shared perferences
                            setLastSearch(query, myContext);
                            // run a search with the current query.
                            PoiSearch(query);
                            //Toast.makeText(getApplicationContext(),"todo: search for "+query,Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        //perform search.
                        Toast.makeText(getApplicationContext(), "todo: filter for " + newText, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
        );

        return true;
    }

} // POIListActivity :-}8
