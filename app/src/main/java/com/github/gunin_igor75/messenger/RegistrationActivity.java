package com.github.gunin_igor75.messenger;

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

import com.github.gunin_igor75.messenger.dto.User;
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

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userDto = new User();
                userDto.setEmail(editTextEmailRegistration.getText().toString().trim());
                userDto.setName(editTextNameRegistration.getText().toString().trim());
                userDto.setLastname(editTextLastNameRegistration.getText().toString().trim());
                userDto.setPassword(editTextPasswordRegistration.getText().toString().trim());
                userDto.setAge(Integer.parseInt(editTextAgeRegistration.getText().toString().trim()));
                viewModel.register(userDto);

                viewModel.getUser().observe(RegistrationActivity.this, new Observer<FirebaseUser>() {
                    @Override
                    public void onChanged(FirebaseUser user) {
                        Intent intent = UserActivity.newIntent(RegistrationActivity.this, user);
                        startActivity(intent);
                    }
                });

                viewModel.getErrorMessage().observe(RegistrationActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String message) {
                        Toast.makeText(
                                RegistrationActivity.this,
                                message,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            }
        });
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