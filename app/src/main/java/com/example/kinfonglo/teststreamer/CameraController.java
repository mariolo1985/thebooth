package com.example.kinfonglo.teststreamer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraController {

    private Context context;
    private boolean hasCamera;
    private Camera camera;
    private int cameraId;
    private String photoDirPath;
    private FrameLayout cameraPreviewLayout;

    // Functions
    public CameraController(Context c) {
        PreferenceHelper _appSharedPref = new PreferenceHelper();
        photoDirPath = _appSharedPref.getPhotoDirPath(c);
        context = c.getApplicationContext();

        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            cameraId = getCameraId();

            if (cameraId != -1) {
                hasCamera = true;
            } else {
                hasCamera = false;
            }
        } else {
            hasCamera = false;
        }

        Log.d("MITTENS", "HAS CAMERA BOOL: " + hasCamera);
        Log.d("MITTENS", "Photo Dir Path: " + photoDirPath);

    }

    public void getCameraInstance(FrameLayout cameraView) {
        camera = null;

        if (hasCamera) {
            try {

                Log.d("MITTENS", "GETCAMERAINSTANCE()");
                camera = Camera.open(cameraId);

                Log.d("MITTENS", "CAMERA IS OPENED: " + (camera != null));
                prepareCamera(cameraView);
            } catch (Exception e) {
                Log.d("MITTENS", e.getMessage().toString());
                hasCamera = false;
            }
        }
    }

    public void takePicture() {
        if (hasCamera) {
            Log.d("MITTENS", "HAS CAMERA - TAKEPICTURE()");
            try {
                camera.takePicture(null, null, mPicture);
            } catch (Exception ex) {
                Log.d("MITTENS", ex.getMessage().toString());
            }
        }
    }

    public void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private int getCameraId() {
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Log.d("MITTENS", "NUMBER OF CAMERAS: " + numberOfCameras);
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, ci);
            Log.d("MITTENS", "CI FACING: " + ci.facing);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                camId = i;
            }
        }

        return camId;
    }

    private void prepareCamera(FrameLayout cameraView) {
        //SurfaceView view = new SurfaceView(context);

        try {
            CameraPreview mPreview = new CameraPreview(context,camera);
            cameraView.removeAllViews();
            cameraView.addView(mPreview);
            cameraPreviewLayout = cameraView;

            Camera.Parameters params = camera.getParameters();
            params.setJpegQuality(100);
            params.setRotation(90);

            camera.setParameters(params);
            //camera.setPreviewDisplay(view);
            //camera.startPreview();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Log.d("MITTENS", "ON PICTURE TAKEN()");
            File pictureFile = getOutputMediaFile();

            if (pictureFile == null) {
                Log.d("MITTENS", "Error creating media file, check storage permissions");
                return;
            }

            try {

                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Log.d("MITTENS", "File created");
                MediaScannerConnection.scanFile(context,
                        new String[]{
                                photoDirPath + "/" + pictureFile.getName()
                        },
                        null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.d("MITTENS", "Scanned: " + path);
                                Log.d("MITTENS", "uri=" + uri);
                            }
                        }

                );


                releaseCamera();
                getCameraInstance(cameraPreviewLayout);
            } catch (FileNotFoundException e) {
                Log.d("MITTENS", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("MITTENS", "Error accessing file: " + e.getMessage());
            }
        }
    };

    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.


        File mediaStorageDir = new File(photoDirPath);

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + "_booth.jpg");

        Log.d("MITTENS", "STORAGE DIR: " + mediaStorageDir.getPath());
        return mediaFile;
    }
}