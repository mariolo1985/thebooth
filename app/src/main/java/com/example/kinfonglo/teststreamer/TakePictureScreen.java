package com.example.kinfonglo.teststreamer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


public class TakePictureScreen extends AppCompatActivity {
    private CameraController _camController;
    private int picCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.take_picture);

        _camController = new CameraController(this);
        FrameLayout cameraView = (FrameLayout) findViewById(R.id.sv_cameraView);
        _camController.getCameraInstance(cameraView);
    }

    public void onTakePictureClick(View view) {
        picCount = 0;
        startCountdown();
    }

    public void takePicture() {
        _camController.takePicture();
    }

    public void startCountdown() {
        new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TextView txtCounter = (TextView) findViewById(R.id.txt_counter);
                txtCounter.setText(Long.toString((millisUntilFinished / 1000) - 1));
            }

            @Override
            public void onFinish() {
                picCount += 1;
                takePicture();

                if (picCount <= 5) {
                    startCountdown();
                }
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        _camController.releaseCamera();
        this.finishAffinity();
    }
}
