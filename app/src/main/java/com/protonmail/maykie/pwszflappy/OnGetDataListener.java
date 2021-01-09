package com.protonmail.maykie.pwszflappy;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public interface OnGetDataListener {

    void onSuccess(ArrayList<User> userList);
    void onStart();
    void onFailure();
}
