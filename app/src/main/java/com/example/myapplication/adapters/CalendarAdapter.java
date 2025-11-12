package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MonthViewHolder> {

    private List<Calendar> months;
    private OnDateSelectedListener listener;

    public CalendarAdapter(List<Calendar> months, OnDateSelectedListener listener) {
        this.months = months;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month, parent, false);
        return new MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {
        Calendar month = months.get(position);
        holder.bind(month);
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    class MonthViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMonthYear;
        private ViewGroup calendarGrid;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonthYear = itemView.findViewById(R.id.tvMonthYear);
            calendarGrid = itemView.findViewById(R.id.calendarGrid);
        }

        public void bind(Calendar month) {
            String monthName = month.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("ru"));
            int year = month.get(Calendar.YEAR);
            tvMonthYear.setText(monthName + " " + year);

            calendarGrid.removeAllViews();

            Calendar firstDay = (Calendar) month.clone();
            firstDay.set(Calendar.DAY_OF_MONTH, 1);
            int daysInMonth = firstDay.getActualMaximum(Calendar.DAY_OF_MONTH);

            Calendar today = Calendar.getInstance();

            for (int day = 1; day <= daysInMonth; day++) {
                Calendar currentDate = (Calendar) firstDay.clone();
                currentDate.set(Calendar.DAY_OF_MONTH, day);

                int dayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
                boolean isWeekend = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
                boolean isPast = currentDate.before(today) && !isSameDay(currentDate, today);
                boolean isSelectable = !isPast && !isWeekend;

                View dayView = createDayView(day, isSelectable, isWeekend);
                calendarGrid.addView(dayView);
            }
        }

        private View createDayView(int day, boolean isSelectable, boolean isWeekend) {
            View dayView = LayoutInflater.from(itemView.getContext())
                    .inflate(R.layout.item_day, calendarGrid, false);

            TextView tvDay = dayView.findViewById(R.id.tvDay);
            tvDay.setText(String.valueOf(day));

            if (!isSelectable) {
                dayView.setBackgroundResource(R.drawable.bg_day_disabled);
                tvDay.setTextColor(itemView.getContext().getColor(android.R.color.darker_gray));
            } else if (isWeekend) {
                dayView.setBackgroundResource(R.drawable.bg_day_weekend);
                tvDay.setTextColor(itemView.getContext().getColor(android.R.color.holo_red_dark));
            } else {
                dayView.setBackgroundResource(R.drawable.bg_day_normal);
                tvDay.setTextColor(itemView.getContext().getColor(android.R.color.black));

                dayView.setOnClickListener(v -> {
                    Calendar selectedDate = (Calendar) months.get(getAdapterPosition()).clone();
                    selectedDate.set(Calendar.DAY_OF_MONTH, day);
                    String dateStr = String.format(Locale.getDefault(), "%02d.%02d.%d",
                            day, selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.YEAR));
                    if (listener != null) {
                        listener.onDateSelected(dateStr);
                    }
                });
            }

            return dayView;
        }

        private boolean isSameDay(Calendar cal1, Calendar cal2) {
            return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                    cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
        }
    }

    public interface OnDateSelectedListener {
        void onDateSelected(String date);
    }
}