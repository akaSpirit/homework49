package com.example.homework49.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String username;
    private String password;
    private boolean isVoted;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
