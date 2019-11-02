package org.bochman.upapp.data.dao;

import org.bochman.upapp.data.enteties.Poi;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PoiDao {

    @Query("Select * From Poi where id = :id")
    public Poi getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertUser(Poi poi);

    @Delete
    public void deleteUser(Poi poi);

    @Query("Delete  From Poi where isFavourite > 0 ")
    public void deleteFavourites();

    @Query("Delete  from Poi where isFavourite = 0 ")
    public void deleteNonFavourites();

}
