package org.bochman.upapp.favourites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.bochman.upapp.R;
import org.bochman.upapp.SmartActivity;
import org.bochman.upapp.data.enteties.PlacePhoto;
import org.bochman.upapp.data.enteties.Poi;
import org.bochman.upapp.data.viewmodel.PoiViewModel;
import org.bochman.upapp.masterDetail.POIMasterActivity;
import org.bochman.upapp.settings.SettingsActivity;
import org.bochman.upapp.utils.Debug;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * An activity representing a list of Favourites.
 */
public class FavouritesActivity extends SmartActivity {

    FavouritesAdapter adapter;
    RecyclerView recyclerView;
    /**
     * the key used by POIListActivity for passing a favourite via an intent
     */
    public static final String POI_KEY = "POI";

    Poi poi;
    /**
     * the list of favourites.
     */
    private List<Poi> favouritesList;
    /**
     * the hashmap of photos
     */
    private HashMap<String, Bitmap> photoMap;
    /**
     * the view model
     */
    private PoiViewModel mPoiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        Toolbar toolbar = findViewById(R.id.favouritesToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //get the favourite and add to the list
        Poi poi = Parcels.unwrap(getIntent().getParcelableExtra(FavouritesActivity.POI_KEY));

        Log.i(Debug.getTag(), "POI in intent is = " + poi.toString());


        //get the view model
        mPoiViewModel = new ViewModelProvider(this).get(PoiViewModel.class);
        //add the new favourite to the using the view model.
        mPoiViewModel.insert(poi);
        //get the data from the view model
        final LiveData<List<PlacePhoto>> favImages = mPoiViewModel.getFavImages();
        if (favImages.getValue() != null) {
            photoMap = convertToHashmap(favImages.getValue());
        } else {
            photoMap = new HashMap<>();
        }
        final LiveData<List<Poi>> allFavs = mPoiViewModel.getAllFavs();
        if (favImages.getValue() != null) {
            favouritesList = allFavs.getValue();
        } else {
            favouritesList = new ArrayList<>();
        }

        recyclerView = findViewById(R.id.favouritesRecyclerView);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavouritesAdapter(this, favouritesList, photoMap);
        recyclerView.setAdapter(adapter);


        allFavs.observe(this, new Observer<List<Poi>>() {
            @Override
            public void onChanged(@Nullable final List<Poi> pois) {
                // Update the cached copy of the words in the adapter.
                Log.i(Debug.getTag(), "onChanged: - " + pois.size());
                adapter.setData(pois);
            }
        });

        //observe the photos for changes
        favImages.observe(this, new Observer<List<PlacePhoto>>() {
            @Override
            public void onChanged(@Nullable final List<PlacePhoto> photos) {
                // Update the cached copy of the words in the adapter.
                adapter.setPhotos(convertToHashmap(photos));
            }
        });


        poi.isFavourite = 1;
        mPoiViewModel.update(poi);
    }

    HashMap<String, Bitmap> convertToHashmap(List<PlacePhoto> photos) {
        HashMap<String, Bitmap> photoMap = new HashMap<>();
        for (PlacePhoto photo : photos) {
            photoMap.put(photo.id, photo.bitmap);
        }
        return photoMap;
    }

    /**
     * handle menu events.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //using the same menu as master activity.
        getMenuInflater().inflate(R.menu.places_list_activity_menu, menu);
        MenuItem preferencesItem = menu.findItem(R.id.action_preferences);
        return true;
    }

    /**
     * Menu click handler.
     * <p>
     * Navigate back to parent activity.
     *
     * @param item - the menu item being accessed
     * @return if handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_preferences:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpTo(this, new Intent(this, POIMasterActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}