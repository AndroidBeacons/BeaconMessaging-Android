package com.yahoo.beaconmessaging.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.adapter.PostRecyclerAdapter;
import com.yahoo.beaconmessaging.api.ExhibitClient;
import com.yahoo.beaconmessaging.model.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PostsStreamFragment extends Fragment {
    private ArrayList<Post> posts;
    private RecyclerView mPostStreamRecyclerView;
    private PostRecyclerAdapter aPostRecyclerAdapter;
    private SwipeRefreshLayout swipeContainer;

    String exhibitId;

    public static PostsStreamFragment newInstance(String exhibitId) {
        PostsStreamFragment fragment = new PostsStreamFragment();
        fragment.exhibitId = exhibitId;
        return fragment;
    }

    public PostsStreamFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populatePostStream();
    }

    private void populatePostStream() {
        ExhibitClient.getCommentsForExhibit(this.exhibitId, new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    addPosts(posts);

                }
            }
        });

    }

    protected void addPosts(List<Post> newPosts){
        this.posts = (ArrayList<Post>) newPosts;

        String[] userIds = new String[posts.size()];
        int i = 0;
        for (Post post : posts) {
            userIds[i++] = post.getUserId();
        }
        ExhibitClient.getUsersByList(userIds, new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                HashMap<String, ParseUser> parseUserHashMap = new HashMap();
                for (ParseUser parseUser : parseUsers) {
                    parseUserHashMap.put(parseUser.getObjectId(), parseUser);
                }

                aPostRecyclerAdapter.addItemsToList(posts, parseUserHashMap);// add the items to the adapter
                aPostRecyclerAdapter.notifyDataSetChanged(); // notify that the data set is changed
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_stream, container, false);

        // setup the views
        // setupSwipeRefresh(view);

        // Inflate the layout for this fragment
        mPostStreamRecyclerView = (RecyclerView) view.findViewById(R.id.rvPostStream);

        // Setup item layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mPostStreamRecyclerView.setLayoutManager(layoutManager);

        // Bind adapter to recycler
        posts = new ArrayList<Post>();
        // Post post = new Post();
        // posts.add(post);
        aPostRecyclerAdapter  = new PostRecyclerAdapter(posts,this.getActivity());
        mPostStreamRecyclerView.setAdapter(aPostRecyclerAdapter);
        /*RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST);*/
        return view;
    }

    /**
     * Setup Swipe Refresh
     * @param view
     */

    /*private void setupSwipeRefresh(View view) {
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                refreshStream();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }*/

    /*//implement sending an api request and then after you get the result,
    // call posts with the data to populate the view
    protected abstract void populateStream();*/

    /**
     * Refresh Stream
     */
    public void refreshStream(){
        aPostRecyclerAdapter.clearPosts();
        populatePostStream();
        // swipeContainer.setRefreshing(false);
        aPostRecyclerAdapter.notifyDataSetChanged();

    }


}
