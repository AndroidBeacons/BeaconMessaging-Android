package com.yahoo.beaconmessaging.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseUser;
import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.fragment.FeaturedExhibitListFragment;
import com.yahoo.beaconmessaging.fragment.NearbyExhibitListFragment;


public class HomeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ExhibitsPagerAdapter(getSupportFragmentManager()));
        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        else if (id == R.id.action_profile)
        {
            Intent i = new Intent(this, ProfileActivity.class);
            ParseUser currentUser = ParseUser.getCurrentUser();
            i.putExtra("user", currentUser.getObjectId());
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
    //Returns the order of fragments in the view pager
    public class ExhibitsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles [] = {"Featured", "Nearby"};

        //Adapter gets the fragment manager to insert or remove fragments from the activity
        ExhibitsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        //The order and creation of the fragments within the pager
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new FeaturedExhibitListFragment();

                case 1: return new NearbyExhibitListFragment();

            }
            return null;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
    
}
