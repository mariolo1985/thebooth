package com.example.kinfonglo.teststreamer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;


public class TakePictureScreen extends AppCompatActivity {
    private CameraController _camController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.take_picture);

        _camController = new CameraController(this);
        StartTakingPictures();

        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                TextView txtCounter = (TextView)findViewById(R.id.txt_counter);
                txtCounter.setText(Long.toString(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {

            }
        }.start();


    }

    public void StartTakingPictures() {

        _camController.getCameraInstance();
        _camController.takePicture();
    }
}
