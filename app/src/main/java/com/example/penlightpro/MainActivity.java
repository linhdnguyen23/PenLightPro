package com.example.penlightpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    android.hardware.camera2.CameraDevice cameraDevice;
    private CameraManager cameraManager;
    private String cameraId;
    private static boolean cameraPerm = false;

    boolean flashOn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToggleButton customButton = (ToggleButton) findViewById(R.id.customButton);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, 101);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            cameraPerm = true;
        }
        try {
            // we want back camera, which has index 0
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                turnOnOff(v);
            }
            else {
                // say that they don't have camera
                Toast.makeText(MainActivity.this, "Device doesn't have camera", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    public boolean hasCameraPermission() {
        return cameraPerm;
    }
    public void checkState() {
        if (flashOn) {
            flashOn = false;
            turnFlashOff();
        }
        else {
            flashOn = true;
            turnFlashOn();
        }
    }

    public void turnOnOff(View view) {
        if (!hasCameraPermission()) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.CAMERA }, 101);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraPerm = true;
            }
            return;
        }
        else {
            checkState();
        }
    }

    public void turnFlashOn() {
        try {
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void turnFlashOff() {
        try {
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}