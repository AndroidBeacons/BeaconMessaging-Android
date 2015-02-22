package com.yahoo.beaconmessaging;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.yahoo.beaconmessaging.model.TestExhibit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;


public class ExhibitAddActivity extends ActionBarActivity {

    private ImageView ivPicture;
    private EditText edDescription;
    private EditText edTitle;
    private Uri uri;
    private ParseFile imageFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exhibit);
        ivPicture = (ImageView)findViewById(R.id.ivPicture);
        edDescription = (EditText)findViewById(R.id.edDescription);
        edTitle = (EditText)findViewById(R.id.edTitle);
//        ParseObject p = new ParseObject("Exhibit");

    }

    public void onImageAdd(View view)
    {
        String imageName = "file_" + new Random().nextInt();
        File image;

        try {
            image = File.createTempFile(imageName, ".jpg",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) );
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
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivPicture.setImageBitmap(imageBitmap);
        }
        else if (requestCode == 101 && resultCode == RESULT_OK)
        {
            Picasso.with(this).load(uri).into(ivPicture);
        }
    }
    
//    @Override
    protected void onActivityResult1(int requestCode, int resultCode, Intent data) {
        Log.d("ParseTest", "We got an intent data back");
        Log.d("ParseTest", data.getData().toString());
    }

    
    public void onAdd(View view)
    {
       new ImageFileLoadTask(new Runnable() {
            @Override
            public void run() {
                Log.d("ParseTestActivity", "Completed the save of file to server");
                ParseObject exhibit = ParseObject.create(TestExhibit.class);
                exhibit.put("favoriteCount", new Random().nextInt());
                exhibit.put("name", edTitle.getText().toString());
                exhibit.put("description", edDescription.getText().toString());
                if (imageFile != null && !imageFile.isDirty())
                {
                    exhibit.put("imageFile",imageFile);
                }
                exhibit.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        edDescription.setText("");
                        edTitle.setText("");
                        ivPicture.setImageBitmap(null);
                        Toast.makeText(ExhibitAddActivity.this,"Exhibit data successfully saved", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).execute(uri);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parse_test, menu);
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
    
    private class ImageFileLoadTask extends AsyncTask<Uri,Double,byte[]> {
        
        private Runnable continuation;
        public ImageFileLoadTask(Runnable continuation)
        {
            this.continuation = continuation;
            
        }
        @Override
        protected byte[] doInBackground(Uri... params) {
            Log.d("ParseTestActivity", "Background task to read and save started");

            Uri uri = params[0];
            File file = new File(uri.getPath());
            if (file.exists()) {
                int totalLength = (int)file.length();
                int sofar = 0;
                Log.d("ParseTestActivity", "File length is:" + totalLength);

                byte[] outputBytes = new byte[totalLength];
                try {
                    FileInputStream fis = new FileInputStream(file);
                    int x = 0;
                    
                    while ((x = fis.read(outputBytes,sofar,(totalLength -sofar) > 1024?1024:(totalLength - sofar))) > 0)
                    {
                        sofar = sofar + x;
                        Log.d("ParseTestActivity", "Read so far:" + sofar);
                        x = 0;
                        publishProgress(sofar/(double)totalLength);
                    }
                    fis.close();
                    return outputBytes;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException ioe)
                {
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
                        continuation.run();
                    }
                });
            }
            else 
            {
                Toast.makeText(ExhibitAddActivity.this,"Error: error reading image",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
