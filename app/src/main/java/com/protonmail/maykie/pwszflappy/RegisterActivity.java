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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private TextView tvBack;
    private EditText etUsername, etEmail, etPassword, etPasswordRepeat;
    private RelativeLayout buttonRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference("users");

        etUsername = findViewById(R.id.register_username_et);
        etEmail = findViewById(R.id.register_email_et);
        etPassword = findViewById(R.id.register_password_et);
        etPasswordRepeat = findViewById(R.id.register_passwordrepeat_et);
        buttonRegister = findViewById(R.id.register_create_b);
        tvBack = findViewById(R.id.register_back_tv);

        buttonRegister.setOnClickListener(this);
        tvBack.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.register_create_b:
                registerUser();
                break;
            case R.id.register_back_tv:
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
    }

    public void registerUser() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordRepeat = etPasswordRepeat.getText().toString().trim();

        if(username.isEmpty()) {
            etUsername.setError("To pole jest wymagane");
            etUsername.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            etEmail.setError("To pole jest wymagane");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Nieprawidłowy format adresu email");
            etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            etPassword.setError("To pole jest wymagane");
            etPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            etPassword.setError("Hasło musi się składać z co najmniej 6 znaków");
            etPassword.requestFocus();
            return;
        }

        if(passwordRepeat.isEmpty()) {
            etPasswordRepeat.setError("To pole jest wymagane");
            etPasswordRepeat.requestFocus();
            return;
        }

        if(!password.equals(passwordRepeat)) {
            etPasswordRepeat.setError("Hasła nie są identyczne");
            etPasswordRepeat.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                User user = new User(username, email, password);
                dbReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(user);
                Toast.makeText(RegisterActivity.this, "Zostało utworzone nowe konto", Toast.LENGTH_LONG).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            } else {
                Toast.makeText(RegisterActivity.this, "Nie udało się utworzyć konta", Toast.LENGTH_LONG).show();
            }
        });
    }
}