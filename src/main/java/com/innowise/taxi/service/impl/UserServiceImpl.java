package com.innowise.taxi.service.impl;

import com.innowise.taxi.dao.impl.UserDaoImpl;
import com.innowise.taxi.entity.User;
import com.innowise.taxi.exception.DaoException;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserServiceImpl implements UserService {
  private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
  private static final UserServiceImpl instance = new UserServiceImpl();
  private final UserDaoImpl userDaoImpl = new UserDaoImpl();

  private UserServiceImpl() {}

  public static UserServiceImpl getInstance() {
    return instance;
  }

  @Override
  public boolean authenticate(String username, String password) throws ServiceException {
    try {
      Optional<User> userOptional = userDaoImpl.findByUsername(username);
      if (userOptional.isPresent()) {
        User user = userOptional.get();
        if (user.getPassword().equals(password)) {
          logger.info("Authentication successful for user {}", username);
          return true;
        } else {
          logger.warn("Authentication failed for user {}: wrong password", username);
          return false;
        }
      } else {
        logger.warn("Authentication failed: user {} not found", username);
        return false;
      }
    } catch (DaoException e) {
      logger.error("Service error during authentication for user {}", username, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean register(User user) throws ServiceException {
    try {
      boolean saved = userDaoImpl.save(user);
      if (saved) {
        logger.info("Registration successful for user {}", user.getUsername());
      } else {
        logger.warn("Registration failed for user {}", user.getUsername());
      }
      return saved;
    } catch (DaoException e) {
      logger.error("Service error during registration for user {}", user.getUsername(), e);
      return switch (e.getErrorCode()) {
        case DUPLICATE_KEY, NULL_VALUE -> {
          logger.warn("Registration failed for user {} due to {}", user.getUsername(), e.getErrorCode());
          yield false;
        }
        default -> throw new ServiceException("Registration failed", e);
      };
    }
  }

}
