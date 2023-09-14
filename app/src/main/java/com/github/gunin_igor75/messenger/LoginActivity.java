package com.github.gunin_igor75.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.gunin_igor75.messenger.dto.UserDto;
import com.github.gunin_igor75.messenger.view_model.LoginViewModel;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private TextView textViewForgot;

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        observeLoginUserPositive();
        observeErrorMessage();
        String email = editTextEmail.getText().toString().trim();
        setupClickListeners(email);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    private void setupClickListeners(String email) {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDto userDtoLogin = new UserDto();
                userDtoLogin.setEmail(email);
                userDtoLogin.setPassword(editTextPassword.getText().toString().trim());
                viewModel.login(userDtoLogin);

            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RegistrationActivity.newIntent(LoginActivity.this);
                startActivity(intent);
            }
        });

        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChangePasswordActivity.newIntent(LoginActivity.this, email);
                startActivity(intent);
            }
        });
    }

    private void observeErrorMessage() {
        viewModel.getErrorMessage().observe(LoginActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null) {
                    Toast.makeText(
                            LoginActivity.this,
                            message,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    private void observeLoginUserPositive() {
        viewModel.getUser().observe(LoginActivity.this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                if (user != null) {
                    Intent intent = UserActivity.newIntent(LoginActivity.this);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    private void init() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);
        textViewForgot = findViewById(R.id.textViewForgot);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }
}