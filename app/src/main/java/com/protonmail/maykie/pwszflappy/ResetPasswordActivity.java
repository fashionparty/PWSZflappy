package com.protonmail.maykie.pwszflappy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView tvBack;
    private EditText etEmail;
    private RelativeLayout bReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        firebaseAuth = FirebaseAuth.getInstance();
        tvBack = findViewById(R.id.reset_back_tv);
        etEmail = findViewById(R.id.reset_email_et);
        bReset = findViewById(R.id.reset_reset_b);

        bReset.setOnClickListener(this);
        tvBack.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.reset_reset_b:
                resetPassword();
                break;
            case R.id.reset_back_tv:
                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                break;
        }
    }

    public void resetPassword() {
        String email = etEmail.getText().toString().trim();
        if(email.isEmpty()) {
            etEmail.setError("To pole nie może być puste");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Wprowadzono nieprawidłowy email");
            etEmail.requestFocus();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(ResetPasswordActivity.this, "Sprawdź swój email", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ResetPasswordActivity.this, "Błąd serwera!", Toast.LENGTH_LONG).show();
            }
        });
    }
}