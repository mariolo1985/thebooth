package com.example.kinfonglo.photobooth;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.lang.String;

public class MainActivity extends AppCompatActivity {
    PreferenceHelper _appSharedPref = new PreferenceHelper();
    private int MY_CAMERA_PERMISSION_REQUEST;
    private int MY_STORAGE_PERMISSION_REQUEST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View v = getWindow().getDecorView();
        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        // Check permissions
        if ((ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            // ask for permission
            requestPermissions(new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_REQUEST);
        }

        //startSlideShow();

    }

    public void onStartPhotoboothClick(View v) {
        try {

            if ((ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                final Intent startTakePictureScreen = new Intent(this, TakePictureScreen.class);
                startActivity(startTakePictureScreen);
            } else {
                Toast.makeText(this, "Allowed access to your camera and storage to start.", Toast.LENGTH_LONG).show();
                requestPermissions(new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_REQUEST);
            }
        } catch (Exception ex) {
            Log.d("STREAMER", ex.getMessage().toString());
        }

    }

    /*
    private void startSlideShow() {
        final LinearLayout llView = (LinearLayout) findViewById(R.id.llViewLanding);
        final ImageView imgView = (ImageView) findViewById(R.id.imgViewLanding);
        Runnable photoRunnable = new Runnable() {
            @Override
            public void run() {
                try {

                    while (true) {
                        // Loop forever
                        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + MainActivity.this.getString(R.string.dir_photo_store);
                        File wbm_dir = new File(dir);
                        File[] folders = wbm_dir.listFiles();// parent directory /WBM__

                        for (int x = 0; x < folders.length; x++) {
                            File folder = folders[x];// down to picture level
                            File[] allPhotos = folder.listFiles();

                            if (allPhotos.length > 0) {
                                for (int y = 0; y < allPhotos.length; y++) {
                                    Thread.sleep(2000);
                                    final Uri photoUri = Uri.fromFile(allPhotos[y]);
                                    ExifInterface exifInfo = new ExifInterface(allPhotos[y].getPath());
                                    int exifOrientation = exifInfo.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                                    int rotate = 0;
                                    switch (exifOrientation) {
                                        case 3:
                                            rotate = 180;
                                            break;
                                        case 6:
                                            rotate = 90;
                                            break;
                                        case 8:
                                            rotate = 270;
                                            break;
                                    }
                                    final int viewRotate = rotate;
                                    imgView.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            llView.setRotation(viewRotate);
                                            imgView.setImageURI(null);
                                            imgView.setImageURI(photoUri);

                                        }
                                    });
                                }

                            }
                        }
                    }


                } catch (Exception ex) {
                    Log.d("STREAMER", ex.getMessage().toString());
                }

            }
        }; // end runnable

        Thread myThread = new Thread(photoRunnable);
        myThread.start();
    }
*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }


}
