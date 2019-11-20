package org.bochman.upapp.data.dao;

import android.graphics.Bitmap;

import org.bochman.upapp.data.enteties.PlacePhoto;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PlacePhotoDao {

    @Query("Select bitmap From PlacePhoto where id = :id")
    LiveData<Bitmap> getPhoto(String id);

    @Query("Select count(bitmap) From PlacePhoto where id = :id")
    int getPhotoCount(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertPhoto(PlacePhoto placePhoto);

    @Delete
    int deletePhoto(PlacePhoto photo);

    @Query("Delete From PlacePhoto where id = :id")
    int deletePhotoById(String id);

    @Query("Select PlacePhoto.id ,PlacePhoto.bitmap From PlacePhoto Join Poi ON Poi.id = placephoto.id Where Poi.isDeleted == 0 ")
    LiveData<List<PlacePhoto>> getAllPhotos();

    @Query("Select PlacePhoto.id ,PlacePhoto.bitmap From PlacePhoto Join Poi ON Poi.id = placephoto.id Where Poi.isFavourite == 1 ")
    LiveData<List<PlacePhoto>> getAllFavPhotos();


}
