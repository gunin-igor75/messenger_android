package com.github.gunin_igor75.messenger.view_model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.gunin_igor75.messenger.pojo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserViewModel extends ViewModel {
    private FirebaseAuth auth;

    private FirebaseDatabase database;

    private DatabaseReference myRef;
    private MutableLiveData<FirebaseUser> firebaseUser = new MutableLiveData<>();

    private MutableLiveData<List<User>> users = new MutableLiveData<>();

    public LiveData<FirebaseUser> getFirebaseUser() {
        return firebaseUser;
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public UserViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(firebaseAuth -> firebaseUser.setValue(firebaseAuth.getCurrentUser()));
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        getUsersFromDb();
    }



    public void logout() {
        setStatusOnline(false);
        auth.signOut();
    }

    public void setStatusOnline(boolean isOnline) {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        myRef.child(userId).child("isOnline").setValue(isOnline);
    }

    private void getUsersFromDb() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> usersFromDb = new ArrayList<>();
                FirebaseUser authUser = auth.getCurrentUser();
                for (DataSnapshot child : snapshot.getChildren()) {

                    User user = child.getValue(User.class);
                    if (authUser != null && user != null && !user.getId().equals(authUser.getUid())) {
                        usersFromDb.add(user);
                    }
                }
                users.setValue(usersFromDb);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("UserViewModel", error.getMessage());
            }
        });
    }
}
