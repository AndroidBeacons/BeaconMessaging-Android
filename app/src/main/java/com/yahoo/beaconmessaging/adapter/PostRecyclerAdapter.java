package com.yahoo.beaconmessaging.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.model.Post;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ListItemViewHolder> {

    private ArrayList<Post> mPostItems;
    private final Context mContext;

    //constructor
    public PostRecyclerAdapter(ArrayList<Post> items, Context context){
        if (items == null) {
            throw new IllegalArgumentException(
                    "modelData must not be null");
        }
        this.mPostItems = items;
        this.mContext = context;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_post,
                        viewGroup, false);
        return new ListItemViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        Post post = mPostItems.get(position);
        holder.tvName.setText(post.getUserName());
        holder.tvPost.setText(post.getPostText());
        // holder.tvFavoriteCount.setText(String.valueOf(exhibit.getFavoriteCount()));
        // holder.tvPostCount.setText(String.valueOf(exhibit.getPostCount()));
        //TODO set the image
        // ParseFile imageUri= exhibit.getImageUri();
        // if(imageUri!=null){
        //     Picasso.with(mContext).load(exhibit.getImageUri().getUrl()).into(holder.ivExhibitImage);
        // }

    }

    @Override
    public int getItemCount() {
        return this.mPostItems.size();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.tvName) TextView tvName;
        @InjectView(R.id.ivProfilPic) ImageView ivProfilePic;
        @InjectView(R.id.tvPost) TextView tvPost;
        @InjectView(R.id.ivPostImage) ImageView ivPostImage;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public void addItemsToList(List<Post> items){
        this.mPostItems.addAll(items);

    }

    public void clearPosts(){
        this.mPostItems.clear();
    }
}
