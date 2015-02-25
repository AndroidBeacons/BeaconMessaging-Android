package com.yahoo.beaconmessaging;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.yahoo.beaconmessaging.model.Comment;
import com.yahoo.beaconmessaging.model.Exhibit;
import com.yahoo.beaconmessaging.model.Post;
import com.yahoo.beaconmessaging.util.LoginManager;


public class BeaconApplication extends Application {
    private static Context context;
    private static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        BeaconApplication.context = this;

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(Exhibit.class);
        ParseObject.registerSubclass(Post.class);
        Parse.initialize(this, "ggsnTmPvdqzCwYLs8diabLA3m0nNgYa75qMkN8l5", "vk5z5IXl2TCizhi8u68fvSWylBXcmMOcWIWKYMcH");
        preferences = getSharedPreferences("com.yahoo.beaconmessaging", Context.MODE_PRIVATE);

        LoginManager.init(context);
    }

}