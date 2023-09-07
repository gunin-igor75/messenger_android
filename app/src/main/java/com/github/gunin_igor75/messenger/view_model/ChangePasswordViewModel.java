package com.github.gunin_igor75.messenger.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class ChangePasswordViewModel extends ViewModel {

    public static final String RESET_PASSWORD_LINK = "the reset password  link has been successfully sent";
    private MutableLiveData<String> message = new MutableLiveData<>();
    private final FirebaseAuth auth;

    public ChangePasswordViewModel() {
        auth = FirebaseAuth.getInstance();
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void changePassword(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> message.setValue(RESET_PASSWORD_LINK))
                .addOnFailureListener(ex -> message.setValue(ex.getMessage()));
    }
}
