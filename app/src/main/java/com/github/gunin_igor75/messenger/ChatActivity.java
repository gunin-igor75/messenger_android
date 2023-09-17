package com.github.gunin_igor75.messenger;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gunin_igor75.messenger.adapter.MessageAdapter;
import com.github.gunin_igor75.messenger.adapter.UsersAdapter;
import com.github.gunin_igor75.messenger.factory_view_model.ChatViewModelFactory;
import com.github.gunin_igor75.messenger.pojo.Message;
import com.github.gunin_igor75.messenger.pojo.User;
import com.github.gunin_igor75.messenger.view_model.ChatViewModel;

public class ChatActivity extends AppCompatActivity {


    public static final String CURRENT_USER_ID = "currentUserId";
    public static final String OTHER_USER_ID = "otherUserId";
    private TextView textViewChatUser;
    private View viewChatUserOnline;
    private RecyclerView recyclerViewChat;
    private EditText editTextChat;
    private ImageView imageViewSendMessage;

    private ChatViewModel viewModel;

    private ChatViewModelFactory chatViewModelFactory;
    private String currentUserId;
    private String otherUserId;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        observeViewModel();
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextChat.getText().toString().trim();
                Message message = new Message(text, currentUserId, otherUserId);
                viewModel.saveMessage(message);
            }
        });
    }

    public static Intent newIntent(Context context, String currentUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(CURRENT_USER_ID, currentUserId);
        intent.putExtra(OTHER_USER_ID, otherUserId);
        return intent;
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
        textViewChatUser = findViewById(R.id.textViewChatUser);
        viewChatUserOnline = findViewById(R.id.viewChatUserOnline);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextChat = findViewById(R.id.editTextChat);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
        messageAdapter = new MessageAdapter(currentUserId);
        recyclerViewChat.setAdapter(messageAdapter);
        currentUserId = getIntent().getStringExtra(CURRENT_USER_ID);
        otherUserId = getIntent().getStringExtra(OTHER_USER_ID);
        chatViewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        viewModel = new ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel.class);
    }

    private void observeViewModel() {
        viewModel.getMessages().observe(this, messages -> messageAdapter.setMessages(messages));
        viewModel.getErrorMessage().observe(this, message -> Toast.makeText(
                ChatActivity.this,
                message,
                Toast.LENGTH_SHORT).show());
        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String text = String.format("%s %s",user.getName(), user.getLastname());
                textViewChatUser.setText(text);
                Drawable drawable = getDrawable(user.isOnline(), ChatActivity.this);
                viewChatUserOnline.setBackground(drawable);
            }
        });
        viewModel.getIsSend().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSend) {
                if (isSend) {
                    editTextChat.setText("");
                }
            }
        });
    }
    private Drawable getDrawable(boolean isOnline, Context context) {
        int idBackGround;
        if (isOnline) {
            idBackGround = R.drawable.presence_green;
        } else {
            idBackGround = R.drawable.presence_red;
        }
        return ContextCompat.getDrawable(context, idBackGround);
    }
}