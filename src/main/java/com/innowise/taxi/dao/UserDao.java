package com.innowise.taxi.dao;

import com.innowise.taxi.entity.User;
import com.innowise.taxi.exception.DaoException;

import java.util.Optional;

public interface UserDao {
  Optional<User> findByUsername(String username) throws DaoException;
  boolean save(User user) throws DaoException;
}
