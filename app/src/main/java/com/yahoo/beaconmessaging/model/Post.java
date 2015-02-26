package com.yahoo.beaconmessaging.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Post")
public class Post extends ParseObject {

    public Post() {
    }

    public String getProfilePicUrl() {
        return "http://www.clker.com/cliparts/5/7/4/8/13099629981030824019profile.svg.med.png";
        // return getParseFile("imageUri");
    }

    public String getUserId() { return getString("userId"); }

    public String getPostText() {
        return getString("description");
    }

    public ParseFile getPostImageUrl() {
        return getParseFile("imageUri");
    }

}
