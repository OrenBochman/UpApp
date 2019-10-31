package org.bochman.upapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import org.bochman.upapp.dummy.DummyContent;
import org.bochman.upapp.utils.Debug;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
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
                            //TODO: get current longitude and lattitude.

                            Toast.makeText(getApplicationContext(),"todo: local search ",Toast.LENGTH_SHORT).show();
                            //TODO: run a retrofit search with the current location.

                        }else{
                            Toast.makeText(getApplicationContext(),"todo: search for "+query,Toast.LENGTH_SHORT).show();
                            //TODO: run a retrofit search with the query.

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


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final PlacesListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final List<DummyContent.DummyItem> mValuesFiltered;
        private final boolean mTwoPane;

        public void filter(String filter){
            mValuesFiltered.clear();
            for (DummyContent.DummyItem item:mValues) {
                if(item.content.matches(".*"+filter+".*"))
                    mValuesFiltered.add(item);
            }
            //can check if we really checked too
            notifyDataSetChanged();

        }

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

        SimpleItemRecyclerViewAdapter(PlacesListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            mValuesFiltered= new ArrayList<>();
            mValuesFiltered.addAll(items);
        }

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

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView =  view.findViewById(R.id.id_text);
                mContentView =  view.findViewById(R.id.content);
                //TODO add additional columns for
                // distance
                // name
                // address &
                // photo
            }
        }
    }
}
