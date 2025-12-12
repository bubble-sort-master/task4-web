package com.innowise.taxi.dao;

import com.innowise.taxi.entity.User;
import com.innowise.taxi.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface UserDao {
  String ID = "id";
  String USERNAME = "username";
  String PASSWORD = "password";
  String ROLE = "role";
  String FIRST_NAME = "first_name";
  String LAST_NAME = "last_name";
  String BONUS_POINTS = "bonus_points";
  String BANNED = "is_banned";

  Optional<User> findByUsername(String username) throws DaoException;
  boolean save(User user) throws DaoException;
  List<User> findAll() throws DaoException;
}
