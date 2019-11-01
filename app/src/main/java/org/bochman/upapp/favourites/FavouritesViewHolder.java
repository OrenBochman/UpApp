package org.bochman.upapp.favourites;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.bochman.upapp.R;

class FavouritesViewHolder extends RecyclerView.ViewHolder {
    final TextView mNameView;
    final TextView mAddressView;
    final TextView mDistance;
    final ImageView mPhoto;
    final Button mButton;

    FavouritesViewHolder(View view) {
        super(view);
        mNameView =  view.findViewById(R.id.name);
        mAddressView = view.findViewById(R.id.address);
        mDistance=view.findViewById(R.id.distance);
        mPhoto=view.findViewById(R.id.photo);
        mButton=view.findViewById(R.id.share);

        //favourite button and share.

    } // Viewholder [:-}~

} // Viewholder :-}8
