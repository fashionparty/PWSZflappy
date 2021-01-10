package com.protonmail.maykie.pwszflappy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CharacterActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout maykieCard, krystianCard, venntrisCard;
    GameConfig gameConfig = GameConfig.getInstanceOf();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        maykieCard = findViewById(R.id.maykie_card);
        krystianCard = findViewById(R.id.krystian_card);
        venntrisCard = findViewById(R.id.venntris_card);

        maykieCard.setOnClickListener(this);
        krystianCard.setOnClickListener(this);
        venntrisCard.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.maykie_card:
                gameConfig.setPostac("maykie");
                break;
            case R.id.krystian_card:
                gameConfig.setPostac("krystian");
                break;
            case R.id.venntris_card:
                gameConfig.setPostac("venntris");
                break;
        }
        startActivity(new Intent(this,MainActivity.class));
    }
}