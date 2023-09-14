package com.github.gunin_igor75.messenger.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class ChangePasswordViewModel extends ViewModel {

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSendResetPassword = new MutableLiveData<>(false);
    private final FirebaseAuth auth;

    public ChangePasswordViewModel() {
        auth = FirebaseAuth.getInstance();
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsSendResetPassword() {
        return isSendResetPassword;
    }

    public void changePassword(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> isSendResetPassword.setValue(true))
                .addOnFailureListener(ex -> errorMessage.setValue(ex.getMessage()));
    }
}
