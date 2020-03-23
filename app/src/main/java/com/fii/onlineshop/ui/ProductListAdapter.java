package com.fii.onlineshop.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fii.onlineshop.R;
import com.fii.onlineshop.db.entities.ProductEntity;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private List<ProductEntity> items;
    private OnClickListeners onClickListeners;

    public ProductListAdapter(List<ProductEntity> items) {
        this.items = items;
    }

    public void setOnClickListeners(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        return new ProductListAdapter.ViewHolder(view, this.onClickListeners);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        OnClickListeners onClickListeners;

        ViewHolder(@NonNull View itemView, OnClickListeners onClickListeners) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            this.onClickListeners = onClickListeners;
            setupOnItemClickListener(itemView, onClickListeners);
        }

        void bindTo(ProductEntity product) {
            name.setText(product.getName());
            price.setText(product.getPriceInDollars());
        }

        void setupOnItemClickListener(@NonNull View itemView,
                                      final OnClickListeners onClickListeners) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListeners != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onClickListeners.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public interface OnClickListeners {
        void onItemClick(int position);
    }
}
