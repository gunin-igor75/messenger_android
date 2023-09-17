package com.github.gunin_igor75.messenger.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gunin_igor75.messenger.R;
import com.github.gunin_igor75.messenger.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> users = new ArrayList<>();

    private OnUserClickListener userClickListener;

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void setUserClickListener(OnUserClickListener userClickListener) {
        this.userClickListener = userClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_user,
                parent,
                false
        );
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        String infoUser =
                String.format("%s %s %s",
                        user.getName(),
                        user.getLastname(),
                        user.getAge());
        holder.textViewUser.setText(infoUser);
        Drawable drawable = getDrawable(user.isOnline(), holder);
        holder.viewPresence.setBackground(drawable);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userClickListener != null) {
                    userClickListener.onUserClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewUser;
        private final View viewPresence;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUser = itemView.findViewById(R.id.textViewUser);
            viewPresence = itemView.findViewById(R.id.viewPresence);
        }
    }

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    private Drawable getDrawable(boolean isOnline, UserViewHolder holder) {
        int idBackGround;
        if (isOnline) {
            idBackGround = R.drawable.presence_green;
        } else {
            idBackGround = R.drawable.presence_red;
        }
        return ContextCompat.getDrawable(holder.itemView.getContext(), idBackGround);
    }
}
