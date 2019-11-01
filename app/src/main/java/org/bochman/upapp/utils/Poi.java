package org.bochman.upapp.utils;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Objects;

/**
 * Place of Interst pojo
 */
@Parcel
public class Poi {

    public String name;
    public String id;
    public double lat;
    public double lng;
    public String address;

    @ParcelConstructor
    public Poi(String id, String name, String address, double lat, double lng){
        this.id=id;
        this.name=name;
        this. address=address;
        this. lat = lat;
        this. lng = lng;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poi poi = (Poi) o;
        return Double.compare(poi.lat, lat) == 0 &&
                Double.compare(poi.lng, lng) == 0 &&
                name.equals( poi.name) &&
                id.equals( poi.id) &&
                address.equals( poi.address);
    }

    @Override
    public int hashCode() {
    int hash = 7;

        hash =31 * hash +  name.hashCode();
        hash =31 * hash + id.hashCode();
        hash =31 * hash + address.hashCode();
        hash =31 * hash + (int) Math.round(lat*1000);
        hash =31 * hash +  (int) Math.round(lng*1000);

        return hash;
    }

    @Override
    public String toString() {
        return "Poi{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", address='" + address + '\'' +
                '}';
    }
}
