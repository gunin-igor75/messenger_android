package com.github.gunin_igor75.messenger.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.gunin_igor75.messenger.dto.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainViewModel extends ViewModel {
    private final FirebaseAuth auth;

    private static final String TAG = "MainViewModel";
    private final MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MainViewModel() {
        auth = FirebaseAuth.getInstance();
    }

    public void login(User userDto) {
        auth.signInWithEmailAndPassword(userDto.getEmail(), userDto.getPassword())
                .addOnSuccessListener(authResult -> {
                    FirebaseUser currentUser = auth.getCurrentUser();
                    user.setValue(currentUser);
                })
                .addOnFailureListener(ex -> errorMessage.setValue(ex.getMessage()));
    }
}
