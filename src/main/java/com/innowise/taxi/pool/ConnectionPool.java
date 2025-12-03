package com.innowise.taxi.pool;

import com.innowise.taxi.exception.PoolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
  private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
  private static final int POOL_SIZE = 10;
  private static final ConnectionPool instance = new ConnectionPool();
  private final BlockingQueue<Connection> connections;

  private ConnectionPool() {
    try {
      Properties props = loadProperties();
      String driver = props.getProperty("db.driver");
      Class.forName(driver);
      String url = props.getProperty("db.url");
      String user = props.getProperty("db.user");
      String password = props.getProperty("db.password");

      connections = new ArrayBlockingQueue<>(POOL_SIZE);

      for (int i = 0; i < POOL_SIZE; i++) {
        Connection connection = DriverManager.getConnection(url, user, password);
        connections.add(connection);
      }
      logger.info("Connection pool initialized with size {}", POOL_SIZE);
    } catch (PoolException | ClassNotFoundException | SQLException e) {
      throw new ExceptionInInitializerError("Failed to initialize connection pool: " + e.getMessage());
    }
  }

  public static ConnectionPool getInstance() {
    return instance;
  }

  private Properties loadProperties() throws PoolException {
    Properties props = new Properties();
    try (InputStream in = getClass().getClassLoader().getResourceAsStream("db.properties")) {
      if (in == null) {
        throw new PoolException("db.properties not found");
      }
      props.load(in);
    } catch (IOException e) {
      throw new PoolException("Error loading db.properties");
    }
    return props;
  }

  public Connection getConnection() {
    try {
      return connections.take();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return null;
    }
  }

  public void releaseConnection(Connection connection) {
    if (connection != null) {
      connections.offer(connection);
    }
  }

  public void shutdown() {
    for (Connection connection : connections) {
      try {
        connection.close();
      } catch (SQLException e) {
        logger.error("Error closing connection", e);
      }
    }
    connections.clear();
    logger.info("Connection pool shut down");

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
  }
}
