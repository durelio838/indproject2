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

import java.util.List;

public class AdminPartsAdapter extends RecyclerView.Adapter<AdminPartsAdapter.PartViewHolder> {

    private List<Part> parts;
    private OnDeleteClickListener listener;

    public AdminPartsAdapter(List<Part> parts, OnDeleteClickListener listener) {
        this.parts = parts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_part, parent, false);
        return new PartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartViewHolder holder, int position) {
        Part part = parts.get(position);
        holder.bind(part);
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

    class PartViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPartName, tvCarModel, tvPrice;
        private Button btnDelete;

        public PartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPartName = itemView.findViewById(R.id.tvPartName);
            tvCarModel = itemView.findViewById(R.id.tvCarModel);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Part part) {
            tvPartName.setText(part.getName());
            tvCarModel.setText("Модель: " + part.getCarModel());
            tvPrice.setText("Цена: " + part.getPrice() + " ₽");

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(part);
                }
            });
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Part part);
    }
}