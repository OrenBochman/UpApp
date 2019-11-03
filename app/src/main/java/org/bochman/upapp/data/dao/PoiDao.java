package org.bochman.upapp.data.dao;

import org.bochman.upapp.data.enteties.Poi;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PoiDao {

    @Query("Select * From Poi where id = :id")
    public Poi getById(String id);

    @Query("SELECT * from Poi where isFavourite = 0")
    LiveData<List<Poi>> getAllPlaces();

    @Query("SELECT * from Poi where isFavourite = 1")
    LiveData<List<Poi>> getAllFavourites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insert(Poi poi);

    @Delete
    public void delete(Poi poi);

    @Query("Delete  From Poi where isFavourite > 0 ")
    public void deleteAllFavourites();

    @Query("Delete  from Poi where isFavourite = 0 ")
    public void deleteAllPoi();

}
