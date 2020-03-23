package com.fii.onlineshop.ui.prosduct_info;

import android.os.Bundle;
import android.widget.TextView;

import com.fii.onlineshop.R;
import com.fii.onlineshop.db.ProductsDatabase;
import com.fii.onlineshop.db.dao.ProductDao;
import com.fii.onlineshop.db.entities.ProductEntity;
import com.fii.onlineshop.ui.BaseActivity;

public class ProductInfoActivity extends BaseActivity {

    private TextView name;
    private TextView price;
    private TextView description;
    private ProductEntity product;

    private ProductsDatabase productsDatabase;
    private ProductDao productDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        name = findViewById(R.id.product_name);
        price = findViewById(R.id.product_price);
        description = findViewById(R.id.product_description);

        initDatabaseConnection();

        int id = getIntent().getIntExtra("id", 0);
        productDao.getProduct(id).observe(this, (product) -> {
            name.setText(product.getName());
            price.setText(product.getPriceInDollars());
            description.setText(product.getDescription());
        });
    }

    private void initDatabaseConnection() {
        productsDatabase = ProductsDatabase.getInstance(this);
        productDao = productsDatabase.productDao();
    }

}
