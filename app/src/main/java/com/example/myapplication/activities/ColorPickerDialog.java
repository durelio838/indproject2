package com.example.myapplication.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Color;

public class ColorPickerDialog {

    private Context context;
    private OnColorAddedListener listener;

    public interface OnColorAddedListener {
        void onColorAdded(Color color);
    }

    public ColorPickerDialog(Context context, OnColorAddedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Добавить цвет");

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_color, null);
        builder.setView(dialogView);

        EditText etColorCode = dialogView.findViewById(R.id.etColorCode);
        EditText etColorName = dialogView.findViewById(R.id.etColorName);
        EditText etColorPrice = dialogView.findViewById(R.id.etColorPrice);
        Button btnSaveColor = dialogView.findViewById(R.id.btnSaveColor);

        AlertDialog dialog = builder.create();

        btnSaveColor.setOnClickListener(v -> {
            String code = etColorCode.getText().toString().trim();
            String name = etColorName.getText().toString().trim();
            String price = etColorPrice.getText().toString().trim();

            if (code.isEmpty() || name.isEmpty() || price.isEmpty()) {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            Color color = new Color(code, name, price);
            if (listener != null) {
                listener.onColorAdded(color);
            }
            dialog.dismiss();
        });

        dialog.show();
    }
}
