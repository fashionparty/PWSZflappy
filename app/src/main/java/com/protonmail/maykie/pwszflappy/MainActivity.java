package com.protonmail.maykie.pwszflappy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout bStart, bCharacter, bHighscore, bExit;
    private FirebaseAuth mAuth;
    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userList = new ArrayList<>();
        RealtimeDatabase database = RealtimeDatabase.getInstanceOf();
        mAuth = FirebaseAuth.getInstance();
        bStart = findViewById(R.id.mainactivity_start_button);
        bCharacter = findViewById(R.id.mainactivity_character_button);
        bHighscore = findViewById(R.id.mainactivity_highscore_button);
        bExit = findViewById(R.id.mainactivity_exit_button);

        bStart.setOnClickListener(MainActivity.this);
        bCharacter.setOnClickListener(MainActivity.this);
        bHighscore.setOnClickListener(MainActivity.this);
        bExit.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.mainactivity_start_button:
                GameView gameView = new GameView(this);
                setContentView(gameView);
                break;
            case R.id.mainactivity_character_button:
                startActivity(new Intent(this, CharacterActivity.class));
                break;
            case R.id.mainactivity_highscore_button:
                startActivity(new Intent(this, HighscoresActivity.class));
                break;
            case R.id.mainactivity_exit_button:
                this.finishAffinity();
                break;
        }
    }
}