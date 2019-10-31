package org.bochman.upapp.utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * pojo
 */
public class Poi {
    String name;
    String id;
    double lat;
    double lng;
    String address;

    Poi(String id, String name, String address, LatLng lat_lng){
        name=name;
        id=id;
        if(lat_lng!=null) {
            lat = lat_lng.latitude;
            lng = lat_lng.latitude;
        }
        address=address;
    }
}
