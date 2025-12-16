package com.innowise.taxi.dao.impl;

import com.innowise.taxi.dao.DriverShiftDao;
import com.innowise.taxi.entity.DriverShift;
import com.innowise.taxi.entity.DriverShiftStatus;
import com.innowise.taxi.exception.DaoException;
import com.innowise.taxi.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class DriverShiftDaoImpl implements DriverShiftDao {
  private static final Logger logger = LogManager.getLogger();

  private static final String SELECT_ACTIVE_BY_DRIVER = """
        SELECT id, driver_id, car_id, start_time, end_time, status, current_lat, current_lon
        FROM driver_shifts
        WHERE driver_id = ? AND status = 'ACTIVE'
        LIMIT 1
    """;

  private static final String UPDATE_STATUS_TO_CLOSED = """
        UPDATE driver_shifts
        SET status = 'CLOSED', end_time = NOW()
        WHERE id = ?
    """;

  private static final String INSERT_SHIFT = """
        INSERT INTO driver_shifts(driver_id, car_id, start_time, status)
        VALUES (?, ?, NOW(), 'ACTIVE')
    """;

  @Override
  public Optional<DriverShift> findActiveByDriverId(int driverId) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement statement = connection.prepareStatement(SELECT_ACTIVE_BY_DRIVER)) {
        statement.setInt(1, driverId);
        try (ResultSet rs = statement.executeQuery()) {
          if (rs.next()) {
            DriverShift shift = mapRow(rs);
            return Optional.of(shift);
          }
        }
      }
    } catch (SQLException e) {
      logger.error("Error finding active shift for driver {}", driverId, e);
      throw new DaoException(e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return Optional.empty();
  }

  @Override
  public boolean updateStatusToClosed(int shiftId) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement statement = connection.prepareStatement(UPDATE_STATUS_TO_CLOSED)) {
        statement.setInt(1, shiftId);
        int rows = statement.executeUpdate();
        return rows > 0;
      }
    } catch (SQLException e) {
      logger.error("Error closing shift {}", shiftId, e);
      throw new DaoException(e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
  }


  @Override
  public DriverShift insertShift(int driverId, int carId) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement statement = connection.prepareStatement(INSERT_SHIFT, Statement.RETURN_GENERATED_KEYS)) {
        statement.setInt(1, driverId);
        statement.setInt(2, carId);
        statement.executeUpdate();

        try (ResultSet keys = statement.getGeneratedKeys()) {
          if (keys.next()) {
            int id = keys.getInt(1);
            return new DriverShift(id, driverId, carId,
                    LocalDateTime.now(), null, DriverShiftStatus.ACTIVE, 0, 0);
          }
        }
      }
    } catch (SQLException e) {
      logger.error("Error inserting new shift for driver {}", driverId, e);
      throw new DaoException(e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    throw new DaoException("Failed to insert shift");
  }

  private DriverShift mapRow(ResultSet rs) throws SQLException {
    return new DriverShift(
            rs.getInt(ID),
            rs.getInt(DRIVER_ID),
            rs.getInt(CAR_ID),
            rs.getTimestamp(START_TIME).toLocalDateTime(),
            rs.getTimestamp(END_TIME) != null
                    ? rs.getTimestamp(END_TIME).toLocalDateTime()
                    : null,
            DriverShiftStatus.valueOf(rs.getString(STATUS)),
            rs.getInt(CURRENT_LAT),
            rs.getInt(CURRENT_LON)
    );
  }
}
