package com.example.seabattle.Models;

public class User {
    private String imagePath;
    private String nickname;

    public User() {}

    public User(String imagePath, String nickname) {
        if (nickname.trim().equals("")) {
            nickname = "NoName";
        }
        this.imagePath = imagePath;
        this.nickname = nickname;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
