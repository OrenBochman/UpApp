package org.bochman.upapp.data.enteties;

import android.graphics.Bitmap;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * Photo POJO
 */
@Entity
@Parcel
public class PlacePhoto {

    @NonNull
    @ColumnInfo
    @PrimaryKey(autoGenerate=false)
    public String id;
    @ColumnInfo
    public Bitmap bitmap;

    @ParcelConstructor
    public PlacePhoto(@NonNull String id, Bitmap bitmap) {
        this.id = id;
        this.bitmap = bitmap;
    }
}
