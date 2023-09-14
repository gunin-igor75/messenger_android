package com.github.gunin_igor75.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.gunin_igor75.messenger.view_model.ChangePasswordViewModel;

public class ChangePasswordActivity extends AppCompatActivity {

    public static final String EMAIL = "EMAIL";
    private EditText editTextEmailChange;
    private Button buttonResetPassword;

    private ChangePasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        init();
        observeViewModel();
        String email = getIntent().getStringExtra(EMAIL);
        editTextEmailChange.setText(email);
        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmailChange.getText().toString().trim();
                viewModel.changePassword(email);
            }
        });
    }

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        intent.putExtra(EMAIL, email);
        return intent;
    }

    private void init() {
        editTextEmailChange = findViewById(R.id.editTextEmailChange);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
        viewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);
    }

    private void observeViewModel() {
        viewModel.getIsSendResetPassword().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSendResetPassword) {
                if (isSendResetPassword) {
                    Toast.makeText(
                            ChangePasswordActivity.this,
                            R.string.reset_password_link,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
        viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(
                        ChangePasswordActivity.this,
                        message,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}