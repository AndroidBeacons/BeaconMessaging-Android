package com.yahoo.beaconmessaging.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Exhibit")
public class Exhibit extends ParseObject {

    public String getName() {
        return getString("name");
    }

    public String getDescription() {
        return getString("description");
    }

    public int getFavoriteCount() {
        return getInt("favoriteCount");
    }

    public boolean getFeatured() {
        return getBoolean("featured");
    }

    public ParseFile getImageUri() {
        return getParseFile("imageFile");
    }

    public int getPostCount() {
        return getInt("postCount");
    }

    public void setName(String name) {
        put("name", name);
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public void setFavoriteCount(int favoriteCount) {
        put("favoriteCount", favoriteCount);
    }

    public void setPostCount(int postCount) {
        put("postCount", postCount);
    }

}
