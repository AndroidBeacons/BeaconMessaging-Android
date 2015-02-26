package com.yahoo.beaconmessaging.api;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.yahoo.beaconmessaging.model.Exhibit;
import com.yahoo.beaconmessaging.model.Post;

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
    
    public static void  getCommentsForExhibit(String exhibitId, FindCallback<Post> postFindCallback)
    {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("exhibitId", exhibitId);
        query.findInBackground( postFindCallback);
    }
}
