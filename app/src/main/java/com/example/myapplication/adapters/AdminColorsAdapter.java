package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Color;

import java.util.List;

public class AdminColorsAdapter extends RecyclerView.Adapter<AdminColorsAdapter.ColorViewHolder> {

    private List<Color> colors;
    private OnDeleteClickListener listener;

    public AdminColorsAdapter(List<Color> colors, OnDeleteClickListener listener) {
        this.colors = colors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_color, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        Color color = colors.get(position);
        holder.bind(color);
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    class ColorViewHolder extends RecyclerView.ViewHolder {
        private View colorView;
        private TextView tvColorName, tvColorCode, tvColorPrice;
        private Button btnDelete;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.colorView);
            tvColorName = itemView.findViewById(R.id.tvColorName);
            tvColorCode = itemView.findViewById(R.id.tvColorCode);
            tvColorPrice = itemView.findViewById(R.id.tvColorPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Color color) {
            try {
                colorView.setBackgroundColor(android.graphics.Color.parseColor(color.getCode()));
            } catch (Exception e) {
                colorView.setBackgroundColor(android.graphics.Color.BLACK);
            }
            tvColorName.setText(color.getName());
            tvColorCode.setText("Код: " + color.getCode());
            tvColorPrice.setText("Примерная цена: ~" + color.getPrice() + " ₽");

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(color);
                }
            });
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Color color);
    }
}