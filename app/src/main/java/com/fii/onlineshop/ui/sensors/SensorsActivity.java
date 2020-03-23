package com.fii.onlineshop.ui.sensors;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fii.onlineshop.R;
import com.fii.onlineshop.helpers.permissions.GPSChecker;
import com.fii.onlineshop.helpers.permissions.LocationPermissionHelper;
import com.fii.onlineshop.helpers.permissions.PermissionCallback;
import com.fii.onlineshop.ui.BaseActivity;
import com.fii.onlineshop.ui.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SensorsActivity extends BaseActivity {

    private SensorManager sensorManager;
    private List<Sensor> sensors = new ArrayList<>();

    private RecyclerView sensorListRecyclerView;
    private SensorListAdapter sensorListAdapter;

    private GPSChecker gpsChecker;
    private LocationPermissionHelper locationPermissionHelper;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView latitudeTextView;
    private TextView longitudeTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        latitudeTextView = findViewById(R.id.latitude);
        longitudeTextView = findViewById(R.id.longitude);
        sensorListRecyclerView = findViewById(R.id.sensor_list);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupPermissionHelpers();
        gpsChecker.asyncCheck();

        setupRecyclerView();
        initSensorsList();
    }

    private void setupPermissionHelpers() {
        locationPermissionHelper = new LocationPermissionHelper(this, new PermissionCallback() {

            @Override
            public void onPermissionGranted() {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(SensorsActivity.this::showLocation);
            }

            @Override
            public void onPermissionDenied() {
                openMainActivity();
            }
        });
        gpsChecker = new GPSChecker(this, new PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                locationPermissionHelper.request();
            }

            @Override
            public void onPermissionDenied() {
                openMainActivity();
            }
        });
    }

    private void showLocation(Location location) {
        if (location != null) {
            String latitude = String.format(new Locale("RO"), "%.6f", location.getLatitude());
            latitudeTextView.setText(latitude);
            String longitude = String.format(new Locale("RO"), "%.6f", location.getLongitude());
            longitudeTextView.setText(longitude);
        }
    }

    private void openMainActivity() {
        Intent intent = new Intent(SensorsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    void initSensorsList() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensors.clear();
            sensors.addAll(sensorManager.getSensorList(Sensor.TYPE_ALL));
        } else {
            sensors.clear();
            sensors.addAll(new ArrayList<>());
        }
        sensorListAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        sensorListAdapter = new SensorListAdapter(sensors);
        sensorListRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        sensorListRecyclerView.setAdapter(sensorListAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gpsChecker.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
