package org.bochman.upapp.masterDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import org.bochman.upapp.favourites.FavouritesActivity;
import org.bochman.upapp.R;
import org.bochman.upapp.utils.Debug;
import org.bochman.upapp.data.enteties.Poi;
import org.bochman.upapp.utils.LocationUtils;
import org.bochman.upapp.utils.SpUtils;
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

    // cached copy of poi data.
    private  List<Poi> mValues;
    // cache for the current location - updated whenever the data is updated.
    private LatLng latlng;
    // cache of flag indicating if master and detail are in the same activity.
    private boolean mTwoPane;
    // reference to the containing activity.
    private POIMasterActivity mParentActivity;

    /**
     * Constructor for the Poi adapter.
     *
     * @param parent - parent activity.
     * @param items - poi data.
     * @param twoPane - flag indicating if master and detail are in the same activity.
     */
    public POIsAdapter(POIMasterActivity parent, List<Poi> items, boolean twoPane) {

        this.mValues = items;
        this.mParentActivity = parent;
        this.mTwoPane = twoPane;
        getLatLng();

    } // SimpleItemRecyclerViewAdapter [:-}~

    //// adapter overrides ////////////////////////////////////////////////////////////////////

    @NonNull
    @Override
    public POIViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vholder_poi_item, parent, false);
        return new POIViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final POIViewHolder holder, int position) {
        Poi poi=mValues.get(position);
        holder.mNameView.setText(poi.name);
        holder.mAddressView.setText(poi.address);
        holder.mDistance.setText(
                LocationUtils.calcDistance(latlng,
                        new LatLng(poi.lat,poi.lng),
                        SpUtils.getIsMetric(mParentActivity.getApplicationContext())==1));



        if (holder.mPhoto != null)
            if( poi.photoUri!=null && !poi.photoUri.isEmpty()) {
            Picasso
                    .get()
                    .load(poi.photoUri)
                    .into(holder.mPhoto);
            }else{
                holder.mPhoto.setImageDrawable(mParentActivity.getResources().getDrawable(R.drawable.ic_photo_black_24));
            }


        holder.itemView.setTag(poi);
        //set the click handlers
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setOnLongClickListener(mOnLongClickListener);
    }

    /**
     * The method for refreshing cached poi following a search.
     *
     * @param data - new poi data
     */
    public void setData(List<Poi> data){
        mValues = data;
        getLatLng();
        notifyDataSetChanged();
    }



    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if(mValues!=null)
            return mValues.size();
        else
            return 0;
    }

    //// adapter event handlers ///////////////////////////////////////////////////////////////

    /**
     *  the click listener.
     */
    private final View.OnClickListener mOnClickListener = view -> {
        Log.i(Debug.getTag(), "OnClick");
        Poi item = (Poi) view.getTag();
        if (mTwoPane) {
            // replace the current fragment with a new fragment with required item.
            mParentActivity.poi=item;
            mParentActivity.updateMap();

        } else {
            // pass the item to POIDetailActivity with the intent.
            Context context = view.getContext();
            Intent intent = new Intent(context, POIDetailActivity.class);
            intent.putExtra(POIDetailActivity.ARG_ITEM_ID, Parcels.wrap(item) );

            context.startActivity(intent);
        }
    };

    /**
     * the long click listener.
     */
    private final View.OnLongClickListener mOnLongClickListener = view -> {
        Log.i(Debug.getTag(), "OnLongClick");
        Poi item = (Poi) view.getTag();

        //creating a popup menu
        PopupMenu popup = new PopupMenu(mParentActivity,view);
        //inflating menu from xml resource
        popup.inflate(R.menu.options_menu);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favourite:                //bookmark
                        //handle menu1 click
                        // add to favourites by passing the item's with the intent.
                        Context context = view.getContext();
                        Intent intent = new Intent(context, FavouritesActivity.class);
                        intent.putExtra(FavouritesActivity.POI_KEY, Parcels.wrap((Poi) view.getTag()) );
                        context.startActivity(intent);

                        break;
                    case R.id.share:
                        // share poi as text using whatapp!

                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, ("UpApp Location := "+ view.getTag().toString()));
                        try {
                            mParentActivity.startActivity(whatsappIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(mParentActivity, R.string.app_not_installed,Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                return false;
            }
        });
        //displaying the popup
        popup.show();


        return true;
    };


    //// utility methods /////////////////////////////////////////////////////////////////////////
    /**
     * helper to get the most recent location
     */
    private void getLatLng(){
        latlng=new LatLng(SpUtils.getLat(mParentActivity.getApplicationContext()),
                SpUtils.getLng(mParentActivity.getApplicationContext()));
    }

} // POIsAdapter :-}8
