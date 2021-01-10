package com.protonmail.maykie.pwszflappy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RealtimeDatabase {

    private final DatabaseReference dbRef;
    private static ArrayList<User> userList;
    private static RealtimeDatabase singleton;

    private int flag = 1;
    private long userCount = 0;

    public static RealtimeDatabase getInstanceOf() {
        if(singleton==null) {
            singleton = new RealtimeDatabase();
        }
        return singleton;
    }

    private RealtimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("users");
        userList = new ArrayList<>();
    }

    public void readData(final OnGetDataListener listener) {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userCount = snapshot.getChildrenCount();
                dbRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        userList.add(snapshot.getValue(User.class));
                        if(flag == userCount) {
                            listener.onSuccess(userList);
                        }
                        flag++;
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
                        System.out.println(error.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
