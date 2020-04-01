package com.fii.onlineshop.ui.camera;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.fii.onlineshop.R;
import com.fii.onlineshop.helpers.permissions.CameraPermissionHelper;
import com.fii.onlineshop.helpers.permissions.PermissionCallback;
import com.fii.onlineshop.helpers.permissions.StoragePermissionHelper;
import com.fii.onlineshop.ui.BaseActivity;
import com.fii.onlineshop.ui.MainActivity;

import androidx.camera.lifecycle.ProcessCameraProvider;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraActivity extends BaseActivity implements CameraXConfig.Provider {
    private static final String TAG = "CameraActivity";
    private String currentPhotoPath;
    private StoragePermissionHelper storagePermissionHelper;
    private CameraPermissionHelper cameraPermissionHelper;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private ProcessCameraProvider cameraProvider;
    private ImageCapture imageCapture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        previewView = findViewById(R.id.preview_view);

        setupPermissionHelpers();
        storagePermissionHelper.request();
    }

    void bindPreviewAndImageProvider(@NonNull ProcessCameraProvider cameraProvider) {
        // bind preview
        Preview preview = new Preview.Builder()
                .build();

        preview.setSurfaceProvider(previewView.getPreviewSurfaceProvider());

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        cameraProvider.bindToLifecycle(this, cameraSelector, preview);

        // bind image capture
        imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(previewView.getDisplay().getRotation())
                        .build();

        cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindPreviewAndImageProvider(cameraProvider);

            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void setupPermissionHelpers() {
        storagePermissionHelper = new StoragePermissionHelper(this, new PermissionCallback() {

            @Override
            public void onPermissionGranted() {
                cameraPermissionHelper.request();
            }

            @Override
            public void onPermissionDenied() {
                openMainActivity();
            }
        });
        cameraPermissionHelper = new CameraPermissionHelper(this, new PermissionCallback() {

            @Override
            public void onPermissionGranted() {
                previewView.post(() -> {
                    startCamera();
                    previewView.setOnClickListener(v -> {
                        takePhoto();
                    });
                });
            }

            @Override
            public void onPermissionDenied() {
                openMainActivity();
            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(CameraActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        storagePermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void takePhoto() {
        File image = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("RO"))
                    .format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_Onlineshop";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            Log.d(TAG, "onClick: ioException on creating the file: " + e.getMessage());
        }
        if (image != null) {
            ImageCapture.OutputFileOptions outputFileOptions =
                    new ImageCapture.OutputFileOptions.Builder(image).build();
            File finalImage = image;
            imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    Log.d(TAG, "Image saved at path: " + finalImage.getAbsolutePath());
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {
                    Log.e(TAG, "onError: Error while saving the image: ", exception);
                }
            });
        }
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }
}
