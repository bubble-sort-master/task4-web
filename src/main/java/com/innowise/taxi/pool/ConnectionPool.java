package com.innowise.taxi.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
  private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
  private static final int POOL_SIZE = 10;
  private static final ConnectionPool instance = new ConnectionPool();
  private final Deque<Connection> availableConnections = new ArrayDeque<>();
  private final ReentrantLock lock = new ReentrantLock();
  private final Condition connectionAvailable = lock.newCondition();

  private ConnectionPool() {
    try {
      Properties props = loadProperties();
      String driver = props.getProperty("db.driver");
      Class.forName(driver);
      String url = props.getProperty("db.url");
      String user = props.getProperty("db.user");
      String password = props.getProperty("db.password");

      for (int i = 0; i < POOL_SIZE; i++) {
        Connection connection = DriverManager.getConnection(url, user, password);
        availableConnections.add(connection);
      }
      logger.info("Connection pool initialized with size {}", POOL_SIZE);
    } catch (IOException | ClassNotFoundException | SQLException e) {
      throw new ExceptionInInitializerError("Failed to initialize connection pool: " + e.getMessage());
    }
  }

  public static ConnectionPool getInstance() {
    return instance;
  }

  private Properties loadProperties() throws IOException {
    Properties props = new Properties();
    try (InputStream in = getClass().getClassLoader().getResourceAsStream("db.properties")) {
      if (in == null) throw new IOException("db.properties not found");
      props.load(in);
    }
    return props;
  }

  public Connection getConnection() {
    lock.lock();
    try {
      while (availableConnections.isEmpty()) {
        connectionAvailable.await();
      }
      return availableConnections.poll();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return null;
    } finally {
      lock.unlock();
    }
  }

  public void releaseConnection(Connection connection) {
    lock.lock();
    try {
      availableConnections.offer(connection);
      connectionAvailable.signal();
    } finally {
      lock.unlock();
    }
  }

  public void shutdown() {
    lock.lock();
    try {
      for (Connection connection : availableConnections) {
        try {
          connection.close();
        } catch (SQLException e) {
          logger.error("Error closing connection", e);
        }
      }
      availableConnections.clear();
      logger.info("Connection pool shut down");

      try {
        var drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
          var driver = drivers.nextElement();
          try {
            DriverManager.deregisterDriver(driver);
            logger.info("Deregistered JDBC driver {}", driver);
          } catch (SQLException e) {
            logger.error("Error deregistering driver {}", driver, e);
          }
        }
      } catch (Exception e) {
        logger.error("Unexpected error during driver deregistration", e);
      }
    } finally {
      lock.unlock();
    }
  }

}
