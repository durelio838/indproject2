package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Part;

import java.util.ArrayList;
import java.util.List;

public class PartsAdapter extends RecyclerView.Adapter<PartsAdapter.PartViewHolder> {

    private List<Part> parts;
    private List<Part> filteredParts;
    private OnPartClickListener listener;

    public PartsAdapter(List<Part> parts, OnPartClickListener listener) {
        this.parts = parts;
        this.filteredParts = new ArrayList<>(parts);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part, parent, false);
        return new PartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartViewHolder holder, int position) {
        Part part = filteredParts.get(position);
        holder.bind(part);
    }

    @Override
    public int getItemCount() {
        return filteredParts.size();
    }

    public void filter(String search) {
        filteredParts.clear();
        if (search.isEmpty()) {
            filteredParts.addAll(parts);
        } else {
            for (Part part : parts) {
                if (part.getName().toLowerCase().contains(search.toLowerCase())) {
                    filteredParts.add(part);
                }
            }
        }
        notifyDataSetChanged();
    }

    class PartViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPartName, tvCarModel, tvPrice;
        private Button btnOrder;

        public PartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPartName = itemView.findViewById(R.id.tvPartName);
            tvCarModel = itemView.findViewById(R.id.tvCarModel);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnOrder = itemView.findViewById(R.id.btnOrder);
        }

        public void bind(Part part) {
            tvPartName.setText(part.getName());
            tvCarModel.setText("Модель: " + part.getCarModel());
            tvPrice.setText("Цена: " + part.getPrice() + " ₽");

            btnOrder.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPartClick(part);
                }
            });
        }
    }

    public interface OnPartClickListener {
        void onPartClick(Part part);
    }
}