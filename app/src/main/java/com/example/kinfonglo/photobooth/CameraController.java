package com.example.kinfonglo.photobooth;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraController {
    private Context mainContext;
    private Context appContext;
    private boolean hasCamera;
    private Camera camera;
    private int cameraId;
    private String photoDirPath;
    private FrameLayout cameraPreviewLayout;
    private int picCount;
    private TakePictureScreen tps;

    private int config_picAmount = 5;

    // Functions
    public CameraController(Context c) {
        PreferenceHelper _appSharedPref = new PreferenceHelper();
        photoDirPath = _appSharedPref.getPhotoDirPath(c);
        mainContext = c;
        appContext = c.getApplicationContext();
        picCount = 0;
        tps = (TakePictureScreen) c;

        if (appContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            cameraId = getCameraId();

            if (cameraId != -1) {
                hasCamera = true;
            } else {
                hasCamera = false;
            }
        } else {
            hasCamera = false;
        }

        //Log.d("WBM", "HAS CAMERA BOOL: " + hasCamera);
        //Log.d("WBM", "Photo Dir Path: " + photoDirPath);

    }

    public void getCameraInstance(FrameLayout cameraView) {
        camera = null;

        if (hasCamera) {
            try {

                //Log.d("MITTENS", "GETCAMERAINSTANCE()");
                camera = Camera.open(cameraId);

                //Log.d("MITTENS", "CAMERA IS OPENED: " + (camera != null));
                prepareCamera(cameraView);
            } catch (Exception e) {
                //Log.d("MITTENS", e.getMessage().toString());
                hasCamera = false;
            }
        }
    }

    public void takePicture() {
        if (hasCamera) {
            //Log.d("WBM", "HAS CAMERA - TAKEPICTURE()");
            try {
                camera.takePicture(null, null, mPicture);
            } catch (Exception ex) {
                //Log.d("WBM", ex.getMessage().toString());
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
        //Log.d("WBM", "NUMBER OF CAMERAS: " + numberOfCameras);
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, ci);
            //Log.d("WBM", "CI FACING: " + ci.facing);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                camId = i;
            }
        }

        return camId;
    }

    private int getRotation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        int rotation = ((Activity) mainContext).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;

    }

    private int getPreviewRotation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        int rotation = ((Activity) mainContext).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;

    }

    private void prepareCamera(FrameLayout cameraView) {
        //SurfaceView view = new SurfaceView(context);

        try {
            int rotation = getRotation();
            int previewRotation = getPreviewRotation();

            CameraPreview mPreview = new CameraPreview(appContext, camera, previewRotation);
            cameraView.removeAllViews();
            cameraView.addView(mPreview);
            cameraPreviewLayout = cameraView;

            Camera.Parameters params = camera.getParameters();
            params.setJpegQuality(100);
            params.setRotation(rotation);

            camera.setParameters(params);
            //camera.setPreviewDisplay(view);
            //camera.startPreview();

            // Start timer after camera view is ready and < pics taken
            //Log.d("WBM", "Pic Count: " + picCount);
            if (picCount > 0) {
                tps.startCountdown();

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            //Log.d("WBM", "ON PICTURE TAKEN()");
            File pictureFile = getOutputMediaFile();

            if (pictureFile == null) {
                //Log.d("WBM", "Error creating media file, check storage permissions");
                return;
            }

            try {

                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                //Log.d("WBM", "File created");
                MediaScannerConnection.scanFile(appContext,
                        new String[]{
                                photoDirPath + "/" + pictureFile.getName()
                        },
                        null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                //Log.d("WBM", "Scanned: " + path);
                                //Log.d("WBM", "uri=" + uri);
                            }
                        }

                );


                releaseCamera();
                picCount++;
                if (picCount < config_picAmount) {
                    getCameraInstance(cameraPreviewLayout);
                } else {
                    tps.goToShowTaken();
                }
            } catch (FileNotFoundException e) {
                //Log.d("WBM", "File not found: " + e.getMessage());
            } catch (IOException e) {
                //Log.d("WBM", "Error accessing file: " + e.getMessage());
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

        String mediaPath = mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + "_booth.jpg";
        try {
            ExifInterface exifInfo = new ExifInterface(mediaPath);
            //exifInfo.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(getRotation()));
            //exifInfo.saveAttributes();
        } catch (Exception ex) {
            //Log.d("WBM : mediaPath: ", mediaPath);
            //Log.d("WBM", ex.getMessage().toString());
        }
        File mediaFile;
        mediaFile = new File(mediaPath);

        //Log.d("WBM", "STORAGE DIR: " + mediaStorageDir.getPath());
        return mediaFile;
    }
}