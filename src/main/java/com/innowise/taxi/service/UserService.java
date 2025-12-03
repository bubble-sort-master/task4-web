package com.innowise.taxi.service;

import com.innowise.taxi.exception.ServiceException;

public interface UserService {
  boolean authentificate(String login, String password) throws ServiceException;
}
