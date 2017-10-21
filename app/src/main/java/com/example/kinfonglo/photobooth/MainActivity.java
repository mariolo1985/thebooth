package com.example.kinfonglo.photobooth;


import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.String;

public class MainActivity extends AppCompatActivity {
    PreferenceHelper _appSharedPref = new PreferenceHelper();

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

        startSlideShow();
    }

    public void onStartPhotoboothClick(View v) {
        try {

            // Create directory to store
            SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy__HH_mm_ss");
            String photoParentDir = sdf.format(new Date());

            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + v.getContext().getString(R.string.dir_photo_store) + "/" + photoParentDir;

            File newDir = new File(dir);
            newDir.mkdirs();

            _appSharedPref.setPhotoDirPath(this, dir);
            _appSharedPref.setPhotoParentDir(this, photoParentDir);

            final Intent startTakePictureScreen = new Intent(this, TakePictureScreen.class);
            startActivity(startTakePictureScreen);

        } catch (Exception ex) {
            Log.d("STREAMER", ex.getMessage().toString());
        }

    }

    private void startSlideShow() {
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

                                    imgView.post(new Runnable() {
                                        @Override
                                        public void run() {
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
}
