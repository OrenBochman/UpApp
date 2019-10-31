package org.bochman.upapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.bochman.upapp.dummy.DummyContent;
import org.bochman.upapp.utils.Debug;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Favourites.
 */
public class FavouritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        Toolbar toolbar = findViewById(R.id.favouritesToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.places_list_activity_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        MenuItemCompat.getActionProvider(searchItem);

        return true;
    }

    FavouritesActivity.SimpleItemRecyclerViewAdapter adapter;
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter= new FavouritesActivity.SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS);
        recyclerView.setAdapter(adapter);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<FavouritesActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final FavouritesActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
//                if (mTwoPane) {
//                    Bundle arguments = new Bundle();
//                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
//                    ItemDetailFragment fragment = new ItemDetailFragment();
//                    fragment.setArguments(arguments);
//                    mParentActivity.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.item_detail_container, fragment)
//                            .commit();
//                } else {
//                    Context context = view.getContext();
//                    Intent intent = new Intent(context, ItemDetailActivity.class);
//                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);
//
//                    context.startActivity(intent);
//                }
            }
        };

        SimpleItemRecyclerViewAdapter(FavouritesActivity parent,
                                      List<DummyContent.DummyItem> items) {
            mValues = items;
            mParentActivity = parent;

        }

        @NotNull
        @Override
        public FavouritesActivity.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new FavouritesActivity.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final FavouritesActivity.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);
            //TODO set the values of the additional columns here
            holder.itemView.setTag(mValues.get(position));
            //set the click handlers
            holder.itemView.setOnClickListener(mOnClickListener);

        }

        @Override
        public int getItemCount() {
            return mValues.size();

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