package com.example.kinfonglo.photobooth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class ShowTaken extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_taken);
        View v = getWindow().getDecorView();
        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        startSlideShow();
    }

    private void startSlideShow() {
        final ImageView imgView = (ImageView) findViewById(R.id.imgViewTaken);
        final TextView txtCount = (TextView) findViewById(R.id.txt_show_imgCount);

        Runnable photoRunnable = new Runnable() {
            @Override
            public void run() {
                try {

                    while (true) {
                        // Loop forever
                        PreferenceHelper appPref = new PreferenceHelper();
                        String dir = appPref.getPhotoDirPath(ShowTaken.this);
                        File folder = new File(dir);
                        File[] allPhotos = folder.listFiles();// down to picture level

                        if (allPhotos.length > 0) {
                            for (int y = 0; y < allPhotos.length; y++) {
                                Thread.sleep(2000);
                                Uri photoUri = Uri.fromFile(allPhotos[y]);
                                final Uri tempPhotoUri = photoUri;
                                final int count = y;
                                imgView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        imgView.setImageURI(null);
                                        imgView.setImageURI(tempPhotoUri);

                                        txtCount.setText(String.valueOf(count + 1) + " of 5");
                                    }
                                });
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

    public void btnTakeMorePictures(View v) {
        Intent takePictureActivity = new Intent(ShowTaken.this, TakePictureScreen.class);
        startActivity(takePictureActivity);
    }

    public void btnViewAllClicked(View v) {
        Intent mainActivity = new Intent(ShowTaken.this, MainActivity.class);
        startActivity(mainActivity);
    }


}
