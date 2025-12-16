package com.innowise.taxi.dao.impl;

import com.innowise.taxi.dao.CarDao;
import com.innowise.taxi.entity.Car;
import com.innowise.taxi.exception.DaoException;
import com.innowise.taxi.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Optional;

public class CarDaoImpl implements CarDao {
  private static final Logger logger = LogManager.getLogger();
  private static final String SELECT_ALL_CARS = """
    SELECT id, model, plate_number, year
    FROM cars
    """;

  private static final String SELECT_FREE_CAR = """
    SELECT id, model, plate_number, year
    FROM cars
    WHERE id NOT IN (SELECT car_id FROM driver_shifts WHERE status='ACTIVE')
    LIMIT 1
""";

  private static final String SELECT_BY_ID = """
    SELECT id, model, plate_number, year
    FROM cars
    WHERE id = ?
""";

  @Override
  public List<Car> findAll() throws DaoException {

    List<Car> cars = new ArrayList<>();
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_CARS);
           ResultSet result = statement.executeQuery()) {
        while (result.next()) {
          Car car = mapRowToCar(result);
          cars.add(car);
        }
      }
    } catch (SQLException e) {
      logger.error("Error while finding all cars", e);
      throw new DaoException(e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return cars;
  }

  @Override
  public Car findById(int id) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
        statement.setInt(1, id);
        try (ResultSet result = statement.executeQuery()) {
          if (result.next()) {
            return mapRowToCar(result);
          }
        }
      }
    } catch (SQLException e) {
      logger.error("Error while finding car by id {}", id, e);
      throw new DaoException(e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    throw new DaoException("Car not found with id=" + id);
  }

  @Override
  public Optional<Car> findFreeCar() throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement statement = connection.prepareStatement(SELECT_FREE_CAR);
           ResultSet result = statement.executeQuery()) {
        if (result.next()) {
          Car car = mapRowToCar(result);
          return Optional.of(car);
        }
      }
    } catch (SQLException e) {
      logger.error("Error while finding free car", e);
      throw new DaoException(e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return Optional.empty();
  }

  private Car mapRowToCar(ResultSet rs) throws SQLException {
    return new Car(
            rs.getInt(ID),
            rs.getString(MODEL),
            rs.getString(PLATE_NUMBER),
            rs.getInt(YEAR)
    );
  }
}
