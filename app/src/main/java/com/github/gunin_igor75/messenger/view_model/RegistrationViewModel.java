package com.github.gunin_igor75.messenger.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.gunin_igor75.messenger.dto.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationViewModel extends ViewModel {

    private final FirebaseAuth auth;

    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public RegistrationViewModel() {
        auth = FirebaseAuth.getInstance();
    }

    public void register(User userDto) {
        auth.createUserWithEmailAndPassword(userDto.getEmail(), userDto.getPassword())
                .addOnSuccessListener(authResult -> {
                    FirebaseUser currentUser = auth.getCurrentUser();
                    user.setValue(currentUser);
                })
                .addOnFailureListener(ex -> errorMessage.setValue(ex.getMessage()));
    }
}
