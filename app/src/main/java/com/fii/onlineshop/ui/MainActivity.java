package com.fii.onlineshop.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fii.onlineshop.R;
import com.fii.onlineshop.db.ProductsDatabase;
import com.fii.onlineshop.db.dao.ProductDao;
import com.fii.onlineshop.db.entities.ProductEntity;
import com.fii.onlineshop.ui.camera.CameraActivity;
import com.fii.onlineshop.ui.sensors.SensorsActivity;
import com.fii.onlineshop.ui.settings.SettingsActivity;
import com.fii.onlineshop.ui.prosduct_info.ProductInfoActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView productListRecyclerView;
    private ProductListAdapter productListAdapter;
    private List<ProductEntity> productList = new ArrayList<>();
    private AlertDialog.Builder alertDialogBuilder;

    private ProductsDatabase productsDatabase;
    private ProductDao productDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productListRecyclerView = findViewById(R.id.product_list);

        initDatabaseConnection();

        setupRecyclerView();
        getProductList();
    }

    private void initDatabaseConnection() {
        productsDatabase = ProductsDatabase.getInstance(this);
        productDao = productsDatabase.productDao();
    }

    private void getProductList() {
        productDao.getProducts().observe(this, (products) -> {
            productList.clear();
            productList.addAll(products);
            productListAdapter.notifyDataSetChanged();
        });
    }

    private void setupRecyclerView() {
        productListAdapter = new ProductListAdapter(productList);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Show a grid if on landscape mode
            productListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            // Show a list if on portrait mode
            productListRecyclerView.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
        productListRecyclerView.setAdapter(productListAdapter);

        productListAdapter.setOnClickListeners(new ProductListAdapter.OnClickListeners() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, ProductInfoActivity.class);
                intent.putExtra("id", productList.get(position).getId());
                startActivity(intent);
                onPause();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: Called");
        String passedString = "Hai salut!";
        outState.putString("OUT_STRING", passedString);
        Log.d(TAG, "onSaveInstanceState: I sent: " + passedString);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: Called");
        String passedString = savedInstanceState.getString("OUT_STRING");
        Log.d(TAG, "onRestoreInstanceState: I got: " + passedString);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: Called");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: Called");
        super.onPause();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: Called");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: Called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: Called");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.opt_sensors:
                intent = new Intent(MainActivity.this, SensorsActivity.class);
                startActivity(intent);
                break;
            case R.id.opt_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.opt_camera:
                intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
                break;
            case R.id.opt_ochi:
                launchCoronaAlert();
                break;
            default:
        }
        return true;
    }

    private void launchCoronaAlert() {
        if (globalIsDarkMode) {
            alertDialogBuilder = new AlertDialog.Builder(this, R.style.DialogThemeDark);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(this, R.style.DialogThemeLight);
        }
        alertDialogBuilder.setTitle("Pericol de Covid-19.")
                .setPositiveButton("Da!", (dialog, id) -> {
                    Toast.makeText(this, "Ai luat covid 19...", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Nope!", (dialog, id) -> {
                    Toast.makeText(this, "Astepti sa se lanseze covid 20...", Toast.LENGTH_SHORT).show();
                });
        AlertDialog dialog;
        alertDialogBuilder.setMessage("Vrei sa te scarpini la ochi! Continui?");
        dialog = alertDialogBuilder.create();
        dialog.show();
    }
}
