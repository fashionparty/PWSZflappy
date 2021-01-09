package com.protonmail.maykie.pwszflappy;

public class GameConfig {

    private static GameConfig gameConfig;

    private String postac = "maykie";

    public GameConfig() {

    }

    public static GameConfig getInstanceOf() {

        if(gameConfig==null) gameConfig = new GameConfig();
        return gameConfig;
    }

    public String getPostac() {
        return postac;
    }

    public void setPostac(String postac) {
        this.postac = postac;
    }
}
