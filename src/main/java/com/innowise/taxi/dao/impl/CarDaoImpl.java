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

public class CarDaoImpl implements CarDao {
  private static final Logger logger = LogManager.getLogger();
  private static final String SELECT_ALL_CARS = """
    SELECT id, model, plate_number, year
    FROM cars
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
          Car car = new Car(
                  result.getLong(ID),
                  result.getString(MODEL),
                  result.getString(PLATE_NUMBER),
                  result.getInt(YEAR)
          );
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

}
