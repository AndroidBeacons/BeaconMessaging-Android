package com.yahoo.beaconmessaging.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.squareup.picasso.Picasso;
import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.model.Exhibit;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by saianudeepm on 2/24/15.
 */
public class ExhibitRecyclerAdapter extends RecyclerView.Adapter<ExhibitRecyclerAdapter.ListItemViewHolder>{

    private ArrayList<Exhibit> mExhibitItems;
    private final Context mContext;
    
    //constructor
    public ExhibitRecyclerAdapter(ArrayList<Exhibit> items, Context context){
        if (items == null) {
            throw new IllegalArgumentException(
                    "modelData must not be null");
        }
        this.mExhibitItems = items;
        this.mContext = context;
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
        holder.tvFavoriteCount.setText(String.valueOf(exhibit.getFavoriteCount()));
        holder.tvPostCount.setText(String.valueOf(exhibit.getPostCount()));
        ParseFile imageUri= exhibit.getImageUri();
        if(imageUri!=null){
            Picasso.with(mContext).load(exhibit.getImageUri().getUrl()).into(holder.ivExhibitImage);
        }

    }

    @Override
    public int getItemCount() {
        return this.mExhibitItems.size();
    }


    class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        
        @InjectView(R.id.tvName) TextView tvName;
        @InjectView(R.id.ivExhibitImage) ImageView ivExhibitImage;
        @InjectView(R.id.tvDescription)  TextView tvDescription;
        @InjectView(R.id.tvFavoriteCount) TextView tvFavoriteCount;
        @InjectView(R.id.tvPostCount) TextView tvPostCount;
                
        public ListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            
            //Replacing with Betterknife annotations to find the views
            /*
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvFavoriteCount = (TextView) itemView.findViewById(R.id.tvFavoriteCount);
            tvPostCount = (TextView) itemView.findViewById(R.id.tvPostCount);
            ivExhibitImage = (ImageView) itemView.findViewById(R.id.ivExhibitImage);
            */
            
           /**
            * One way to hook up the on click events is from the recycler adapter
            * the other way would be to do it using ontouchlistener in the fragment
            * * * */
           /*
            //set item click listeners on image, name and description
            ivExhibitImage.setOnClickListener(this);
            tvName.setOnClickListener(this);
            tvDescription.setOnClickListener(this);*/
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext,"Clicked on position: "+getPosition(),Toast.LENGTH_SHORT).show();
        }

    }
    
    public void addItemsToList(List<Exhibit> items){
        this.mExhibitItems.addAll(items);
        
    }

    public void clearExhibits(){
        this.mExhibitItems.clear();
    }

  }
