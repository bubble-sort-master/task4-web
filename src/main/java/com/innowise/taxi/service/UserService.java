package com.innowise.taxi.service;

import com.innowise.taxi.entity.User;
import com.innowise.taxi.exception.ServiceException;
import java.util.List;
import java.util.Optional;

public interface UserService {
  Optional<User> authenticate(String login, String password) throws ServiceException;

  boolean register(User user) throws ServiceException;

  List<User> findAllUsers() throws ServiceException;
}
