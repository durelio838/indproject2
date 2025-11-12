package com.example.myapplication.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.List;

public class ColorSelectorDialog {

    private Context context;
    private List<com.example.myapplication.models.Color> colors;
    private OnColorSelectedListener listener;

    public interface OnColorSelectedListener {
        void onColorSelected(com.example.myapplication.models.Color color);
    }

    public ColorSelectorDialog(Context context, List<com.example.myapplication.models.Color> colors, OnColorSelectedListener listener) {
        this.context = context;
        this.colors = colors;
        this.listener = listener;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Выберите цвет");

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_color_selector, null);
        builder.setView(dialogView);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerColors);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        AlertDialog dialog = builder.create();

        ColorAdapter adapter = new ColorAdapter(colors, color -> {
            if (listener != null) {
                listener.onColorSelected(color);
            }
            dialog.dismiss();
        });

        recyclerView.setAdapter(adapter);
        dialog.show();
    }

    private static class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
        private List<com.example.myapplication.models.Color> colors;
        private OnColorSelectedListener listener;

        ColorAdapter(List<com.example.myapplication.models.Color> colors, OnColorSelectedListener listener) {
            this.colors = colors;
            this.listener = listener;
        }

        @Override
        public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_color_selector, parent, false);
            return new ColorViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ColorViewHolder holder, int position) {
            com.example.myapplication.models.Color color = colors.get(position);
            holder.tvColorName.setText(color.getName());
            holder.tvColorCode.setText(color.getCode());
            holder.tvColorPrice.setText("Примерная цена: ~" + color.getApproximatePrice() + " ₽");

            // Устанавливаем цвет для визуального индикатора
            try {
                int colorInt = Color.parseColor(color.getCode());
                holder.viewColorIndicator.setBackgroundColor(colorInt);
            } catch (IllegalArgumentException e) {
                // Если не удалось распарсить цвет, ставим серый
                holder.viewColorIndicator.setBackgroundColor(Color.GRAY);
            }

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onColorSelected(color);
                }
            });
        }

        @Override
        public int getItemCount() {
            return colors.size();
        }

        static class ColorViewHolder extends RecyclerView.ViewHolder {
            TextView tvColorName, tvColorCode, tvColorPrice;
            View viewColorIndicator;

            ColorViewHolder(View itemView) {
                super(itemView);
                tvColorName = itemView.findViewById(R.id.tvColorName);
                tvColorCode = itemView.findViewById(R.id.tvColorCode);
                tvColorPrice = itemView.findViewById(R.id.tvColorPrice);
                viewColorIndicator = itemView.findViewById(R.id.viewColorIndicator);
            }
        }
    }
}
