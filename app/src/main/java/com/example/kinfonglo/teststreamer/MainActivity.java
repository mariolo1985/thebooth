package com.example.kinfonglo.teststreamer;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    }


    public void onStartPhotoboothClick(View v) {
        try {

            // Create directory to store
            SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy__HH_mm_ss");
            String photoParentDir = sdf.format(new Date());

            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + v.getContext().getString(R.string.dir_photo_store) + "/" + photoParentDir;

            File newDir = new File(dir);
            newDir.mkdirs();

            _appSharedPref.setPhotoDirPath(this,dir);
            _appSharedPref.setPhotoParentDir(this, photoParentDir);

            final Intent startTakePictureScreen = new Intent(this, TakePictureScreen.class);
            startActivity(startTakePictureScreen);

        } catch (Exception ex) {
            Log.d("STREAMER", ex.getMessage().toString());
        }

    }
}
