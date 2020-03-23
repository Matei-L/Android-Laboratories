package com.fii.onlineshop.ui.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.fii.onlineshop.R;
import com.fii.onlineshop.helpers.permissions.PermissionCallback;
import com.fii.onlineshop.helpers.permissions.StoragePermissionHelper;
import com.fii.onlineshop.ui.BaseActivity;
import com.fii.onlineshop.ui.MainActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends BaseActivity {
    private static final String TAG = "CameraActivity";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String currentPhotoPath;
    private StoragePermissionHelper storagePermissionHelper;

    private ImageView imageView;
    private TextView imagePathTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = findViewById(R.id.imageView);
        imagePathTextView = findViewById(R.id.imagePath);

        setupPermissionHelpers();
        storagePermissionHelper.request();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, "dispatchTakePictureIntent: Error while opening a file for a new photo.");
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.fii.onlineshop.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Toast.makeText(this, currentPhotoPath + " saved", Toast.LENGTH_SHORT).show();
            showImage(currentPhotoPath);
        }
    }

    private void showImage(String path) {
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imagePathTextView.setText(currentPhotoPath);
            imageView.setImageBitmap(myBitmap);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("RO")).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_Onlineshop";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setupPermissionHelpers() {
        storagePermissionHelper = new StoragePermissionHelper(this, new PermissionCallback() {

            @Override
            public void onPermissionGranted() {
                dispatchTakePictureIntent();
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
    }
}
