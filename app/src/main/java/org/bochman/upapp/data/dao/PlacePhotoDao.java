package org.bochman.upapp.data.dao;

import org.bochman.upapp.data.enteties.PlacePhoto;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PlacePhotoDao {

    @Query("Select * From PlacePhoto where id = :id")
    PlacePhoto getPhoto(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertPhoto(PlacePhoto placePhoto);

    @Delete
    int deletePhoto(PlacePhoto photo);

    @Query("Delete From PlacePhoto where id = :id")
    int deletePhotoById(String id);

}
