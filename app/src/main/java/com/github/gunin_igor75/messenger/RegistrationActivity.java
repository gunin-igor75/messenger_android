package com.github.gunin_igor75.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.gunin_igor75.messenger.dto.UserDto;
import com.github.gunin_igor75.messenger.view_model.RegistrationViewModel;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextEmailRegistration;
    private EditText editTextPasswordRegistration;
    private EditText editTextNameRegistration;
    private EditText editTextLastNameRegistration;
    private EditText editTextAgeRegistration;
    private Button buttonSignUp;

    private RegistrationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
        observeViewModel();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDto userDto = new UserDto();
                userDto.setEmail(getStringFormat(editTextEmailRegistration));
                userDto.setName(getStringFormat(editTextNameRegistration));
                userDto.setLastname(getStringFormat(editTextLastNameRegistration));
                userDto.setPassword(getStringFormat(editTextPasswordRegistration));
                userDto.setAge(Integer.parseInt(getStringFormat(editTextAgeRegistration)));
                viewModel.register(userDto);
            }
        });
    }

    private void observeViewModel() {
        viewModel.getFirebaseUserMutableLiveData().observe(RegistrationActivity.this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                if (user != null) {
                    Intent intent = UserActivity.newIntent(RegistrationActivity.this, user.getUid());
                    startActivity(intent);
                    finish();
                }
            }
        });

        viewModel.getErrorMessage().observe(RegistrationActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null) {
                    Toast.makeText(
                            RegistrationActivity.this,
                            message,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    @NonNull
    private String getStringFormat(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, RegistrationActivity.class);
    }

    private void init() {
        editTextEmailRegistration = findViewById(R.id.editTextEmailRegistration);
        editTextPasswordRegistration = findViewById(R.id.editTextPasswordRegistration);
        editTextNameRegistration = findViewById(R.id.editTextNameRegistration);
        editTextLastNameRegistration = findViewById(R.id.editTextLastNameRegistration);
        editTextAgeRegistration = findViewById(R.id.editTextAgeRegistration);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
    }
}