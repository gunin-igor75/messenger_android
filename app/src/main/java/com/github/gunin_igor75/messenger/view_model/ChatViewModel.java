package com.github.gunin_igor75.messenger.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.gunin_igor75.messenger.pojo.Message;
import com.github.gunin_igor75.messenger.pojo.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSend = new MutableLiveData<>(false);
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefUsers = database.getReference("users");
    private DatabaseReference myRefMessages = database.getReference("messages");
    private String currentUserId;
    private String otherUserId;

    public ChatViewModel(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
        myRefUsers.child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User otherUser = (User) snapshot.getValue();
                user.setValue(otherUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorMessage.setValue(error.getMessage());
            }
        });
        myRefMessages.child(currentUserId).child(otherUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Message> messagesFromDb = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Message message = (Message) child.getValue();
                            messagesFromDb.add(message);
                        }
                        messages.setValue(messagesFromDb);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        errorMessage.setValue(error.getMessage());
                    }
                });
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<Boolean> getIsSend() {
        return isSend;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setStatusOnline(boolean isOnline) {
        myRefUsers.child(currentUserId).child("isOnline").setValue(isOnline);
    }
    public void saveMessage(Message message) {
        myRefMessages.child(message.getSenderId())
                .child(message.getRecipientId())
                .push()
                .setValue(message)
                .addOnSuccessListener(unused -> myRefMessages.child(message.getRecipientId())
                        .child(message.getSenderId())
                        .push()
                        .setValue(message)
                        .addOnSuccessListener(unused1 -> isSend.setValue(true))
                        .addOnFailureListener(e -> errorMessage.setValue(e.getMessage())))
                .addOnFailureListener(e -> errorMessage.setValue(e.getMessage()));
    }
}
