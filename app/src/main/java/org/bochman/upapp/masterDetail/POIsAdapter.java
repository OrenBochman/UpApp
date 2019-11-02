package org.bochman.upapp.masterDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.bochman.upapp.favourites.FavouritesActivity;
import org.bochman.upapp.R;
import org.bochman.upapp.utils.Debug;
import org.bochman.upapp.data.enteties.Poi;
import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

/**
 *  This is the adapter for the places' recyclerview (Boilerplate from a wizard).
 *
 *  It should move to its own file - only it accesses the fragment in onClick -- Bummer therefore
 *
 *  It could share an parent with the favourites' recyclerview.
 */
public class POIsAdapter extends RecyclerView.Adapter<POIViewHolder> {

    private final POIMasterActivity mParentActivity;
    private final List<Poi> mValues;
    //private final List<Poi> mValuesFiltered;
    private final boolean mTwoPane;

    POIsAdapter(POIMasterActivity parent,
                List<Poi> items,
                boolean twoPane) {
        mValues = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
       // mValuesFiltered = new ArrayList<>();
       // if(items!=null)
       //     mValuesFiltered.addAll(items);

    } // SimpleItemRecyclerViewAdapter [:-}~

    //// adapter overrides ////////////////////////////////////////////////////////////////////

    @NotNull
    @Override
    public POIViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vholder_poi_item, parent, false);
        return new POIViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final POIViewHolder holder, int position) {
        holder.mNameView.setText(mValues.get(position).name);
        holder.mAddressView.setText(mValues.get(position).address);
        holder.mDistance.setText("20");
        //holder.mPhoto.setImageBitmap()); // TODO: add a method to fetch the photo via the api
        holder.itemView.setTag(mValues.get(position));
        //set the click handlers
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setOnLongClickListener(mOnLongClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    //// adapter event handlers ///////////////////////////////////////////////////////////////

    // the click listener
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(Debug.getTag(), "OnClick");
            Poi item = (Poi) view.getTag();
            if (mTwoPane) {
                // replace the current fragment with a new fragment with required item.
                Bundle arguments = new Bundle();
                arguments.putString(POIDetailFragment.ARG_ITEM_ID, item.id);
                POIDetailFragment fragment = new POIDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                // pass the item to POIDetailActivity with the intent.
                Context context = view.getContext();
                Intent intent = new Intent(context, POIDetailActivity.class);
                intent.putExtra(POIDetailFragment.ARG_ITEM_ID, Parcels.wrap(item) );

                context.startActivity(intent);
            }
        }
    };

    // the long click lister
    private final View.OnLongClickListener mOnLongClickListener = view -> {
        Log.i(Debug.getTag(), "OnLongClick");
        Poi item = (Poi) view.getTag();
        //add to favourites by passing the item's id with the intent.
        Context context = view.getContext();
        Intent intent = new Intent(context, FavouritesActivity.class);
        intent.putExtra(FavouritesActivity.POI_KEY, Parcels.wrap(item) );

        context.startActivity(intent);
        return true;
    };

} // POIsAdapter :-}8
