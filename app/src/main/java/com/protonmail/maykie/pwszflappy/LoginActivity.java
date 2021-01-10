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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private RelativeLayout buttonLoggin;
    private EditText etEmail, etPassword;
    private TextView tvReset, tvNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        buttonLoggin = findViewById(R.id.login_login_b);
        etEmail = findViewById(R.id.login_email_ev);
        etPassword = findViewById(R.id.login_password_ev);
        tvReset = findViewById(R.id.login_resetpassword_tv);
        tvNewAccount = findViewById(R.id.login_register_tv);

        buttonLoggin.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        tvNewAccount.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.login_register_tv:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.login_resetpassword_tv:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
            case R.id.login_login_b:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("To pole nie może być puste");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Nieprawidłowy adres email");
            etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            etPassword.setError("To pole nie może być puste");
            etPassword.requestFocus();
            return;
        }

        if(password.length()<6) {
            etPassword.setError("Hasło musi składać się z co najmniej 6 znaków");
            etPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, "Nie udało się zalogować", Toast.LENGTH_SHORT).show();
            }
        });
    }
}