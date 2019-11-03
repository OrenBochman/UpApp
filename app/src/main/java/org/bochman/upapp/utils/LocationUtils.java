package org.bochman.upapp.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.RectangularBounds;

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
    public LocationUtils(Context ctx){
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
                location.getLatitude() + "," +
                location.getLongitude();
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
    public static String calcDistance(LatLng source, LatLng destination, boolean metric ) {
        Location lSource = new Location(LocationManager.GPS_PROVIDER);
        lSource.setLatitude(source.latitude);
        lSource.setLongitude(source.longitude);
        Location lDestination = new Location(LocationManager.GPS_PROVIDER);
        lDestination.setLatitude(destination.latitude);
        lDestination.setLongitude(destination.longitude);

        float meters = lSource.distanceTo(lDestination);

        if (metric)
            if(meters<1000)
                return Double.toString(Math.round(meters))+" m";
            else
                return Double.toString(Math.round(meters/1000))+" km";
        else
            return  Double.toString(Math.round(meters/1609.344))+ " ml";
    }

    /**
     * bounding box using spherical approximation
     * @param location location
     * @param radius distance in km say 50
     * @return
     */
    RectangularBounds calcBounds(LatLng location, double radius){

        double R = 6371;  // earth radius in km

        //double radius = 50; // km
        final double width = Math.toDegrees(radius / R / Math.cos(Math.toRadians(location.latitude)));
        final double height =  Math.toDegrees(radius/R);

        double x1 = location.longitude - width;
        double x2 = location.longitude + width;
        double y1 = location.latitude + height;
        double y2 = location.latitude - height;

        RectangularBounds bounds = RectangularBounds.newInstance( new LatLng(x1, y1), new LatLng(x2,y2));

        return bounds;

    }

}
