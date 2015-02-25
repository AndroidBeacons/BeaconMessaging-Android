package com.yahoo.beaconmessaging.fragment;

import android.os.Bundle;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.yahoo.beaconmessaging.model.Exhibit;

import java.util.List;

public class FeaturedExhibitListFragment extends ExhibitListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateStream();
    }

    @Override
    protected void populateStream() {
        Exhibit.getPopularExhibits(new FindCallback<Exhibit>() {
            @Override
            public void done(List<Exhibit> exhibits, ParseException e) {
                if (e == null) {
                    addExhibits(exhibits);
                }
            }
        });
    }

    @Override
    protected void loadMore() {

    }
}
