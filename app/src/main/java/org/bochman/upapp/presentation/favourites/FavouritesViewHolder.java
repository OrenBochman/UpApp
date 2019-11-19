package org.bochman.upapp.presentation.favourites;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.bochman.upapp.R;

class FavouritesViewHolder extends RecyclerView.ViewHolder {
    final TextView mNameView;
    final TextView mAddressView;
    final TextView mDistance;
    final ImageView mPhoto;
    public RatingBar ratingBar;

    FavouritesViewHolder(View view) {
        super(view);
        mNameView =  view.findViewById(R.id.name);
        mAddressView = view.findViewById(R.id.address);
        mDistance=view.findViewById(R.id.distance);
        mPhoto=view.findViewById(R.id.photo);
        ratingBar =view.findViewById(R.id.rating);
        //favourite button and share.

    }

}
