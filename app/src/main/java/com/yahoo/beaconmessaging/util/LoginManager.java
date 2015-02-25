package com.yahoo.beaconmessaging.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.activity.BaseActivity;
import com.yahoo.beaconmessaging.activity.LoginActivity;
import com.yahoo.beaconmessaging.activity.WelcomeActivity;

public class LoginManager {
    
    private static  Context context;
    private static SharedPreferences preferences;

    public static void init(Context context)
    {
        LoginManager.context = context;
        LoginManager.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public static boolean isSavedLoginPresent()
    {
        return !getUser().equals("") && !getPassword().equals("");
    }
    
    public static String getUser()
    {        
        return preferences.getString("user", "");
    }
    
    public static String getPassword()
    {
        return preferences.getString("password", "");
    }
    
    public static void logout()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("user");
        editor.remove("password");
        editor.commit();
    }
    
    public static void loginComplete(String user, String password)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", user);
        editor.putString("password", password);
        editor.commit();
    }
}
