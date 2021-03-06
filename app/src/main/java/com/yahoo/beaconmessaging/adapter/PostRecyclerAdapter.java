package com.yahoo.beaconmessaging.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.fragment.PostsStreamFragment;
import com.yahoo.beaconmessaging.model.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ListItemViewHolder> {

    private ArrayList<Post> mPostItems;
    private final Context mContext;
    private HashMap<String,ParseUser> parseUserHashMap;
    private PostsStreamFragment.ProfileImageClickListener profileImageClickListener;

    //constructor
    public PostRecyclerAdapter(ArrayList<Post> items, Context context,
                               PostsStreamFragment.ProfileImageClickListener profileImageClickListener){
        if (items == null) {
            throw new IllegalArgumentException(
                    "modelData must not be null");
        }
        this.mPostItems = items;
        this.mContext = context;
        this.profileImageClickListener = profileImageClickListener;
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
        ParseUser owner = parseUserHashMap.get(post.getUserId());
        holder.tvName.setText(owner.getUsername());
        holder.tvPost.setText(post.getPostText());
        ParseFile profileImage = owner.getParseFile("imageFile");
        if (profileImage!=null){
            Picasso.with(mContext).load(profileImage.getUrl()).into(holder.ivProfilePic);
        }
        ParseFile postImage = post.getPostImageUrl();
        if (postImage!=null){
            Picasso.with(mContext).load(postImage.getUrl()).into(holder.ivPostImage);
        } else {
            holder.ivPostImage.setImageBitmap(null);
            holder.ivPostImage.getLayoutParams().height = 0;
        }
        holder.ivProfilePic.setTag(owner.getObjectId());
        holder.ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileImageClickListener != null) {
                    profileImageClickListener.profileImageClicked(v);
                }
            }
        });
        // holder.tvFavoriteCount.setText(String.valueOf(exhibit.getFavoriteCount()));
        // holder.tvPostCount.setText(String.valueOf(exhibit.getPostCount()));
    }

    @Override
    public int getItemCount() {
        return this.mPostItems.size();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.tvName) TextView tvName;
        @InjectView(R.id.ivProfilePic) ImageView ivProfilePic;
        @InjectView(R.id.tvPost) TextView tvPost;
        @InjectView(R.id.ivPostImage) ImageView ivPostImage;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public void addItemsToList(List<Post> items, HashMap<String,ParseUser> parseUserHashMap){
        this.mPostItems.addAll(items);
        this.parseUserHashMap = parseUserHashMap;
    }

    public void clearPosts(){
        this.mPostItems.clear();
    }
}
