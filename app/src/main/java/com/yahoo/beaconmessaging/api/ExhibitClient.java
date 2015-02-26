package com.yahoo.beaconmessaging.api;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
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
}
