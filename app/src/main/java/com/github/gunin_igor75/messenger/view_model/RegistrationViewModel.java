package com.github.gunin_igor75.messenger.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.gunin_igor75.messenger.dto.UserDto;
import com.github.gunin_igor75.messenger.pojo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationViewModel extends ViewModel {

    private final FirebaseAuth auth;

    private FirebaseDatabase database;

    private DatabaseReference myRef;

    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public RegistrationViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                firebaseUserMutableLiveData.setValue(currentUser);
            }
        });
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
    }

    public void register(UserDto userDto) {
        auth.createUserWithEmailAndPassword(userDto.getEmail(), userDto.getPassword())
                .addOnSuccessListener(authResult -> {
                    FirebaseUser authResultUser = authResult.getUser();
                    if (authResultUser != null) {
                        User user = User.userDtoToUser(userDto);
                        myRef.child(authResultUser.getUid()).setValue(user);
                    }
                })
                .addOnFailureListener(ex -> errorMessage.setValue(ex.getMessage()));
    }
}
