package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.User;

import java.util.ArrayList;
import java.util.List;

public class AdminProfilesAdapter extends RecyclerView.Adapter<AdminProfilesAdapter.ProfileViewHolder> {

    private List<User> users;
    private List<User> filteredUsers;
    private OnBlockUserClickListener listener;

    public AdminProfilesAdapter(List<User> users, OnBlockUserClickListener listener) {
        this.users = users;
        this.filteredUsers = new ArrayList<>(users);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        User user = filteredUsers.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return filteredUsers.size();
    }

    public void filter(String search) {
        filteredUsers.clear();
        if (search.isEmpty()) {
            filteredUsers.addAll(users);
        } else {
            String searchLower = search.toLowerCase();
            for (User user : users) {
                if (user.getName().toLowerCase().contains(searchLower) ||
                        user.getPhone().contains(search)) {
                    filteredUsers.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPhone, tvPassword, tvTicketsCount, tvStatus;
        private Button btnBlock;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvPassword = itemView.findViewById(R.id.tvPassword);
            tvTicketsCount = itemView.findViewById(R.id.tvTicketsCount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnBlock = itemView.findViewById(R.id.btnBlock);
        }

        public void bind(User user) {
            tvName.setText(user.getName());
            tvPhone.setText("Номер: " + user.getPhone());
            tvPassword.setText("Пароль: " + "*".repeat(user.getPassword().length()));
            tvTicketsCount.setText("Количество заявок: " + user.getTicketCount());

            if (user.isBlocked()) {
                tvStatus.setText("Статус: Заблокирован");
                tvStatus.setTextColor(itemView.getContext().getColor(android.R.color.holo_red_dark));
                btnBlock.setVisibility(View.GONE);
            } else {
                tvStatus.setText("Статус: Активен");
                tvStatus.setTextColor(itemView.getContext().getColor(android.R.color.holo_green_dark));
                btnBlock.setVisibility(View.VISIBLE);
                btnBlock.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onBlockUserClick(user.getId());
                    }
                });
            }
        }
    }

    public interface OnBlockUserClickListener {
        void onBlockUserClick(int userId);
    }
}