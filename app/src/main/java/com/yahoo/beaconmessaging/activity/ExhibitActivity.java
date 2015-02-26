package com.yahoo.beaconmessaging.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.fragment.AddPostFragment;
import com.yahoo.beaconmessaging.fragment.ExhibitDetailFragment;
import com.yahoo.beaconmessaging.fragment.PostsStreamFragment;
import com.yahoo.beaconmessaging.model.Exhibit;


public class ExhibitActivity extends BaseActivity implements AddPostFragment.AddPostDialogListener {
    private Exhibit mExhibit;
    private ExhibitDetailFragment mExhibitDetailFragment;
    private PostsStreamFragment mPostsStreamFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit);
        String exhibitId = getIntent().getStringExtra("exhibitId");
        ParseQuery<Exhibit> query = ParseQuery.getQuery(Exhibit.class);
        //TODO see how you can use cache and not network everytime
        // First try to find from the cache and only then go to network
        // query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY
        // Execute the query to find the object with ID
        query.getInBackground(exhibitId, new GetCallback<Exhibit>() {
            public void done(Exhibit exhibit, ParseException e) {
                if (e == null) {
                    // item was found
                    mExhibit = exhibit;
                    mExhibitDetailFragment.populateView(mExhibit);
                }
            }
        });
        mExhibitDetailFragment = ExhibitDetailFragment.newInstance(mExhibit);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager = getSupportFragmentManager();
        //begin the transaction
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flExhibitContainer,mExhibitDetailFragment);
        fragmentTransaction.commit();

        mPostsStreamFragment = PostsStreamFragment.newInstance(exhibitId, null);
        fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flPostsContainer, mPostsStreamFragment);
        fragmentTransaction.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exhibit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishAddPostDialog() {
        mPostsStreamFragment.refreshStream();
    }
}
