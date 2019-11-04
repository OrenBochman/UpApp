package org.bochman.upapp.favourites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.bochman.upapp.R;
import org.bochman.upapp.data.enteties.Poi;
import org.bochman.upapp.utils.LocationUtils;
import org.bochman.upapp.utils.SpUtils;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesViewHolder> {

    // cached copy of poi data.

    private  List<Poi> mValues;
    // cache for the current location - updated whenever the data is updated.
    private LatLng latlng;
    // reference to the containing activity.
    private  FavouritesActivity mParentActivity;

    FavouritesAdapter(FavouritesActivity parent,
                      List<Poi> items) {
        mValues = items;
        mParentActivity = parent;
        getLatLng();
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vholder_favourite_item, parent, false);
        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavouritesViewHolder holder, int position) {

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

        holder.ratingBar.setRating((float) poi.rating);
        //holder.ratingBar.setEnabled(false);
        holder.ratingBar.setIsIndicator(true);

        holder.itemView.setTag(poi);
        //set the click handlers
        holder.itemView.setOnClickListener(mOnClickListener);

    }

    @Override
    public int getItemCount() {
        return mValues.size();

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

    private final View.OnClickListener mOnClickListener = view -> {
        Poi item = (Poi) view.getTag();
    };

    //// utility methods /////////////////////////////////////////////////////////////////////////
    /**
     * helper to get the most recent location
     */
    private void getLatLng(){
        latlng=new LatLng(SpUtils.getLat(mParentActivity.getApplicationContext()),
                SpUtils.getLng(mParentActivity.getApplicationContext()));
    }

}
