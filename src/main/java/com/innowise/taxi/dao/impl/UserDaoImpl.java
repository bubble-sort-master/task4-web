package com.innowise.taxi.dao.impl;
import com.innowise.taxi.dao.UserDao;
import com.innowise.taxi.dao.constants.UserColumn;
import com.innowise.taxi.entity.User;
import com.innowise.taxi.exception.DaoErrorCode;
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
    INSERT INTO users (username, password, first_name, last_name)
    VALUES (?, ?, ?, ?)
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
                    result.getLong(UserColumn.ID),
                    result.getString(UserColumn.USERNAME),
                    result.getString(UserColumn.PASSWORD)
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
        return rows > 0;
      }
    } catch (SQLIntegrityConstraintViolationException e) {
      logger.warn("Constraint violation while saving user {}: {}", user.getUsername(), e.getMessage());
      if (e.getMessage().contains("NULL")) {
        throw new DaoException("Null value in required field", e, DaoErrorCode.NULL_VALUE);
      } else {
        throw new DaoException("Duplicate key", e, DaoErrorCode.DUPLICATE_KEY);
      }
    } catch (SQLException e) {
      logger.error("SQL error while saving user {}", user.getUsername(), e);
      throw new DaoException("SQL error while saving user", e, DaoErrorCode.SQL_ERROR);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
  }
}
