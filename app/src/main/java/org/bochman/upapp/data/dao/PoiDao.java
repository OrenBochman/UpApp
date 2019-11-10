package org.bochman.upapp.data.dao;

import org.bochman.upapp.data.enteties.Poi;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PoiDao {

    @Query("Select * From Poi where id = :id")
    Poi getById(String id);

    @Query("SELECT * from Poi where isDeleted = 0")
    LiveData<List<Poi>> getAllPlaces();

    @Query("SELECT * from Poi where isFavourite = 1")
    LiveData<List<Poi>> getAllFavourites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Poi poi);

    @Query("Update Poi set isDeleted=1 where id Like :id")
    int delete(String id);

    @Query("Update Poi set isFavourite=0 where isFavourite = 1 ")
    int deleteAllFavourites();

    @Query("Update Poi set isDeleted=1")
    int deleteAllPoi();

    @Update
    public int updatePoi(Poi... poi);

}
