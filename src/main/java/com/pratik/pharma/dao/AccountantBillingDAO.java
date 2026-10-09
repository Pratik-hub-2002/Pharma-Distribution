package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Order;
import com.pratik.pharma.util.DBConnection;

public class AccountantBillingDAO {

	// =========================================================
	// GET ORDERS READY FOR BILLING
	// =========================================================

	public List<Order> getOrdersReadyForBilling() {

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
				    c.client_name,
				    u.username AS created_by_name
				FROM orders o
				INNER JOIN clients c
				    ON o.client_id = c.client_id
				LEFT JOIN users u
				    ON o.created_by = u.user_id
				LEFT JOIN invoices i
				    ON o.order_id = i.order_id
				WHERE o.order_status = 'APPROVED'
				AND i.invoice_id IS NULL
				ORDER BY o.order_id DESC
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {

				orders.add(mapOrder(resultSet));
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return orders;
	}

	// =========================================================
	// GET APPROVED ORDER BY ID
	// =========================================================

	public Order getApprovedOrderById(int orderId) {

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
				    c.client_name,
				    u.username AS created_by_name
				FROM orders o
				INNER JOIN clients c
				    ON o.client_id = c.client_id
				LEFT JOIN users u
				    ON o.created_by = u.user_id
				LEFT JOIN invoices i
				    ON o.order_id = i.order_id
				WHERE o.order_id = ?
				AND o.order_status = 'APPROVED'
				AND i.invoice_id IS NULL
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, orderId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					return mapOrder(resultSet);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	// =========================================================
	// MAP ORDER
	// =========================================================

	private Order mapOrder(ResultSet resultSet) throws Exception {

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

		order.setClientName(resultSet.getString("client_name"));

		order.setCreatedByName(resultSet.getString("created_by_name"));

		return order;
	}
}