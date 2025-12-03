package com.innowise.taxi.service;

import com.innowise.taxi.entity.User;
import com.innowise.taxi.exception.ServiceException;

public interface UserService {
  boolean authenticate(String login, String password) throws ServiceException;

  boolean register(User user) throws ServiceException;
}
