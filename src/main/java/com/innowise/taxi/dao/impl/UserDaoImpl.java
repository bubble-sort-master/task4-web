package com.innowise.taxi.dao.impl;

import com.innowise.taxi.dao.UserDao;
import com.innowise.taxi.entity.User;
import com.innowise.taxi.exception.DaoException;
import com.innowise.taxi.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
  private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);
  private static final String SELECT_BY_USERNAME = "SELECT id, username, password FROM users WHERE username = ?";
  private static final String INSERT_USER = """
    INSERT INTO users (username, password, first_name, last_name, role, is_banned)
    VALUES (?, ?, ?, ?, 'CLIENT', 0)
    """;

  @Override
  public Optional<User> findByUsername(String username) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_USERNAME)) {
        statement.setString(1, username);
        try (ResultSet result = statement.executeQuery()) {
          if (result.next()) {
            User user = new User(
                    result.getLong("id"),
                    result.getString("username"),
                    result.getString("password")
            );
            logger.info("User {} found in database", username);
            return Optional.of(user);
          }
        }
      }
    } catch (SQLException e) {
      logger.error("Error while finding user {}", username, e);
      throw new DaoException(e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    logger.warn("User {} not found", username);
    return Optional.empty();
  }

  @Override
  public boolean save(User user) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement stmt = connection.prepareStatement(INSERT_USER)) {
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getFirstName());
        stmt.setString(4, user.getLastName());

        int rows = stmt.executeUpdate();
        if (rows > 0) {
          logger.info("User {} successfully saved to database", user.getUsername());
          return true;
        } else {
          logger.warn("User {} was not saved to database", user.getUsername());
          return false;
        }
      }
    } catch (SQLException e) {
      logger.error("Error saving user {}", user.getUsername(), e);
      throw new DaoException("Error saving user", e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
  }
}
