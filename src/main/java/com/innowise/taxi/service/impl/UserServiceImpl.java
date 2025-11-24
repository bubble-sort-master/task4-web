package com.innowise.taxi.service.impl;

import com.innowise.taxi.service.UserService;

public class UserServiceImpl implements UserService {
    private static UserServiceImpl instance = new UserServiceImpl();

    private UserServiceImpl() {}

    public static UserServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean authentificate(String login, String password) {
        return login.equals(CLIENT_LOGIN);
    }
}
