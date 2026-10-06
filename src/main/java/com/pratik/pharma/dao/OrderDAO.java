package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Order;
import com.pratik.pharma.util.DBConnection;

public class OrderDAO {

	// =========================================================
	// GET ALL ORDERS
	// =========================================================

	public List<Order> getAllOrders() {

		List<Order> orders = new ArrayList<>();

		String sql = """
				SELECT
				    o.order_id,
				    o.client_id,
				    o.order_date,
				    o.order_status,
				    o.subtotal,
				    o.discount_amount,
				    o.tax_amount,
				    o.total_amount,
				    o.credit_status,
				    o.approved_by,
				    o.created_by,
				    o.created_at,

				    c.client_name AS client_name,
				    u.username AS created_by_name,

				    COUNT(oi.order_item_id) AS item_count

				FROM orders o

				JOIN clients c
				    ON o.client_id = c.client_id

				JOIN users u
				    ON o.created_by = u.user_id

				LEFT JOIN order_items oi
				    ON o.order_id = oi.order_id

				GROUP BY
				    o.order_id,
				    o.client_id,
				    o.order_date,
				    o.order_status,
				    o.subtotal,
				    o.discount_amount,
				    o.tax_amount,
				    o.total_amount,
				    o.credit_status,
				    o.approved_by,
				    o.created_by,
				    o.created_at,
				    c.client_name,
				    u.username

				ORDER BY o.order_id DESC
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {

				orders.add(mapOrder(resultSet));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return orders;
	}

	// =========================================================
	// GET ORDER BY ID
	// =========================================================

	public Order getOrderById(int orderId) {

		String sql = """
				SELECT
				    o.order_id,
				    o.client_id,
				    o.order_date,
				    o.order_status,
				    o.subtotal,
				    o.discount_amount,
				    o.tax_amount,
				    o.total_amount,
				    o.credit_status,
				    o.approved_by,
				    o.created_by,
				    o.created_at,

				    c.client_name AS client_name,
				    u.username AS created_by_name,

				    COUNT(oi.order_item_id) AS item_count

				FROM orders o

				JOIN clients c
				    ON o.client_id = c.client_id

				JOIN users u
				    ON o.created_by = u.user_id

				LEFT JOIN order_items oi
				    ON o.order_id = oi.order_id

				WHERE o.order_id = ?

				GROUP BY
				    o.order_id,
				    o.client_id,
				    o.order_date,
				    o.order_status,
				    o.subtotal,
				    o.discount_amount,
				    o.tax_amount,
				    o.total_amount,
				    o.credit_status,
				    o.approved_by,
				    o.created_by,
				    o.created_at,
				    c.client_name,
				    u.username
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setInt(1, orderId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				if (resultSet.next()) {

					return mapOrder(resultSet);
				}
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	// =========================================================
	// ADD ORDER
	// =========================================================

	public int addOrder(Order order) {

		String sql = """
				INSERT INTO orders
				(
				    client_id,
				    order_status,
				    subtotal,
				    discount_amount,
				    tax_amount,
				    total_amount,
				    credit_status,
				    approved_by,
				    created_by
				)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql,
						java.sql.Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setInt(1, order.getClientId());

			preparedStatement.setString(2, order.getOrderStatus());

			preparedStatement.setBigDecimal(3, order.getSubtotal());

			preparedStatement.setBigDecimal(4, order.getDiscountAmount());

			preparedStatement.setBigDecimal(5, order.getTaxAmount());

			preparedStatement.setBigDecimal(6, order.getTotalAmount());

			preparedStatement.setString(7, order.getCreditStatus());

			if (order.getApprovedBy() == null) {

				preparedStatement.setNull(8, java.sql.Types.INTEGER);

			} else {

				preparedStatement.setInt(8, order.getApprovedBy());
			}

			preparedStatement.setInt(9, order.getCreatedBy());

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {

				return 0;
			}

			try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {

				if (resultSet.next()) {

					return resultSet.getInt(1);
				}
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return 0;
	}

	// =========================================================
	// ADD ORDER - EXISTING CONNECTION / TRANSACTION
	// =========================================================

	public int addOrder(Connection connection, Order order) {

		String sql = """
				INSERT INTO orders
				(
				    client_id,
				    order_status,
				    subtotal,
				    discount_amount,
				    tax_amount,
				    total_amount,
				    credit_status,
				    approved_by,
				    created_by
				)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql,
				java.sql.Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setInt(1, order.getClientId());

			preparedStatement.setString(2, order.getOrderStatus());

			preparedStatement.setBigDecimal(3, order.getSubtotal());

			preparedStatement.setBigDecimal(4, order.getDiscountAmount());

			preparedStatement.setBigDecimal(5, order.getTaxAmount());

			preparedStatement.setBigDecimal(6, order.getTotalAmount());

			preparedStatement.setString(7, order.getCreditStatus());

			if (order.getApprovedBy() == null) {

				preparedStatement.setNull(8, java.sql.Types.INTEGER);

			} else {

				preparedStatement.setInt(8, order.getApprovedBy());
			}

			preparedStatement.setInt(9, order.getCreatedBy());

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {

				return 0;
			}

			try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {

				if (resultSet.next()) {

					return resultSet.getInt(1);
				}
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return 0;
	}

	// =========================================================
	// UPDATE ORDER STATUS
	// =========================================================

	public boolean updateOrderStatus(int orderId, String orderStatus) {

		String sql = """
				UPDATE orders
				SET order_status = ?
				WHERE order_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, orderStatus);

			preparedStatement.setInt(2, orderId);

			return preparedStatement.executeUpdate() > 0;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// MAP RESULT SET TO ORDER
	// =========================================================

	private Order mapOrder(ResultSet resultSet) throws SQLException {

		Order order = new Order();

		order.setOrderId(resultSet.getInt("order_id"));

		order.setClientId(resultSet.getInt("client_id"));

		order.setOrderDate(resultSet.getTimestamp("order_date"));

		order.setOrderStatus(resultSet.getString("order_status"));

		order.setSubtotal(resultSet.getBigDecimal("subtotal"));

		order.setDiscountAmount(resultSet.getBigDecimal("discount_amount"));

		order.setTaxAmount(resultSet.getBigDecimal("tax_amount"));

		order.setTotalAmount(resultSet.getBigDecimal("total_amount"));

		order.setCreditStatus(resultSet.getString("credit_status"));

		int approvedBy = resultSet.getInt("approved_by");

		if (resultSet.wasNull()) {

			order.setApprovedBy(null);

		} else {

			order.setApprovedBy(approvedBy);
		}

		order.setCreatedBy(resultSet.getInt("created_by"));

		order.setCreatedAt(resultSet.getTimestamp("created_at"));

		// Display fields

		order.setClientName(resultSet.getString("client_name"));

		order.setCreatedByName(resultSet.getString("created_by_name"));

		// NEW: number of products/order items

		order.setItemCount(resultSet.getInt("item_count"));

		return order;
	}
}