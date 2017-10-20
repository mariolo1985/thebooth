package com.example.kinfonglo.teststreamer;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

public class ShowTaken extends AppCompatActivity {
    private int _count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_taken);

        startSlideShow();
    }

    private void startSlideShow() {
        final ImageView imgView = (ImageView) findViewById(R.id.imgViewTaken);
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

                        _count = 0;
                        for (int x = 0; x < allPhotos.length; x++) {

                            if (allPhotos.length > 0) {
                                for (int y = 0; y < allPhotos.length; y++) {
                                    Thread.sleep(2000);
                                    Uri photoUri = Uri.fromFile(allPhotos[_count]);
                                    final Uri tempPhotoUri = photoUri;
                                    imgView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            imgView.setImageURI(null);
                                            imgView.setImageURI(tempPhotoUri);
                                            _count++;
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
