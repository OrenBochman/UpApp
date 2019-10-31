package org.bochman.upapp.utils;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.RectangularBounds;

import java.util.Dictionary;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class LocationUtils {

   // Context ctx;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000;      /* 2 sec */

    LatLng latLng;

    /**
     *
     * @param ctx - app context is preferred
     */
    LocationUtils(Context ctx){
        startLocationUpdates(ctx);
    }

    // Trigger new location updates at interval
    private void startLocationUpdates(Context ctx) {

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
        SettingsClient settingsClient = LocationServices.getSettingsClient(ctx);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(ctx).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

     void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        //Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    /**
     * Calculates Distances
     *
     * distance is defined using the WGS84 ellipsoid.
     *
     * @param source current location
     * @param destination location of destination
     * @param metric - true for KM, false for Miles
     * @return distance in specified units
     */
    public double calcDistance(Location source, Location destination, boolean metric ){

        float meters = source.distanceTo(destination);
        if (metric)
            return meters/1000.0;
        else
            return meters/1609.344;
    }


    /**
     * bounding box using spherical approximation
     * @param location location
     * @param radius distance in km say 50
     * @return
     */
    RectangularBounds calcBounds(Location location, double radius){

        double R = 6371;  // earth radius in km

        //double radius = 50; // km
        double x1 = location.getLongitude() - Math.toDegrees(radius/R/Math.cos(Math.toRadians(location.getLatitude())));
        double x2 = location.getLongitude() + Math.toDegrees(radius/R/Math.cos(Math.toRadians(location.getLatitude())));
        double y1 = location.getLatitude() + Math.toDegrees(radius/R);
        double y2 = location.getLatitude() - Math.toDegrees(radius/R);

        RectangularBounds bounds = RectangularBounds.newInstance( new LatLng(x1, y1), new LatLng(x2,y2));

        return bounds;

    }

}
