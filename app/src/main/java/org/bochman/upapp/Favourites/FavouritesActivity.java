package org.bochman.upapp.Favourites;

import androidx.annotation.NonNull;
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
import android.view.View;

import org.bochman.upapp.MasterDetail.POIDetailFragment;
import org.bochman.upapp.MasterDetail.POIListActivity;
import org.bochman.upapp.R;
import org.bochman.upapp.utils.Debug;
import org.bochman.upapp.utils.Poi;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Favourites.
 */
public class FavouritesActivity extends AppCompatActivity {

    /** the list of favourites. */
    // TODO load & save to ROOM DBfrom
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.favourites_list_activity_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        MenuItemCompat.getActionProvider(searchItem);

        return true;
    }

    /**
     * Menu click handler.
     *
     * Navigate back to parent activity.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, POIListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}