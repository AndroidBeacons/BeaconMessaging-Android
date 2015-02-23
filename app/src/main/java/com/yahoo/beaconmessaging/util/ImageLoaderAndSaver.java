package com.yahoo.beaconmessaging.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.yahoo.beaconmessaging.model.Exhibit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

/* 
* Easy way to capture an image. Also an easy way to capture image load functions
 */
public class ImageLoaderAndSaver {
    private ParseFile imageFile;
    private Activity context;
    private Uri uri;
    private ImageView ivPicture;
    
    public ImageLoaderAndSaver(Activity activity, ImageView imageView)
    {
        this.context  = activity;
        this.ivPicture = imageView;
        
    }
    public void onImageAdd(View view)
    {
        String imageName = "file_" + new Random().nextInt();
        File image;

        try {
            image = File.createTempFile(imageName, ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) );
            uri = Uri.fromFile(image);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivityForResult(intent, 101);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == context.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivPicture.setImageBitmap(imageBitmap);
        }
        else if (requestCode == 101 && resultCode == context.RESULT_OK)
        {
            Picasso.with(context).load(uri).into(ivPicture);
        }
    }


    public void saveImageAndCall(final SaveCallback saveCallback)
    {
        new ImageFileLoadTask(saveCallback).execute(uri);
    }


    public ParseFile getImageFile() {
        return imageFile;
        
    }

    private class ImageFileLoadTask extends AsyncTask<Uri, Double, byte[]> {

        private SaveCallback continuation;

        public ImageFileLoadTask(SaveCallback continuation) {
            this.continuation = continuation;
        }

        @Override
        protected byte[] doInBackground(Uri... params) {
            Log.d("ParseTestActivity", "Background task to read and save started");

            Uri uri = params[0];
            File file = new File(uri.getPath());
            if (file.exists()) {
                int totalLength = (int) file.length();
                int sofar = 0;
                Log.d("ParseTestActivity", "File length is:" + totalLength);

                byte[] outputBytes = new byte[totalLength];
                try {
                    FileInputStream fis = new FileInputStream(file);
                    int x = 0;

                    while ((x = fis.read(outputBytes, sofar, (totalLength - sofar) > 1024 ? 1024 : (totalLength - sofar))) > 0) {
                        sofar = sofar + x;
                        Log.d("ParseTestActivity", "Read so far:" + sofar);
                        x = 0;
                        publishProgress(sofar / (double) totalLength);
                    }
                    fis.close();
                    return outputBytes;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            return new byte[0];
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            if (bytes.length > 0)
            {
                imageFile = new ParseFile("image", bytes,"jpg");
                imageFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        continuation.done(e);
                    }
                });
            }
            else
            {
                Toast.makeText(context, "Error: error reading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
