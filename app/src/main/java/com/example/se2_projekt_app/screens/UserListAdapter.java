package com.example.se2_projekt_app.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.se2_projekt_app.R;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    // Use a final modifier for the userList as it should not change once it's set through the constructor
    private final List<User> userList;

    public UserListAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater should be obtained from the context of the parent to ensure it matches the theme and context of the parent
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Using a local variable for user to ensure that we don't call get(position) multiple times
        final User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0; // Ensure null safety
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView usernameTextView; // Mark member as final since it's not expected to change

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
        }

        // Encapsulate the binding logic within the ViewHolder to keep onBindViewHolder clean and focused
        public void bind(final User user) {
            usernameTextView.setText(user.getUsername());
        }
    }
}