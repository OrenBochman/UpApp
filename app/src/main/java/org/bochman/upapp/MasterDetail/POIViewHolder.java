package org.bochman.upapp.MasterDetail;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.bochman.upapp.R;

class POIViewHolder extends RecyclerView.ViewHolder {

    final TextView mNameView;
    final TextView mAddressView;
    final TextView mDistance;
    final ImageView mPhoto;

    POIViewHolder(View view) {
        super(view);

        mNameView =  view.findViewById(R.id.name);
        mAddressView = view.findViewById(R.id.address);
        mDistance=view.findViewById(R.id.distance);
        mPhoto=view.findViewById(R.id.photo);
    } // Viewholder [:-}~

} // Viewholder :-}8
