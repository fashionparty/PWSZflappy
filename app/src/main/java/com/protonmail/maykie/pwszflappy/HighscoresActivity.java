package com.protonmail.maykie.pwszflappy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HighscoresActivity extends AppCompatActivity {

    private Button[] buttons;
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private ArrayList<User> lista;
    private int count = 1;
    private int numberOfUsers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference("users");

        lista = new ArrayList<>();

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("number of users: " + snapshot.getChildrenCount());
                numberOfUsers = (int) snapshot.getChildrenCount();

                dbReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        lista.add(snapshot.getValue(User.class));
                        if(count == numberOfUsers) readData(lista);
                        count++;
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        buttons = new Button[5];

        buttons[0] = findViewById(R.id.highscoreactivity_1_b);
        buttons[1] = findViewById(R.id.highscoreactivity_2_b);
        buttons[2] = findViewById(R.id.highscoreactivity_3_b);
        buttons[3] = findViewById(R.id.highscoreactivity_4_b);
        buttons[4] = findViewById(R.id.highscoreactivity_5_b);
    }

    private void readData(ArrayList<User> userList) {

        Collections.sort(userList);

        for(int i = 0; i<5; i++) {
            buttons[i].setText(i+1 + ". " + userList.get(i).getName() + " - " + userList.get(i).getHighscore() + " pkt");
        }
    }
}