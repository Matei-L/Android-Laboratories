package com.fii.onlineshop.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.fii.onlineshop.db.entities.BaseEntity;
import com.fii.onlineshop.db.entities.ProductEntity;

import java.util.List;

@Dao
public abstract class ProductDao extends BaseDao<ProductEntity> {
    @Query("SELECT * FROM products")
    public abstract LiveData<List<ProductEntity>> getProducts();

    @Query("SELECT * FROM products WHERE id = :id")
    public abstract LiveData<ProductEntity> getProduct(int id);
}
