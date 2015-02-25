package com.yahoo.beaconmessaging.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.yahoo.beaconmessaging.model.Exhibit;

import java.util.List;

public class PopularExhibitListFragment extends ExhibitListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateStream();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /*Send the api request and then call addExhibits*/
    @Override
    protected void populateStream() {
        Exhibit.getPopularExhibits(new FindCallback<Exhibit>() {
            @Override
            public void done(List<Exhibit> exhibits, ParseException e) {
                if (e != null)
                {
                    addExhibits(exhibits);
                }
            }
        });
    }

    @Override
    protected void loadMore() {

    }
}
