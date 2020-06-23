package com.michael.afrivac.ui.popular_destination;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.michael.afrivac.R;
import com.michael.afrivac.model.PopularPlaces;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

class PopularDestinationRVAdapter extends RecyclerView.Adapter<PopularDestinationRVAdapter.PopularPlacesRVAdapterVH> {
    private PopularDestinationRVAdapter.OnItemSelectedListener onItemSelectedListener;
    private List<PopularPlaces> popularPlaces;
    private Context context;

    public PopularDestinationRVAdapter(Context context, OnItemSelectedListener onItemSelectedListener) {
        this.context = context;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void setDestinations(List<PopularPlaces> popularPlaces) {
        this.popularPlaces = popularPlaces;
        notifyDataSetChanged();
    }

    class PopularPlacesRVAdapterVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView country, destination, description, ratingNumber, engagement;
        ImageView image;
        ImageButton favoriteIcon;
        RatingBar ratingBar;

        public PopularPlacesRVAdapterVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
            destination = itemView.findViewById(R.id.destination);
            country = itemView.findViewById(R.id.country);
            description = itemView.findViewById(R.id.description);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingNumber = itemView.findViewById(R.id.ratingNumber);
            engagement = itemView.findViewById(R.id.engagement);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemSelectedListener.onItemSelected(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public PopularPlacesRVAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_destination, parent, false);
        return new PopularPlacesRVAdapterVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularPlacesRVAdapterVH holder, final int position) {
        final PopularPlaces current = popularPlaces.get(position);
        Glide.with(context)
                .load(current.getImageUrl())
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .into(holder.image);
        holder.destination.setText(current.getDestination());
        holder.country.setText(current.getCountry());
        holder.description.setText(current.getDescription());

        if (current.getRating() < 5.1) {
            holder.ratingBar.setRating((float) current.getRating());
            holder.ratingNumber.setText(String.valueOf(
                    new BigDecimal(current.getRating()).setScale(2, RoundingMode.HALF_EVEN).doubleValue()));
        }

        holder.engagement.setText(String.format("(%s)", current.getEngagement()));

        int resId = current.isFavorite() ?
                R.drawable.ic_baseline_favorite_24 :
                R.drawable.ic_favorite_border_black_24dp;

        holder.favoriteIcon.setImageResource(resId);
        holder.favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current.isFavorite()) {
                    current.setFavorite(false);
                } else {
                    current.setFavorite(true);
                }
                notifyItemChanged(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return popularPlaces == null ? 0 : popularPlaces.size();
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int selectedPosition);
    }
}
