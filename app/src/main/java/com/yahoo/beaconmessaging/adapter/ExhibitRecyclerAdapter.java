package com.yahoo.beaconmessaging.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.model.Exhibit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saianudeepm on 2/24/15.
 */
public class ExhibitRecyclerAdapter extends RecyclerView.Adapter<ExhibitRecyclerAdapter.ListItemViewHolder>{

    private ArrayList<Exhibit> mExhibitItems;
    
    //constructor
    public ExhibitRecyclerAdapter(ArrayList<Exhibit> items){
        if (items == null) {
            throw new IllegalArgumentException(
                    "modelData must not be null");
        }
        this.mExhibitItems = items;
    }
    
    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_exhibit,
                        viewGroup, false);
        return new ListItemViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        Exhibit exhibit = mExhibitItems.get(position);
        holder.tvName.setText(exhibit.getName());
        holder.tvDescription.setText(exhibit.getDescription());
        holder.tvFavoriteCount.setText(exhibit.getFavoriteCount());
        holder.tvPostCount.setText(exhibit.getPostCount());
        //TODO set the image
        //Picasso holder.ivExhibitImage
    }

    @Override
    public int getItemCount() {
        return this.mExhibitItems.size();
    }


    class ListItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        ImageView ivExhibitImage;
        TextView tvDescription;
        TextView tvFavoriteCount;
        TextView tvPostCount;
                
        public ListItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvFavoriteCount = (TextView) itemView.findViewById(R.id.tvFavoriteCount);
            tvPostCount = (TextView) itemView.findViewById(R.id.tvPostCount);
            ivExhibitImage = (ImageView) itemView.findViewById(R.id.ivExhibitImage);
        }
    }
    
    public void addItemsToList(List<Exhibit> items){
        this.mExhibitItems.addAll(items);
        
    }

    public void clearExhibits(){
        this.mExhibitItems.clear();
    }
}
