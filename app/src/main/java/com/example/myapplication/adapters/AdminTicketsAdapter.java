package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Question;
import com.example.myapplication.models.Ticket;

import java.util.ArrayList;
import java.util.List;

public class AdminTicketsAdapter extends RecyclerView.Adapter<AdminTicketsAdapter.BaseViewHolder> {

    private List<Object> items;
    private List<Object> filteredItems;
    private OnAnswerClickListener answerListener;
    private OnBlockUserClickListener blockUserListener;

    public AdminTicketsAdapter(List<Object> items, OnAnswerClickListener answerListener,
                               OnBlockUserClickListener blockUserListener) {
        this.items = items;
        this.filteredItems = new ArrayList<>(items);
        this.answerListener = answerListener;
        this.blockUserListener = blockUserListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_ticket, parent, false);
            return new TicketViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_question, parent, false);
            return new QuestionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Object item = filteredItems.get(position);
        if (item instanceof Ticket) {
            ((TicketViewHolder) holder).bind((Ticket) item);
        } else if (item instanceof Question) {
            ((QuestionViewHolder) holder).bind((Question) item);
        }
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = filteredItems.get(position);
        return (item instanceof Ticket) ? 0 : 1;
    }

    public void filter(String search) {
        filteredItems.clear();
        if (search.isEmpty()) {
            filteredItems.addAll(items);
        } else {
            String searchLower = search.toLowerCase();
            for (Object item : items) {
                if (item instanceof Ticket) {
                    Ticket ticket = (Ticket) item;
                    if (ticket.getUserName().toLowerCase().contains(searchLower) ||
                            ticket.getUserPhone().contains(search)) {
                        filteredItems.add(item);
                    }
                } else if (item instanceof Question) {
                    Question question = (Question) item;
                    if (question.getUserName().toLowerCase().contains(searchLower) ||
                            question.getUserPhone().contains(search)) {
                        filteredItems.add(item);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    abstract static class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class TicketViewHolder extends BaseViewHolder {
        private TextView tvTitle, tvContent, tvUserInfo;
        private Button btnBlockUser;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvUserInfo = itemView.findViewById(R.id.tvUserInfo);
            btnBlockUser = itemView.findViewById(R.id.btnBlockUser);
        }

        public void bind(Ticket ticket) {
            String type = ticket.getType();
            String title = "";
            String content = "";

            switch (type) {
                case "repair":
                    title = "Ремонт";
                    content = "Модель: " + ticket.getCarModel() + "\n" +
                            "Описание: " + ticket.getDescription() + "\n" +
                            "Запись: " + ticket.getBookingDate() + " в " + ticket.getBookingTime();
                    break;
                case "painting":
                    title = "Покраска";
                    content = "Модель: " + ticket.getCarModel() + "\n" +
                            "Цвет: " + ticket.getColor() + "\n" +
                            "Запись: " + ticket.getBookingDate() + " в " + ticket.getBookingTime();
                    break;
                case "parts":
                    title = "Запчасти";
                    content = "Запчасть: " + ticket.getPartName();
                    break;
            }

            tvTitle.setText(title);
            tvContent.setText(content);
            tvUserInfo.setText("Пользователь: " + ticket.getUserName() + " (" + ticket.getUserPhone() + ")");

            btnBlockUser.setOnClickListener(v -> {
                if (blockUserListener != null) {
                    blockUserListener.onBlockUserClick(ticket.getUserId());
                }
            });
        }
    }

    class QuestionViewHolder extends BaseViewHolder {
        private TextView tvTitle, tvContent, tvUserInfo, tvAnswer;
        private Button btnAnswer, btnBlockUser;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvUserInfo = itemView.findViewById(R.id.tvUserInfo);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            btnAnswer = itemView.findViewById(R.id.btnAnswer);
            btnBlockUser = itemView.findViewById(R.id.btnBlockUser);
        }

        public void bind(Question question) {
            tvTitle.setText("Вопрос: " + question.getTheme());
            tvContent.setText(question.getText());
            tvUserInfo.setText("Пользователь: " + question.getUserName() + " (" + question.getUserPhone() + ")");

            if (question.getAnswer().isEmpty()) {
                tvAnswer.setText("Ответ: Ожидается ответ...");
            } else {
                tvAnswer.setText("Ответ: " + question.getAnswer());
            }

            btnAnswer.setOnClickListener(v -> {
                if (answerListener != null) {
                    answerListener.onAnswerClick(question);
                }
            });

            btnBlockUser.setOnClickListener(v -> {
                if (blockUserListener != null) {
                    blockUserListener.onBlockUserClick(question.getUserId());
                }
            });
        }
    }

    public interface OnAnswerClickListener {
        void onAnswerClick(Question question);
    }

    public interface OnBlockUserClickListener {
        void onBlockUserClick(int userId);
    }
}