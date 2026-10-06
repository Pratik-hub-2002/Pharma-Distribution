package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.OrderItem;
import com.pratik.pharma.util.DBConnection;

public class OrderItemDAO {

	// =========================================================
	// GET ALL ITEMS OF A PARTICULAR ORDER
	// =========================================================

	public List<OrderItem> getItemsByOrderId(int orderId) {

		List<OrderItem> items = new ArrayList<>();

		String sql = """
				SELECT
				    oi.order_item_id,
				    oi.order_id,
				    oi.product_id,
				    oi.ordered_quantity,
				    oi.free_quantity,
				    oi.total_quantity,
				    oi.unit_price,
				    oi.discount_amount,
				    oi.tax_amount,
				    oi.line_total,
				    oi.scheme_id,

				    p.product_name,
				    p.product_code,

				    s.scheme_name

				FROM order_items oi

				JOIN products p
				    ON oi.product_id = p.product_id

				LEFT JOIN schemes s
				    ON oi.scheme_id = s.scheme_id

				WHERE oi.order_id = ?

				ORDER BY oi.order_item_id
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, orderId);

			try (ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {

					OrderItem item = mapOrderItem(resultSet);

					items.add(item);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return items;
	}

	// =========================================================
	// GET ONE ORDER ITEM
	// =========================================================

	public OrderItem getOrderItemById(int orderItemId) {

		OrderItem item = null;

		String sql = """
				SELECT
				    oi.order_item_id,
				    oi.order_id,
				    oi.product_id,
				    oi.ordered_quantity,
				    oi.free_quantity,
				    oi.total_quantity,
				    oi.unit_price,
				    oi.discount_amount,
				    oi.tax_amount,
				    oi.line_total,
				    oi.scheme_id,

				    p.product_name,
				    p.product_code,

				    s.scheme_name

				FROM order_items oi

				JOIN products p
				    ON oi.product_id = p.product_id

				LEFT JOIN schemes s
				    ON oi.scheme_id = s.scheme_id

				WHERE oi.order_item_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, orderItemId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					item = mapOrderItem(resultSet);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return item;
	}

	// =========================================================
	// ADD ORDER ITEM
	// =========================================================
	// Normal version - creates its own connection
	// =========================================================

	public int addOrderItem(OrderItem item) {

		int orderItemId = 0;

		String sql = """
				INSERT INTO order_items
				(
				    order_id,
				    product_id,
				    ordered_quantity,
				    free_quantity,
				    total_quantity,
				    unit_price,
				    discount_amount,
				    tax_amount,
				    line_total,
				    scheme_id
				)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			statement.setInt(1, item.getOrderId());
			statement.setInt(2, item.getProductId());
			statement.setInt(3, item.getOrderedQuantity());
			statement.setInt(4, item.getFreeQuantity());
			statement.setInt(5, item.getTotalQuantity());
			statement.setBigDecimal(6, item.getUnitPrice());
			statement.setBigDecimal(7, item.getDiscountAmount());
			statement.setBigDecimal(8, item.getTaxAmount());
			statement.setBigDecimal(9, item.getLineTotal());

			if (item.getSchemeId() == null) {

				statement.setNull(10, Types.INTEGER);

			} else {

				statement.setInt(10, item.getSchemeId());
			}

			statement.executeUpdate();

			try (ResultSet resultSet = statement.getGeneratedKeys()) {

				if (resultSet.next()) {

					orderItemId = resultSet.getInt(1);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return orderItemId;
	}

	// =========================================================
	// ADD ORDER ITEM - TRANSACTION VERSION
	// =========================================================
	// IMPORTANT:
	// Uses the connection supplied by OrderServlet.
	// Does NOT create or close the connection.
	// =========================================================

	public int addOrderItem(Connection connection, OrderItem item) {

		int orderItemId = 0;

		String sql = """
				INSERT INTO order_items
				(
				    order_id,
				    product_id,
				    ordered_quantity,
				    free_quantity,
				    total_quantity,
				    unit_price,
				    discount_amount,
				    tax_amount,
				    line_total,
				    scheme_id
				)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			statement.setInt(1, item.getOrderId());
			statement.setInt(2, item.getProductId());
			statement.setInt(3, item.getOrderedQuantity());
			statement.setInt(4, item.getFreeQuantity());
			statement.setInt(5, item.getTotalQuantity());
			statement.setBigDecimal(6, item.getUnitPrice());
			statement.setBigDecimal(7, item.getDiscountAmount());
			statement.setBigDecimal(8, item.getTaxAmount());
			statement.setBigDecimal(9, item.getLineTotal());

			if (item.getSchemeId() == null) {

				statement.setNull(10, Types.INTEGER);

			} else {

				statement.setInt(10, item.getSchemeId());
			}

			statement.executeUpdate();

			try (ResultSet resultSet = statement.getGeneratedKeys()) {

				if (resultSet.next()) {

					orderItemId = resultSet.getInt(1);
				}
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return orderItemId;
	}

	// =========================================================
	// DELETE ORDER ITEM
	// =========================================================

	public boolean deleteOrderItem(int orderItemId) {

		String sql = """
				DELETE FROM order_items
				WHERE order_item_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, orderItemId);

			return statement.executeUpdate() > 0;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// MAP RESULTSET TO ORDER ITEM
	// =========================================================

	private OrderItem mapOrderItem(ResultSet resultSet) throws SQLException {

		OrderItem item = new OrderItem();

		item.setOrderItemId(resultSet.getInt("order_item_id"));

		item.setOrderId(resultSet.getInt("order_id"));

		item.setProductId(resultSet.getInt("product_id"));

		item.setOrderedQuantity(resultSet.getInt("ordered_quantity"));

		item.setFreeQuantity(resultSet.getInt("free_quantity"));

		item.setTotalQuantity(resultSet.getInt("total_quantity"));

		item.setUnitPrice(resultSet.getBigDecimal("unit_price"));

		item.setDiscountAmount(resultSet.getBigDecimal("discount_amount"));

		item.setTaxAmount(resultSet.getBigDecimal("tax_amount"));

		item.setLineTotal(resultSet.getBigDecimal("line_total"));

		// scheme_id can be NULL
		int schemeId = resultSet.getInt("scheme_id");

		if (resultSet.wasNull()) {

			item.setSchemeId(null);

		} else {

			item.setSchemeId(schemeId);
		}

		item.setProductName(resultSet.getString("product_name"));

		item.setProductCode(resultSet.getString("product_code"));

		item.setSchemeName(resultSet.getString("scheme_name"));

		return item;
	}
}