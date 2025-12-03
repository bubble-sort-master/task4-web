package com.innowise.taxi.service.impl;

import com.innowise.taxi.dao.impl.UserDao;
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
  private final UserDao userDao = new UserDao();

  private UserServiceImpl() {}

  public static UserServiceImpl getInstance() {
    return instance;
  }

  @Override
  public boolean authentificate(String username, String password) throws ServiceException {
    try {
      Optional<User> userOpt = userDao.findByLogin(username);
      if (userOpt.isPresent()) {
        User user = userOpt.get();
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
}
