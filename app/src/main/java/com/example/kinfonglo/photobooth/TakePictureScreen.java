package com.example.kinfonglo.photobooth;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePictureScreen extends AppCompatActivity {
    private PreferenceHelper _appSharedPref = new PreferenceHelper();
    private CameraController _camController;
    private FrameLayout cameraView;
    private int picCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.take_picture);
        View v = getWindow().getDecorView();
        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Set filename
        // Create directory to store
        SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy__HH_mm_ss");
        String photoParentDir = sdf.format(new Date());

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + this.getString(R.string.dir_photo_store) + "/" + photoParentDir;

        File newDir = new File(dir);
        newDir.mkdirs();

        _appSharedPref.setPhotoDirPath(this, dir);
        _appSharedPref.setPhotoParentDir(this, photoParentDir);

        _camController = new CameraController(this);
        cameraView = (FrameLayout) findViewById(R.id.sv_cameraView);
        _camController.getCameraInstance(cameraView);
    }

    public void onTakePictureClick(View view) {

        // Cam activities
        cameraView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        Button btnTakePicture = (Button) findViewById(R.id.btn_takePicture);
        btnTakePicture.setVisibility(View.GONE);

        picCount = 0;
        startCountdown();
    }

    public void onCancelPictureClick(View v) {
        _camController.releaseCamera();
    }

    public void takePicture() {
        _camController.takePicture();
    }

    public void startCountdown() {
        picCount++;
        new CountDownTimer(4000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                TextView txtCounter = (TextView) findViewById(R.id.txt_counter);
                txtCounter.bringToFront();
                txtCounter.setText(Long.toString((millisUntilFinished / 1000)));

                TextView txtImgCount = (TextView) findViewById(R.id.txt_imgCount);
                txtImgCount.bringToFront();
                txtImgCount.setVisibility(View.VISIBLE);
                txtImgCount.setText(String.valueOf(picCount) + " of 5");
            }

            @Override
            public void onFinish() {
                TextView txtCounter = (TextView) findViewById(R.id.txt_counter);
                txtCounter.setText("Cheese!");
                takePicture();
            }
        }.start();
    }

    public void goToShowTaken() {
        Intent showTaken = new Intent(this, ShowTaken.class);
        startActivity(showTaken);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        _camController.releaseCamera();
        //this.finishAffinity();
        Intent mainActivity = new Intent(TakePictureScreen.this, MainActivity.class);
        startActivity(mainActivity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _camController.releaseCamera();
    }
}
