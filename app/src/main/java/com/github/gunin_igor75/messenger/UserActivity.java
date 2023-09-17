package com.github.gunin_igor75.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gunin_igor75.messenger.adapter.UsersAdapter;
import com.github.gunin_igor75.messenger.pojo.User;
import com.github.gunin_igor75.messenger.view_model.UserViewModel;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {

    private UserViewModel viewModel;

    private final static String CURRENT_USER_ID = "currentUserId";
    private RecyclerView recyclerViewUsers;
    private UsersAdapter usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
        observeViewModel();
        String currentUserId = getIntent().getStringExtra(CURRENT_USER_ID);
        usersAdapter.setUserClickListener(new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                Intent intent = ChatActivity.newIntent(
                        UserActivity.this,
                        currentUserId,
                        user.getId());
                startActivity(intent);
            }
        });
    }

    public static Intent newIntent(Context context, String idSender) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(CURRENT_USER_ID, idSender);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemLogout) {
            viewModel.logout();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        viewModel.setStatusOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.setStatusOnline(false);
    }

    private void init() {
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        usersAdapter = new UsersAdapter();
        recyclerViewUsers.setAdapter(usersAdapter);
    }

    private void observeViewModel() {
        viewModel.getFirebaseUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                if (user == null) {
                    Intent intent = LoginActivity.newIntent(UserActivity.this);
                    startActivity(intent);
                    finish();
                }
            }
        });

        viewModel.getUsers().observe(this, users -> usersAdapter.setUsers(users));
    }
}