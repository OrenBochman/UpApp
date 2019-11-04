package org.bochman.upapp.favourites;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.bochman.upapp.masterDetail.POIMasterActivity;
import org.bochman.upapp.R;
import org.bochman.upapp.settings.SettingsActivity;
import org.bochman.upapp.utils.Debug;
import org.bochman.upapp.data.enteties.Poi;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Favourites.
 */
public class FavouritesActivity extends AppCompatActivity {

    /** the list of favourites. */
    private final List<Poi> favouritesList = new ArrayList<>();
    FavouritesAdapter adapter;
    RecyclerView recyclerView;
    /**  the key used by POIListActivity for passing a favourite via an intent */
    public static final String POI_KEY = "POI";
    Poi poi;
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
        Poi poi = Parcels.unwrap(getIntent()
                .getParcelableExtra(FavouritesActivity.POI_KEY) );

        Log.i(Debug.getTag(),"POI passed = "+ poi.toString());
        //fetch the list from
        favouritesList.add(poi);

        recyclerView = findViewById(R.id.favouritesRecyclerView);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavouritesAdapter(this, favouritesList);
        recyclerView.setAdapter(adapter);
    }


    /**
     * handle menu events.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //using the same menue as master activity.
        getMenuInflater().inflate(R.menu.places_list_activity_menu, menu);
        MenuItem preferencesItem = menu.findItem(R.id.action_preferences);
        return true;
    }

    /**
     * Menu click handler.
     *
     * Navigate back to parent activity.
     *
     * @param item - the menu item being accessed
     * @return if handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
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