package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Question;
import com.example.myapplication.models.Ticket;

import java.util.List;

public class MyTicketsAdapter extends RecyclerView.Adapter<MyTicketsAdapter.BaseViewHolder> {

    private List<Object> items;

    public MyTicketsAdapter(List<Object> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Object item = items.get(position);
        if (item instanceof Ticket) {
            ((TicketViewHolder) holder).bind((Ticket) item);
        } else if (item instanceof Question) {
            ((TicketViewHolder) holder).bind((Question) item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    abstract static class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class TicketViewHolder extends BaseViewHolder {
        private TextView tvTitle, tvContent, tvDate;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        public void bind(Ticket ticket) {
            String type = ticket.getType();
            String title = "";
            String content = "";

            switch (type) {
                case "repair":
                    title = "Ремонт - " + ticket.getCarModel();
                    content = "Описание: " + ticket.getDescription() + "\n" +
                            "Дата записи: " + ticket.getBookingDate() + " в " + ticket.getBookingTime();
                    break;
                case "painting":
                    title = "Покраска - " + ticket.getCarModel();
                    content = "Цвет: " + ticket.getColor() + "\n" +
                            "Дата записи: " + ticket.getBookingDate() + " в " + ticket.getBookingTime();
                    break;
                case "parts":
                    title = "Покупка запчасти";
                    content = "Запчасть: " + ticket.getPartName();
                    break;
            }

            tvTitle.setText(title);
            tvContent.setText(content);
            tvDate.setText("Создано: " + ticket.getCreatedDate());
        }

        public void bind(Question question) {
            tvTitle.setText("Вопрос: " + question.getTheme());
            if (question.getAnswer().isEmpty()) {
                tvContent.setText(question.getText() + "\n\nОтвет: Ожидается ответ...");
            } else {
                tvContent.setText(question.getText() + "\n\nОтвет: " + question.getAnswer());
            }
            tvDate.setText("Создано: " + question.getCreatedDate());
        }
    }
}