package org.bochman.upapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javafaker.App;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.bochman.upapp.dummy.DummyContent;
import org.bochman.upapp.utils.Debug;
import org.bochman.upapp.utils.LocationUtils;
import org.bochman.upapp.utils.PlacesUtils;
import org.bochman.upapp.utils.Poi;
import org.bochman.upapp.utils.SharedPreferencesUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * An activity representing a list of Places. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PlacesListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    //places singlton
    private PlacesUtils placesUtils;

    Context myContext;

    /**
     * The FusedLocation client.
     *
     */
    private FusedLocationProviderClient fusedLocationClient;

    /** Activity entry point.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        LocationUtils locationutils = new LocationUtils(getApplicationContext());

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        myContext=this;

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;

        // Initialize FusedLocation APIs
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        String query= SharedPreferencesUtils.getLastSearch(this);
        PoiSearch(query);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //32.06684,34.8113214
        setupRecyclerView((RecyclerView) recyclerView);
    }

    void PoiSearch(String query){
        // get places nearby
        placesUtils=PlacesUtils.getInstance(getApplicationContext());
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            placesList = placesUtils.fetchCurrentPlaces();

        }    else {
            getLocationPermission();
            Toast.makeText(this,"Permission Issue in PoiSearch",Toast.LENGTH_LONG).show();

        }
    }

    List<Poi> placesList;

    private Location lastKnownLocation;

    final static int MY_LOCATION_REQUEST_CODE = 1;

    void getLocationPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            placesList = placesUtils.fetchCurrentPlaces();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (permissions.length == 1 &&
                        permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    placesList = placesUtils.fetchCurrentPlaces();
                } else {
                    Log.e(Debug.getTag(),"permissions.length="+permissions.length);
                    Log.e(Debug.getTag(),"permissions[0]="+permissions[0]);
                    Log.e(Debug.getTag(),"grantResults[0]="+ PackageManager.PERMISSION_GRANTED);
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.places_list_activity_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        MenuItemCompat.getActionProvider(searchItem);


        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //text changed apply filtering.

                        if(query.length()==0){
                            //Save search in shared perferences
                            SharedPreferencesUtils.setLastSearch("",myContext);
                            // run a search with the current location.
                            PoiSearch("");
                            //Toast.makeText(getApplicationContext(),"todo: local search ",Toast.LENGTH_SHORT).show();
                        }else{

                            //Save search in shared perferences
                            SharedPreferencesUtils.setLastSearch(query,myContext);
                            // run a search with the current query.
                            PoiSearch(query);
                            //Toast.makeText(getApplicationContext(),"todo: search for "+query,Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        //perform search.
                        Toast.makeText(getApplicationContext(),"todo: filter for "+newText,Toast.LENGTH_SHORT).show();
                        //TODO add a filtering field for the class for use with the listview.
                        adapter.filter(newText);
                        //filter.setLength(0);
                        //filter.append(newText);

                        return false;
                    }
                }
        );

        return true;
    }

    SimpleItemRecyclerViewAdapter adapter;
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter= new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane);
        recyclerView.setAdapter(adapter);
    }

    /**
     *  This is the adapter for the places' recyclerview (Boilerplate from a wizard).
     *
     *  It should move to its own file - only it accesses the fragment in onClick -- Bummer therefore
     *  // TODO: use an interface to access the fragment and then pass it into the constructor.
     *
     *  It could share an parent with the favourites' recyclerview.
     *  // TODO: move filtering to parent and use with both favourites & places.
     */
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView =  view.findViewById(R.id.id_text);
                mContentView =  view.findViewById(R.id.content);
                //TODO add cols for distance, name, address, photo
            } // Viewholder [:-}~

        } // Viewholder :-}8

        private final PlacesListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final List<DummyContent.DummyItem> mValuesFiltered;
        private final boolean mTwoPane;

        SimpleItemRecyclerViewAdapter(PlacesListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            mValuesFiltered= new ArrayList<>();
            mValuesFiltered.addAll(items);

        } // SimpleItemRecyclerViewAdapter [:-}~

        //// adapter overrides ////////////////////////////////////////////////////////////////////

        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValuesFiltered.get(position).id);
            holder.mContentView.setText(mValuesFiltered.get(position).content);
            //TODO set the values of the additional columns here
            holder.itemView.setTag(mValuesFiltered.get(position));
            //set the click handlers
            holder.itemView.setOnClickListener(mOnClickListener);
            holder.itemView.setOnLongClickListener(mOnLongClickListener);
        }

        @Override
        public int getItemCount() {
            return mValuesFiltered.size();

        }

        //// adapter event handlers ///////////////////////////////////////////////////////////////

        // the click listener
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    // replace the current fragment with a new fragment with required item.
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    // pass the item to ItemDetailActivity with the intent.
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        // the long click lister
        private final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();

                Log.i(Debug.getTag(), "Long Click");

                //add to favourites by passing the item's id with the intent.
                Context context = view.getContext();
                Intent intent = new Intent(context, FavouritesActivity.class);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                context.startActivity(intent);
                return true;
            }
        };


        /**
         * Filter and refreshing the adapter.
         *
         * This does not change the data in the adapter.
         *
         * @param filter
         */
        public void filter(String filter){
            mValuesFiltered.clear();
            for (DummyContent.DummyItem item:mValues) {
                if(item.content.matches(".*"+filter+".*"))
                    mValuesFiltered.add(item);
            }
            //can check if we really checked too
            notifyDataSetChanged();
        }

    } // SimpleRecyclerViewAdapter :-}8

    //// Location and places helpers ///////////////////////////////////////////////////////////////






} // PlacesListActivity :-}8
