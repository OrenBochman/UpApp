package org.bochman.upapp.utils;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Place of Interest POJO
 */
@Entity
@Parcel
public class Poi {



    @ColumnInfo
    @PrimaryKey(autoGenerate=false)
    public String id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public double lat;
    @ColumnInfo
    public double lng;
    @ColumnInfo
    public String address;
    @ColumnInfo
    public String phone;
    @ColumnInfo
    public String website;
    @ColumnInfo
    public Integer isFavourite;
    @ColumnInfo
    private float rating;


    @ParcelConstructor
    public Poi(String id, String name, String address, double lat, double lng){
        this.id=id;
        this.name=name;
        this. address=address;
        this.lat = lat;
        this.lng = lng;

    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Poi)) return false;

        Poi poi = (Poi) o;

        if (Double.compare(poi.lat, lat) != 0) return false;
        if (Double.compare(poi.lng, lng) != 0) return false;
        if (Float.compare(poi.rating, rating) != 0) return false;
        if (!id.equals(poi.id)) return false;
        if (name != null ? !name.equals(poi.name) : poi.name != null) return false;
        if (address != null ? !address.equals(poi.address) : poi.address != null) return false;
        if (phone != null ? !phone.equals(poi.phone) : poi.phone != null) return false;
        return website != null ? website.equals(poi.website) : poi.website == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (rating != +0.0f ? Float.floatToIntBits(rating) : 0);
        return result;
    }
}
