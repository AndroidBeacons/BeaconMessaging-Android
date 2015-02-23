package com.yahoo.beaconmessaging.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.fragment.UserDetailFragment;
import com.yahoo.beaconmessaging.util.ImageLoaderAndSaver;

import java.io.File;
import java.io.IOException;
import java.util.Random;


public class ProfileActivity extends BaseActivity  implements UserDetailFragment.ImageClickListener {

    UserDetailFragment userDetailFragment;
    private Uri uri;
    private ImageView ivProfileImage;
    private ImageLoaderAndSaver imageLoaderAndSaver;
    private ParseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String userObjectId = getIntent().getStringExtra("user");
        FrameLayout flUserDetail = (FrameLayout)findViewById(R.id.flUserDetail);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        userDetailFragment = UserDetailFragment.newInstance(userObjectId,this);
        ft.replace(R.id.flUserDetail, userDetailFragment);
        ft.commit();
    }

    @Override
    public void imageClicked(View view) {
        if (imageLoaderAndSaver == null) {
            ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
            imageLoaderAndSaver = new ImageLoaderAndSaver(this, ivProfileImage);
        }
        imageLoaderAndSaver.onImageAdd(view);
    }

    @Override
    public void setUser(ParseUser user) {
        this.user = user;   
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public void onImageAdd(View view)
    {
        String imageName = "file_" + new Random().nextInt();
        File image;
        ivProfileImage = (ImageView)view.findViewById(R.id.ivProfileImage);
        try {
            image = File.createTempFile(imageName, ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) );
            uri = Uri.fromFile(image);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, 101);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageLoaderAndSaver.onActivityResult(requestCode,resultCode,data);
        imageLoaderAndSaver.saveImageAndCall(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                ProfileActivity.this.user.put("imageFile", imageLoaderAndSaver.getImageFile());
                ProfileActivity.this.user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)
                        {
                            Toast.makeText(ProfileActivity.this, "User image successfully updated", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ProfileActivity.this, "Error updating user image:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


}
