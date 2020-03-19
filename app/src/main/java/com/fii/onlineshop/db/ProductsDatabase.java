package com.fii.onlineshop.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.fii.onlineshop.db.dao.ProductDao;
import com.fii.onlineshop.db.entities.ProductEntity;

import java.util.concurrent.Executors;

import static android.appwidget.AppWidgetManager.getInstance;

@androidx.room.Database(
        entities = {
                ProductEntity.class,
        },
        version = 1
)
public abstract class ProductsDatabase extends RoomDatabase {
    private static ProductsDatabase INSTANCE;

    public abstract ProductDao productDao();

    public synchronized static ProductsDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static ProductsDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                ProductsDatabase.class,
                "ProductsDatabase.db")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() ->
                                getInstance(context)
                                        .productDao().insert(ProductEntity.populateData()));
                    }
                })
                .build();
    }
}

