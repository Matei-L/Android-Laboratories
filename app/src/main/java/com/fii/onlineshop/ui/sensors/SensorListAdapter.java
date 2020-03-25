package com.fii.onlineshop.ui.sensors;

import android.hardware.Sensor;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fii.onlineshop.R;
import com.fii.onlineshop.db.entities.ProductEntity;

import java.util.Arrays;
import java.util.List;

public class SensorListAdapter extends RecyclerView.Adapter<SensorListAdapter.ViewHolder> {

    private List<Pair<Sensor, List<Float>>> items;
    private OnClickListeners onClickListeners;

    public SensorListAdapter(List<Pair<Sensor, List<Float>>> items) {
        this.items = items;
    }

    public void setOnClickListeners(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_list_item, parent, false);
        return new SensorListAdapter.ViewHolder(view, this.onClickListeners);
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
        TextView details;
        OnClickListeners onClickListeners;

        ViewHolder(@NonNull View itemView, OnClickListeners onClickListeners) {
            super(itemView);
            name = itemView.findViewById(R.id.sensor_name);
            details = itemView.findViewById(R.id.sensor_details);
            this.onClickListeners = onClickListeners;
            setupOnItemClickListener(itemView, onClickListeners);
        }

        void bindTo(Pair<Sensor, List<Float>> sensor) {
            name.setText(sensor.first.getName());
            details.setText(Arrays.toString(sensor.second.toArray()));
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
