package com.github.gunin_igor75.messenger;

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

import com.github.gunin_igor75.messenger.dto.User;
import com.github.gunin_igor75.messenger.view_model.MainViewModel;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private TextView textViewForgot;

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userLogin = new User();
                userLogin.setEmail(editTextEmail.getText().toString().trim());
                userLogin.setPassword(editTextPassword.getText().toString().trim());
                viewModel.login(userLogin);
                viewModel.getUser().observe(MainActivity.this, new Observer<FirebaseUser>() {
                    @Override
                    public void onChanged(FirebaseUser user) {
                        Intent intent = UserActivity.newIntent(MainActivity.this, user);
                        startActivity(intent);
                    }
                });
                viewModel.getErrorMessage().observe(MainActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String message) {
                        Toast.makeText(
                                MainActivity.this,
                                message,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RegistrationActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChangePasswordActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }


    private void init() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);
        textViewForgot = findViewById(R.id.textViewForgot);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }
}