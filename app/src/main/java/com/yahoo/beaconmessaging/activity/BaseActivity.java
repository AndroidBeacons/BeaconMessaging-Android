package com.yahoo.beaconmessaging.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.parse.ParseUser;
import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.util.LoginManager;

/**
 * Created by rneel on 2/23/15.
 */
public class BaseActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            LoginManager.logout();
            ParseUser.logOut();
            Intent i = new Intent(this, WelcomeActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
