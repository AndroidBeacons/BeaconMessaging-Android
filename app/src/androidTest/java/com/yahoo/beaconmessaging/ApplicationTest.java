package com.yahoo.beaconmessaging;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.yahoo.beaconmessaging.BeaconApplication;
import com.yahoo.beaconmessaging.model.Comment;
import com.yahoo.beaconmessaging.model.Exhibit;
import com.yahoo.beaconmessaging.model.Post;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    
    @MediumTest
    public void parseTest()
    {
        Parse.enableLocalDatastore(getContext());
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(Exhibit.class);
        ParseObject.registerSubclass(Post.class);
        Parse.initialize(getContext(), "ggsnTmPvdqzCwYLs8diabLA3m0nNgYa75qMkN8l5", "vk5z5IXl2TCizhi8u68fvSWylBXcmMOcWIWKYMcH");

//        ParseQuery.getQuery(Exhibit.class);
        
        Exhibit exhibit = ParseObject.create(Exhibit.class);
        exhibit.setDescription("test exhibit");
        exhibit.setName("first");
        exhibit.setFavoriteCount(100);

        try {
            exhibit.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        ParseObject obj = new ParseObject("Exhibit");
        obj.put("name", "one");
        obj.put("description", "one desc");

        try {
            obj.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}