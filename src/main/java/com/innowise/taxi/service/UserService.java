package com.innowise.taxi.service;

public interface UserService {

    //TODO: take data from database
    String ADMIN_LOGIN = "admin";
    String DRIVER_LOGIN = "driver";
    String CLIENT_LOGIN = "client";

    String ADMIN_PASSWORD = "123";
    String DRIVER_PASSWORD = "123";
    String CLIENT_PASSWORD = "123";

    boolean authentificate(String login, String password);
}
