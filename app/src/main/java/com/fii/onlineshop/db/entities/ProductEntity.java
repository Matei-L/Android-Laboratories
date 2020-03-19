package com.fii.onlineshop.db.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity(
        tableName = "products"
)
public class ProductEntity extends BaseEntity {
    private String description;
    private String name;
    private float price;

    public ProductEntity(String name, String description, float price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public String getPriceInDollars() {
        return price + "$";
    }

    public static List<ProductEntity> populateData() {
        List<ProductEntity> products = new ArrayList<>();
        products.add(new ProductEntity("Telefon", "Un telefon ff fain.", 300));
        products.add(new ProductEntity("Calculator", "Un calculator ff fain.", 1500));
        products.add(new ProductEntity("Mouse", "Un mouse ff fain.", 30));
        products.add(new ProductEntity("Keyboard", "Un keyboard ff fain.", 100));
        products.add(new ProductEntity("Iphone", "Un telefon ff fain.", 3000));
        return products;
    }
}
