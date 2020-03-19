package com.fii.onlineshop.models;

import java.io.Serializable;

public class Product implements Serializable {
    private String description;
    private String name;
    private float price;

    public Product(String name, String description, float price) {
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

    public String getPrice() {
        return price + "$";
    }
}
