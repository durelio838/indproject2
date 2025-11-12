package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.flexbox.FlexboxLayout;

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
        private FlexboxLayout calendarGrid;

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
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            for (int day = 1; day <= daysInMonth; day++) {
                Calendar currentDate = (Calendar) firstDay.clone();
                currentDate.set(Calendar.DAY_OF_MONTH, day);
                currentDate.set(Calendar.HOUR_OF_DAY, 0);
                currentDate.set(Calendar.MINUTE, 0);
                currentDate.set(Calendar.SECOND, 0);
                currentDate.set(Calendar.MILLISECOND, 0);

                int dayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
                boolean isWeekend = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
                boolean isPast = currentDate.before(today);

                // НЕ показываем квадратики для прошедших дат и выходных
                if (isPast || isWeekend) {
                    continue;
                }

                // Показываем только доступные дни
                View dayView = createDayView(day);
                calendarGrid.addView(dayView);
            }
        }

        private View createDayView(int day) {
            View dayView = LayoutInflater.from(itemView.getContext())
                    .inflate(R.layout.item_day, calendarGrid, false);

            TextView tvDay = dayView.findViewById(R.id.tvDay);
            tvDay.setText(String.valueOf(day));

            dayView.setOnClickListener(v -> {
                Calendar selectedDate = (Calendar) months.get(getAdapterPosition()).clone();
                selectedDate.set(Calendar.DAY_OF_MONTH, day);
                String dateStr = String.format(Locale.getDefault(), "%02d.%02d.%d",
                        day, selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.YEAR));
                if (listener != null) {
                    listener.onDateSelected(dateStr);
                }
            });

            return dayView;
        }
    }

    public interface OnDateSelectedListener {
        void onDateSelected(String date);
    }
}
