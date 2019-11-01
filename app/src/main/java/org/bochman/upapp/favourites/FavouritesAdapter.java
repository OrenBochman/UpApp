package org.bochman.upapp.favourites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.bochman.upapp.R;
import org.bochman.upapp.utils.Poi;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesViewHolder> {

    private final FavouritesActivity mParentActivity;
    private final List<Poi> mValues;



    FavouritesAdapter(FavouritesActivity parent,
                      List<Poi> items) {
        mValues = items;
        mParentActivity = parent;

    }

    @NotNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vholder_favourite_item, parent, false);
        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavouritesViewHolder holder, int position) {
        holder.mNameView.setText(mValues.get(position).name);
        holder.mAddressView.setText(mValues.get(position).address);
        holder.mDistance.setText("20km");  // TODO: calculate the distance
        //holder.mPhoto.setImageBitmap()); // TODO: add a method to fetch the photo for by via the places api + show via glide/etc
        holder.itemView.setTag(mValues.get(position));
        //set the click handlers
        holder.itemView.setOnClickListener(mOnClickListener);

    }

    @Override
    public int getItemCount() {
        return mValues.size();

    }

    private final View.OnClickListener mOnClickListener = view -> {
        Poi item = (Poi) view.getTag();
//                if (mTwoPane) {
//                    Bundle arguments = new Bundle();
//                    arguments.putString(POIDetailFragment.POI_KEY, item.id);
//                    POIDetailFragment fragment = new POIDetailFragment();
//                    fragment.setArguments(arguments);
//                    mParentActivity.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.item_detail_container, fragment)
//                            .commit();
//                } else {
//                    Context context = view.getContext();
//                    Intent intent = new Intent(context, POIDetailActivity.class);
//                    intent.putExtra(POIDetailFragment.POI_KEY, item.id);
//
//                    context.startActivity(intent);
//                }
    };


}
