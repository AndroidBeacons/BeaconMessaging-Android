package com.yahoo.beaconmessaging.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.adapter.ExhibitRecyclerAdapter;
import com.yahoo.beaconmessaging.model.Exhibit;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 
 */

//TODO need to setup the onclick event handlers
public abstract class ExhibitListFragment extends Fragment {

    protected ArrayList<Exhibit> exhibitList;
    private ExhibitRecyclerAdapter mExhibitRecyclerAdapter;
    private RecyclerView mExhibitRecyclerview;
    private SwipeRefreshLayout swipeContainer;
    

    public ExhibitListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_exhibit_list, container, false);
        
        //setup the views
        setupSwipeRefresh(view);
        
        // Inflate the layout for this fragment
        mExhibitRecyclerview = (RecyclerView) view.findViewById(R.id.rvExhibits);

        // Setup item layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mExhibitRecyclerview.setLayoutManager(layoutManager);
        
        // Bind adapter to recycler
        mExhibitRecyclerAdapter  = new ExhibitRecyclerAdapter(exhibitList);
        mExhibitRecyclerview.setAdapter(mExhibitRecyclerAdapter);
        return view;
    }

    /**
     * * Private Methods
     * * */

    private void setupSwipeRefresh(View view) {
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateStream();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    /**
     * * Protected methods
     * * */
    
    protected void addExhibits(List<Exhibit> newExhibits, boolean isRefresh){
        if(isRefresh){
            /*
            * exhibitList.clear()
            * // this should work ? cause of reference bt check if its right way
            * * * */
            mExhibitRecyclerAdapter.clearExhibits();
        }
        mExhibitRecyclerAdapter.addItemsToList(newExhibits);// add the items to the adapter
        mExhibitRecyclerAdapter.notifyDataSetChanged(); // notify that the data set is changed
        if(isRefresh){
            swipeContainer.setRefreshing(false);
            mExhibitRecyclerAdapter.notifyDataSetChanged();
        }

    }

    //implement sending an api request and then after you get the result,
    // call addExhibits with the data to populate the view
    protected abstract void populateStream();

    protected abstract void loadMoreTimeline();//optional?

}
