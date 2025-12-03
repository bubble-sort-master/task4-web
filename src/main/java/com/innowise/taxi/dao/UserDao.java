package com.innowise.taxi.dao.impl;

import com.innowise.taxi.dao.AbstractDao;
import com.innowise.taxi.entity.User;
import com.innowise.taxi.exception.DaoException;
import com.innowise.taxi.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class UserDao extends AbstractDao<User> {
  private static final Logger logger = LogManager.getLogger(UserDao.class);
  private static final String SELECT_BY_LOGIN = "SELECT id, username, password FROM users WHERE username = ?";

  @Override
  public boolean insert(User entity) throws DaoException {
    throw new UnsupportedOperationException("Insert not implemented");
  }

  @Override
  public boolean delete(User entity) throws DaoException {
    throw new UnsupportedOperationException("Delete not implemented");
  }

  @Override
  public List<User> findAll() throws DaoException {
    throw new UnsupportedOperationException("FindAll not implemented");
  }

  @Override
  public Optional<User> findById(Long id) throws DaoException {
    throw new UnsupportedOperationException("FindById not implemented");
  }

  @Override
  public User update(User entity) throws DaoException {
    throw new UnsupportedOperationException("Update not implemented");
  }

  public Optional<User> findByLogin(String login) throws DaoException {
    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(SELECT_BY_LOGIN)) {
      statement.setString(1, login);
      try (ResultSet result = statement.executeQuery()) {
        if (result.next()) {
          User user = new User(
                  result.getLong("id"),
                  result.getString("username"),
                  result.getString("password")
          );
          logger.info("User {} found in database", login);
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      logger.error("Error while finding user {}", login, e);
      throw new DaoException(e);
    }
    logger.warn("User {} not found", login);
    return Optional.empty();
  }
}
