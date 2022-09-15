package com.example.homework49.service;

import java.util.Map;

public class UserModel {
    private final Map<String, User> users;

    public UserModel() {
        users = FileService.readUsers();
    }

    public Map<String, User> getUsers() {
        return users;
    }
}
