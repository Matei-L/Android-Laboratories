package com.fii.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.fii.onlineshop.models.Product;

public class ProductInfoActivity extends AppCompatActivity {

    TextView name;
    TextView price;
    TextView description;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        name = findViewById(R.id.product_name);
        price = findViewById(R.id.product_price);
        description = findViewById(R.id.product_description);
        name.setText(getIntent().getExtras().get("name").toString());
        price.setText(getIntent().getExtras().get("price").toString());
        description.setText(getIntent().getExtras().get("desc").toString());
    }
}
