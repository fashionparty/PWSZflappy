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
import java.util.concurrent.CountDownLatch;

public class RealtimeDatabase {

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private static ArrayList<User> userList;
    private static RealtimeDatabase singleton;
    private static boolean empty = true;

    private int flag = 1;
    private long userCount = 0;

    public ArrayList<User> getUserList() {
        return userList;
    }

    public boolean isEmpty() {
        return empty;
    }

    public static RealtimeDatabase getInstanceOf() {
        if(singleton==null) {
            singleton = new RealtimeDatabase();
        }
        return singleton;
    }

    private RealtimeDatabase() {
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("users");
        userList = new ArrayList<>();
    }

    public void readData(final OnGetDataListener listener) {
        listener.onStart();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userCount = snapshot.getChildrenCount();
                dbRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        userList.add(snapshot.getValue(User.class));
                        if(flag == userCount) {
                            empty =false;
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
    /*
    private void readUsersData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userCount = snapshot.getChildrenCount();
                dbRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        userList.add(snapshot.getValue(User.class));
                        if(flag == userCount) {
                            empty =false;
                            System.out.println("tibia");
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
    } */
}
