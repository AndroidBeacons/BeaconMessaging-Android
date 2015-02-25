package com.yahoo.beaconmessaging.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.activity.ExhibitActivity;
import com.yahoo.beaconmessaging.adapter.ExhibitRecyclerAdapter;
import com.yahoo.beaconmessaging.model.Exhibit;
import com.yahoo.beaconmessaging.ui.DividerItemDecoration;


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
        
        //setup the recyclerview adapter
        setupRecycleViewAdapter(view);
        return view;
    }

    protected void setupRecycleViewAdapter(View view) {
        // Inflate the layout for this fragment
        mExhibitRecyclerview = (RecyclerView) view.findViewById(R.id.rvExhibits);

        // Setup item layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mExhibitRecyclerview.setLayoutManager(layoutManager);

        // Bind adapter to recycler
        exhibitList = new ArrayList<Exhibit>();
        mExhibitRecyclerAdapter  = new ExhibitRecyclerAdapter(exhibitList,this.getActivity());
        mExhibitRecyclerview.setAdapter(mExhibitRecyclerAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mExhibitRecyclerview.addItemDecoration(itemDecoration);
        mExhibitRecyclerview.addOnItemTouchListener(new RecyclerTouchListener(this.getActivity(), mExhibitRecyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Do something on click 
                view.playSoundEffect(SoundEffectConstants.CLICK);
                Intent intent = new Intent(getActivity(), ExhibitActivity.class);
                intent.putExtra("exhibitId",exhibitList.get(position).getObjectId());
                startActivity(intent);
                //Toast.makeText(getActivity(), "click detected on position: "+position, Toast.LENGTH_SHORT).show();
                        
            }

            @Override
            public void onLongClick(View view, int position) {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                //For now lets not do anything if its on click
                //Toast.makeText(getActivity(), "Long click detected on position:"+position, Toast.LENGTH_SHORT).show();
            }
        }));

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
                refreshStream();
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
    
    protected void addExhibits(List<Exhibit> newExhibits){
        mExhibitRecyclerAdapter.addItemsToList(newExhibits);// add the items to the adapter
        mExhibitRecyclerAdapter.notifyDataSetChanged(); // notify that the data set is changed
    }

    //implement sending an api request and then after you get the result,
    // call addExhibits with the data to populate the view
    protected abstract void populateStream();
    
    protected void refreshStream(){
        mExhibitRecyclerAdapter.clearExhibits();
        populateStream();
        swipeContainer.setRefreshing(false);
        mExhibitRecyclerAdapter.notifyDataSetChanged();
        
    }

    protected abstract void loadMore();//optional?
    
    /*
    STEPS TO HANDLE THE RECYCLER CLICK
    1 Create a class that EXTENDS RecylcerView.OnItemTouchListener
    2 Create an interface inside that class that supports click and long click and indicates the View that was clicked and the position where it was clicked
    3 Create a GestureDetector to detect ACTION_UP single tap and Long Press events
    4 Return true from the singleTap to indicate your GestureDetector has consumed the event.
    5 Find the childView containing the coordinates specified by the MotionEvent and if the childView is not null and the listener is not null either, fire a long click event
    6 Use the onInterceptTouchEvent of your RecyclerView to check if the childView is not null, the listener is not null and the gesture detector consumed the touch event
    7 if above condition holds true, fire the click event
    8 return false from the onInterceptedTouchEvent to give a chance to the childViews of the RecyclerView to process touch events if any.
    9 Add the onItemTouchListener object for our RecyclerView that uses our class created in step 1
     */


    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);


    }

    static class RecyclerTouchListener implements  RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;
        
        public RecyclerTouchListener(Context context, final RecyclerView recycler, final ClickListener clickListener ){
            this.clickListener= clickListener;
            
            //Configure our gesture detector to respond to clicks and long presses
            this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                
                // onSingleTapUp is called if gesture detector detects the gesture as on SingleTap up (click) 
                // we want gestureDetector.onTouchEvent(e) returns true if its tap up (click event)
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                    
                }
                
                // when touch is detected as longpress, call our onLongClick method defined in clickListener
                // onLongpress is called if the gesture detector detects the gesture as long press
                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycler.findChildViewUnder(e.getX(), e.getY());
                    if(child!=null && clickListener!=null)
                    {
                        clickListener.onLongClick(child, recycler.getChildPosition(child));
                    }
                }
            });
        }

        // when the touch is detected as click, call our onClick method defined in clickListener
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            // When a touch event occurs, we try to send the event to our gesture detector and let it
            // find out if its a click event (gestureDetector.onTouchEvent(e) returns true if click event)
            View child=rv.findChildViewUnder(e.getX(), e.getY());
            if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e))
            {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }
    }
    
}
