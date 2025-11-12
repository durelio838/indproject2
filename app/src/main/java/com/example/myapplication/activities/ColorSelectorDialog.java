package com.example.myapplication.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.models.Color;
import java.util.List;

public class ColorSelectorDialog {

    private Context context;
    private List<Color> colors;
    private OnColorSelectedListener listener;

    public interface OnColorSelectedListener {
        void onColorSelected(Color color);
    }

    public ColorSelectorDialog(Context context, List<Color> colors, OnColorSelectedListener listener) {
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
        private List<Color> colors;
        private OnColorSelectedListener listener;

        ColorAdapter(List<Color> colors, OnColorSelectedListener listener) {
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
            Color color = colors.get(position);
            holder.tvColorName.setText(color.getName());
            holder.tvColorCode.setText(color.getCode());
            holder.tvColorPrice.setText("Примерная цена: ~" + color.getApproximatePrice() + " ₽");


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

            ColorViewHolder(View itemView) {
                super(itemView);
                tvColorName = itemView.findViewById(R.id.tvColorName);
                tvColorCode = itemView.findViewById(R.id.tvColorCode);
                tvColorPrice = itemView.findViewById(R.id.tvColorPrice);
            }
        }
    }
}
