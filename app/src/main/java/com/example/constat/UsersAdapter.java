package com.example.constat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private List<User> users;
    private Context mContext;

    public UsersAdapter(Context context, List<User> users) {
        this.mContext = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_row, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.userName.setText(user.getFirstName() + " " + user.getLastName());

        holder.btnDelete.setOnClickListener(v -> {
            AppDatabase.getAppDatabase(mContext).userDao().delete(user);
            // Mettre à jour la liste après la suppression
            users.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, users.size());
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        Button btnDelete;

        UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
