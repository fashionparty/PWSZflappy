package com.protonmail.maykie.pwszflappy;

public class User implements Comparable<User> {

    public String name, email, password;
    public int highscore;

    public User() {}

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.highscore = 0;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getHighscore() {
        return highscore;
    }

    @Override
    public int compareTo(User user) {
        if(highscore>user.getHighscore()) return -1;
        else return 1;
    }
}
