package com.yahoo.beaconmessaging.api;

import android.util.Log;

import com.estimote.sdk.Beacon;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.yahoo.beaconmessaging.model.Exhibit;
import com.yahoo.beaconmessaging.model.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by saianudeepm on 2/25/15.
 */
public class ExhibitClient {
    
    private static List<String> lastBeaconIds;

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
    
    public static void addPost(Post post, SaveCallback saveCallback)
    {
        ParseUser user = ParseUser.getCurrentUser();
        user.increment("postCount");
        user.saveInBackground();
        
        ParseQuery<Exhibit> query = ParseQuery.getQuery(Exhibit.class);
        query.getInBackground(post.getString("exhibitId"), new GetCallback<Exhibit>() {
            @Override
            public void done(Exhibit exhibit, ParseException e) {
                if (exhibit != null) {
                    Log.d("ExhibitClient", "Exhibit obtained has:" + exhibit.getInt("postCount") + ": posts");
                    exhibit.increment("postCount");
                    exhibit.saveInBackground();
                }
            }
        });

        post.saveInBackground(saveCallback);
    }

    public static void getUsersByList(String[] userids, FindCallback<ParseUser> findCallback) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn("objectId", Arrays.asList(userids));
        query.findInBackground(findCallback);
    }
    
    public static void getNearByExhibits(List<Beacon> beacons,FindCallback<Exhibit> findCallback)
    {
        boolean performQuery = false;
        List<String> ids = new ArrayList<String>();
        for(int i=0;i<beacons.size();i++){
            
            ids.add(beacons.get(i).getMacAddress());
            
        }
        if(lastBeaconIds==null) {
            lastBeaconIds = ids;
        }

        else if(lastBeaconIds.size()== ids.size() && lastBeaconIds.containsAll(ids))
                return;

        ParseQuery<Exhibit> query = ParseQuery.getQuery(Exhibit.class);
        query.whereContainedIn("beaconId",ids);
        query.findInBackground( findCallback);



    }

    public static void resetCachedNearByExhibits(){

        lastBeaconIds =null;
    }
}
