package com.innowise.taxi.dao.impl;

import com.innowise.taxi.dao.OrderDao;
import com.innowise.taxi.entity.Order;
import com.innowise.taxi.entity.OrderStatus;
import com.innowise.taxi.exception.DaoException;
import com.innowise.taxi.exception.DaoErrorCode;
import com.innowise.taxi.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {
  private static final Logger logger = LogManager.getLogger(OrderDaoImpl.class);
  private static final String INSERT_SQL = """
    INSERT INTO orders (client_id, driver_shift_id, pickup_lat, pickup_lon, dropoff_lat, dropoff_lon, price, status, is_paid)
    VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)
    """;
  private static final String FIND_BY_ID_SQL =
    "SELECT * FROM orders WHERE id=?";
  private static final String UPDATE_STATUS_SQL =
    "UPDATE orders SET status=? WHERE id=?";
  private static final String SET_DRIVER_SQL =
    "UPDATE orders SET driver_shift_id=? WHERE id=?";

  @Override
  public Order insert(Order order) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
        ps.setInt(1, order.getClientId());
        ps.setInt(2,order.getDriverShiftId());
        ps.setInt(3, order.getPickupLat());
        ps.setInt(4, order.getPickupLon());
        ps.setInt(5, order.getDropOffLat());
        ps.setInt(6, order.getDropOffLon());
        ps.setDouble(7, order.getPrice());
        ps.setString(8, order.getStatus().name());
        ps.setBoolean(9, order.isPaid());

        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            order.setId(rs.getInt(1));
          }
        }
        return order;
      }
    } catch (SQLException e) {
      logger.error("Error while inserting order", e);
      throw new DaoException("SQL error while inserting order", e, DaoErrorCode.SQL_ERROR);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
  }

  @Override
  public Optional<Order> findById(int id) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement ps = connection.prepareStatement(FIND_BY_ID_SQL)) {
        ps.setInt(1, id);
        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            return Optional.of(mapRow(rs));
          }
        }
      }
    } catch (SQLException e) {
      logger.error("Error while finding order by id", e);
      throw new DaoException("SQL error", e, DaoErrorCode.SQL_ERROR);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return Optional.empty();
  }

  @Override
  public boolean updateStatus(int id, OrderStatus status) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement ps = connection.prepareStatement(UPDATE_STATUS_SQL)) {
        ps.setString(1, status.name());
        ps.setInt(2, id);
        return ps.executeUpdate() > 0;
      }
    } catch (SQLException e) {
      logger.error("Error while updating order status", e);
      throw new DaoException("SQL error", e, DaoErrorCode.SQL_ERROR);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
  }

  @Override
  public boolean setDriverShift(int id, int driverShiftId) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement ps = connection.prepareStatement(SET_DRIVER_SQL)) {
        ps.setInt(1, driverShiftId);
        ps.setInt(2, id);
        return ps.executeUpdate() > 0;
      }
    } catch (SQLException e) {
      logger.error("Error while setting driver shift for order", e);
      throw new DaoException("SQL error", e, DaoErrorCode.SQL_ERROR);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
  }

  private Order mapRow(ResultSet rs) throws SQLException {
    Order order = new Order();
    order.setId(rs.getInt(ID));
    order.setClientId(rs.getInt(CLIENT_ID));
    order.setDriverShiftId(rs.getInt(DRIVER_SHIFT_ID));
    order.setStatus(OrderStatus.valueOf(rs.getString(STATUS)));
    order.setPickupLat(rs.getInt(PICKUP_LAT));
    order.setPickupLon(rs.getInt(PICKUP_LON));
    order.setDropOffLat(rs.getInt(DROPOFF_LAT));
    order.setDropOffLon(rs.getInt(DROPOFF_LON));
    order.setPrice(rs.getDouble(PRICE));
    order.setPaid(rs.getBoolean(IS_PAID));
    order.setCreatedAt(rs.getTimestamp(CREATED_AT).toLocalDateTime());
    return order;
  }
}
