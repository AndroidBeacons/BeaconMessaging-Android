package com.yahoo.beaconmessaging.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.model.Exhibit;
import com.yahoo.beaconmessaging.model.TestExhibit;
import com.yahoo.beaconmessaging.util.ImageLoaderAndSaver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;


public class ExhibitAddActivity extends BaseActivity {

    private ImageView ivPicture;
    private EditText edDescription;
    private EditText edTitle;
    private CheckBox featured;
    private Uri uri;
    private ParseFile imageFile;
    private ImageLoaderAndSaver imageLoaderAndSaver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exhibit);        
        ivPicture = (ImageView)findViewById(R.id.ivPicture);
        edDescription = (EditText)findViewById(R.id.edDescription);
        edTitle = (EditText)findViewById(R.id.edTitle);
        imageLoaderAndSaver = new ImageLoaderAndSaver(this,ivPicture);
        featured = (CheckBox)findViewById(R.id.featured);
//        ParseObject p = new ParseObject("Exhibit");

    }

    public void onImageAdd(View view)
    {
        imageLoaderAndSaver.onImageAdd(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageLoaderAndSaver.onActivityResult(requestCode,resultCode,data);
    }
    
    public void clearFieldsAfterSave() {
        edDescription.setText("");
        edTitle.setText("");
        ivPicture.setImageBitmap(null);
        Toast.makeText(ExhibitAddActivity.this, "Exhibit data successfully saved", Toast.LENGTH_SHORT).show();            
    }
    
    public void onAdd(View view)
    {
        final ParseObject exhibit = ParseObject.create(Exhibit.class);
        exhibit.put("favoriteCount", 0);
        exhibit.put("name", edTitle.getText().toString());
        exhibit.put("description", edDescription.getText().toString());
        exhibit.put("featured", featured.isChecked());
        imageLoaderAndSaver.saveImageAndCall(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                exhibit.put("imageFile",imageLoaderAndSaver.getImageFile());
                exhibit.saveInBackground( new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)
                        {
                            clearFieldsAfterSave();
                        }
                        else 
                        {
                            Toast.makeText(ExhibitAddActivity.this, "Error saving record", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exhibit_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
}
