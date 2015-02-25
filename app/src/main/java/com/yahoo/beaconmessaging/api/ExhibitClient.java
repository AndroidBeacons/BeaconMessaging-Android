package com.yahoo.beaconmessaging.api;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.yahoo.beaconmessaging.model.Exhibit;

/**
 * Created by saianudeepm on 2/25/15.
 */
public class ExhibitClient {

    public static void getPopularExhibits(FindCallback<Exhibit> findCallback)
    {
        ParseQuery<Exhibit> query = ParseQuery.getQuery(Exhibit.class);
        query.whereEqualTo("featured", true);
        query.findInBackground(findCallback);
    }
}
