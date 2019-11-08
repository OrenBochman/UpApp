package org.bochman.upapp.masterDetail;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.bochman.upapp.R;
import org.bochman.upapp.api.PoiIntentService;
import org.bochman.upapp.data.enteties.Poi;
import org.bochman.upapp.data.viewmodel.PoiViewModel;
import org.bochman.upapp.settings.SettingsActivity;
import org.bochman.upapp.utils.Debug;
import org.bochman.upapp.utils.LocationUtils;
import org.bochman.upapp.utils.PlacesUtils;
import org.bochman.upapp.utils.SpUtils;
import org.bochman.upapp.wifi.ConnectivityWatcher;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static org.bochman.upapp.utils.SpUtils.getLastSearch;
import static org.bochman.upapp.utils.SpUtils.setLastSearch;

/**
 * An activity representing a list of Places. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link POIDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class POIMasterActivity extends AppCompatActivity implements OnMapReadyCallback {


    /**
     * the list of places of interest
     */
    private final List<Poi> placesList = new ArrayList<>();
    POIsAdapter adapter;                    // the places adapter
    RecyclerView recyclerView;

    Button searchButton;
    Button nearbyButton;
    EditText queryText;


    private GoogleMap mMap;
    Poi poi;

    private PoiViewModel mPoiViewModel;

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
    /** Flag indicating whether a the search query should be used in the search */

    private  boolean useQuery=false;
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
        //placesList.add(new Poi("123", "Name", 0.0, 0.0, "Address", "0544320000", "http://goggle.com", 4.0f));
        String query = getLastSearch(this);
        queryText = findViewById(R.id.editTextQuery);
        searchButton = findViewById(R.id.buttonSearch);
        nearbyButton = findViewById(R.id.buttonNearBy);

        // Feature: restoring saved search.
        queryText.setText(query);
        searchButton.setOnClickListener(v -> {
            useQuery=true;
            this.PoiSearch(true);

        });

        nearbyButton.setOnClickListener(v -> {
            useQuery=false;
            this.PoiSearch(false);

        });

        LocationUtils locationutils = new LocationUtils(getApplicationContext());
        if (findViewById(R.id.map) != null) {
       // if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.

            mTwoPane = true;
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            assert mapFragment != null;
            mapFragment.getMapAsync(this);
        }
        myContext = this;
        recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new POIsAdapter(this, placesList, mTwoPane);
        recyclerView.setAdapter(adapter);

        // get the ViewModel and observe changes to the poi list
        mPoiViewModel = new ViewModelProvider(this).get(PoiViewModel.class);
        mPoiViewModel.getAllPois().observe(this, new Observer<List<Poi>>() {
            @Override
            public void onChanged(@Nullable final List<Poi> pois) {
                // Update the cached copy of the words in the adapter.
                adapter.setData(pois);
            }
        });
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
                Looper.myLooper());     //TODO: change looper to work off main thread!
    }

    /**
     * Handle location updates.
     * <p>
     * Store the new location in SharedPreferences for distance calculations, etc.
     *
     * @param location location update
     */
    private void onLocationChanged(Location location) {
        SpUtils.setLat(location.getLatitude(), this);
        SpUtils.setLng(location.getLongitude(), this);
    }

//    /**
//     * TODO: remove this method.
//     *
//     * @param query
//     */
//    void PoiSearchOld(String query) {
//        // get places nearby
//        placesUtils = PlacesUtils.getInstance(getApplicationContext());
//        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            placesUtils.fetchCurrentPlaces(placesList);
//
//        } else {
//            getLocationPermission();
//            Toast.makeText(this, "Permission Issue in PoiSearch", Toast.LENGTH_LONG).show();
//        }
//    }


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
        registerReceiver(connectivityWatcher, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    /**
     *
     */
    private ConnectivityWatcher connectivityWatcher = new ConnectivityWatcher();

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

    /** actual search request */
    private void PoiSearchQuery() {

        Intent intent = new Intent(this, PoiIntentService.class);
        String query = useQuery ? queryText.getText().toString() : "";
        intent.putExtra(PoiIntentService.QUERY, query);
        startService(intent);
    }
    /** check permission and make request request */
    protected void PoiSearch(boolean useQuery){

        this.useQuery=useQuery;
        hideSoftKeyBoard();
        // get places nearby
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            PoiSearchQuery();
        } else {
            Log.e(Debug.getTag(), "ACCESS_FINE_LOCATION permissions is not granted - asking for permission");

            getLocationPermission(ACCESS_FINE_LOCATION);
        }
    }
    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    //// Permissions //////////////////////////////////////////////////////////////////////////////

    /** make a permission request
     *
     * @param Permission - permission like ACCESS_FINE_LOCATION
     */

    void getLocationPermission(String Permission) {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Permission) != PackageManager.PERMISSION_GRANTED) {
            Log.e(Debug.getTag(), String.format("getLocationPermission() - Permission %s not granted !",Permission));
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Permission)) {
                Log.e(Debug.getTag(),  String.format("Permission %s not granted - explanation required",Permission));
                showMessageOKCancel("The app needs location access to find near by places.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                            LOCATION_PERMISSION_REQUEST_CODE);
                                }
                            }
                        });

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                Log.e(Debug.getTag(), String.format("Permission %s not granted - explanation not required",Permission));
                ActivityCompat.requestPermissions(this, new String[]{Permission}, LOCATION_PERMISSION_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            //placesUtils.fetchCurrentPlaces(placesList);
            Log.e(Debug.getTag(), "permission already granted !");


        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(POIMasterActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    /** callback for permission requests */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {// If request is cancelled, the result arrays are empty.
            if (permissions.length == 1 &&
                    permissions[0].equals(ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //do the search again!
                Log.e(Debug.getTag(), String.format("Permission %s granted - running search",ACCESS_FINE_LOCATION));
                PoiSearchQuery();
            } else {
                Log.e(Debug.getTag(), "permissions.length=" + permissions.length);
                Log.e(Debug.getTag(), "permissions[0]=" + permissions[0]);
                Log.e(Debug.getTag(), "grantResults[0]=" + PackageManager.PERMISSION_GRANTED);
                Toast.makeText(this, "Permission Denied - Search Not available", Toast.LENGTH_LONG).show();
            }
        }
    }

    //// END Permissions Section //////////////////////////////////////////////////////////////////

    /**
     * handle menu events.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.places_list_activity_menu, menu);
        MenuItem preferencesItem = menu.findItem(R.id.action_preferences);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_preferences:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (this.poi!=null) {
            // Add a marker in Sydney and move the camera
            LatLng telAviv = new LatLng(poi.lat, poi.lng);
            mMap.addMarker(new MarkerOptions().position(telAviv).title(poi.name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(telAviv, 16));
            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
        }
    }

    public void updateMap() {
        if (this.poi!=null) {
            // Add a marker in Sydney and move the camera
            LatLng telAviv = new LatLng(poi.lat, poi.lng);
            mMap.addMarker(new MarkerOptions().position(telAviv).title(poi.name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(telAviv, 16));
            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
        }
    }
} // POIListActivity :-}8
