package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private List<String> timeSlots;
    private OnTimeSlotClickListener listener;

    public TimeSlotAdapter(List<String> timeSlots, OnTimeSlotClickListener listener) {
        this.timeSlots = timeSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        String time = timeSlots.get(position);
        holder.bind(time);
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
        }

        public void bind(String time) {
            tvTime.setText(time);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTimeSlotClick(time);
                }
            });
        }
    }

    public interface OnTimeSlotClickListener {
        void onTimeSlotClick(String time);
    }
}