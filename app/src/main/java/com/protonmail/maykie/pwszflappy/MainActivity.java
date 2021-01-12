package com.protonmail.maykie.pwszflappy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout bStart, bCharacter, bHighscore, bExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    @SuppressLint("NonConstantResourceId")
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