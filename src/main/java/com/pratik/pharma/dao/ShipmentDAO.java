package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Order;
import com.pratik.pharma.model.Shipment;
import com.pratik.pharma.util.DBConnection;

public class ShipmentDAO {

	// =========================================================
	// GET ALL SHIPMENTS
	// =========================================================

	public List<Shipment> getAllShipments() {

		List<Shipment> shipments = new ArrayList<>();

		String sql = """
				SELECT
				    s.shipment_id,
				    s.order_id,
				    s.tracking_number,
				    s.shipment_date,
				    s.dispatch_date,
				    s.delivery_date,
				    s.shipment_status,
				    s.cold_chain_required,
				    s.temperature_status,
				    s.created_by,
				    s.created_at,

				    c.client_name,

				    u.username AS created_by_name

				FROM shipments s

				INNER JOIN orders o
				    ON s.order_id = o.order_id

				INNER JOIN clients c
				    ON o.client_id = c.client_id

				INNER JOIN users u
				    ON s.created_by = u.user_id

				ORDER BY s.shipment_id DESC
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {

				shipments.add(mapShipment(resultSet));
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return shipments;
	}

	// =========================================================
	// GET SHIPMENT BY ID
	// =========================================================

	public Shipment getShipmentById(int shipmentId) {

		Shipment shipment = null;

		String sql = """
				SELECT
				    s.shipment_id,
				    s.order_id,
				    s.tracking_number,
				    s.shipment_date,
				    s.dispatch_date,
				    s.delivery_date,
				    s.shipment_status,
				    s.cold_chain_required,
				    s.temperature_status,
				    s.created_by,
				    s.created_at,

				    c.client_name,

				    u.username AS created_by_name

				FROM shipments s

				INNER JOIN orders o
				    ON s.order_id = o.order_id

				INNER JOIN clients c
				    ON o.client_id = c.client_id

				INNER JOIN users u
				    ON s.created_by = u.user_id

				WHERE s.shipment_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, shipmentId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					shipment = mapShipment(resultSet);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return shipment;
	}

	// =========================================================
	// GET SHIPMENT BY ORDER ID
	// =========================================================

	public Shipment getShipmentByOrderId(int orderId) {

		Shipment shipment = null;

		String sql = """
				SELECT
				    s.shipment_id,
				    s.order_id,
				    s.tracking_number,
				    s.shipment_date,
				    s.dispatch_date,
				    s.delivery_date,
				    s.shipment_status,
				    s.cold_chain_required,
				    s.temperature_status,
				    s.created_by,
				    s.created_at,

				    c.client_name,

				    u.username AS created_by_name

				FROM shipments s

				INNER JOIN orders o
				    ON s.order_id = o.order_id

				INNER JOIN clients c
				    ON o.client_id = c.client_id

				INNER JOIN users u
				    ON s.created_by = u.user_id

				WHERE s.order_id = ?

				ORDER BY s.shipment_id DESC

				LIMIT 1
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, orderId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					shipment = mapShipment(resultSet);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return shipment;
	}

	// =========================================================
	// ADD SHIPMENT
	// =========================================================

	public boolean addShipment(Shipment shipment) {

		String sql = """
				INSERT INTO shipments (
				    order_id,
				    tracking_number,
				    shipment_date,
				    dispatch_date,
				    delivery_date,
				    shipment_status,
				    cold_chain_required,
				    temperature_status,
				    created_by
				)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, shipment.getOrderId());

			statement.setString(2, shipment.getTrackingNumber());

			if (shipment.getShipmentDate() != null) {

				statement.setDate(3, shipment.getShipmentDate());

			} else {

				statement.setNull(3, java.sql.Types.DATE);
			}

			if (shipment.getDispatchDate() != null) {

				statement.setDate(4, shipment.getDispatchDate());

			} else {

				statement.setNull(4, java.sql.Types.DATE);
			}

			if (shipment.getDeliveryDate() != null) {

				statement.setDate(5, shipment.getDeliveryDate());

			} else {

				statement.setNull(5, java.sql.Types.DATE);
			}

			statement.setString(6, shipment.getShipmentStatus());

			statement.setBoolean(7, shipment.isColdChainRequired());

			statement.setString(8, shipment.getTemperatureStatus());

			statement.setInt(9, shipment.getCreatedBy());

			return statement.executeUpdate() > 0;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// UPDATE SHIPMENT STATUS
	// =========================================================

	public boolean updateShipmentStatus(int shipmentId, String status) {

		String sql = """
				UPDATE shipments
				SET shipment_status = ?
				WHERE shipment_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, status);

			statement.setInt(2, shipmentId);

			return statement.executeUpdate() > 0;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// UPDATE DISPATCH
	// =========================================================

	public boolean markDispatched(int shipmentId) {

		String sql = """
				UPDATE shipments
				SET
				    shipment_status = 'DISPATCHED',
				    dispatch_date = CURRENT_DATE
				WHERE shipment_id = ?
				AND shipment_status = 'PENDING'
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, shipmentId);

			return statement.executeUpdate() > 0;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// UPDATE DELIVERY + COMPLETE ORDER
	// =========================================================

	public boolean markDelivered(int shipmentId) {

		String getOrderIdSQL = """
				SELECT order_id
				FROM shipments
				WHERE shipment_id = ?
				""";

		String updateShipmentSQL = """
				UPDATE shipments
				SET
				    shipment_status = 'DELIVERED',
				    delivery_date = CURRENT_DATE
				WHERE shipment_id = ?
				AND shipment_status = 'DISPATCHED'
				""";

		/*
		 * The order is normally APPROVED when the shipment is created.
		 *
		 * After successful delivery, the order becomes COMPLETED.
		 */
		String updateOrderSQL = """
				UPDATE orders
				SET order_status = 'COMPLETED'
				WHERE order_id = ?
				AND order_status = 'APPROVED'
				""";

		Connection connection = null;

		try {

			connection = DBConnection.getConnection();

			// Start transaction
			connection.setAutoCommit(false);

			// -------------------------------------------------
			// 1. GET ORDER ID FROM SHIPMENT
			// -------------------------------------------------

			int orderId = 0;

			try (PreparedStatement statement = connection.prepareStatement(getOrderIdSQL)) {

				statement.setInt(1, shipmentId);

				try (ResultSet resultSet = statement.executeQuery()) {

					if (resultSet.next()) {

						orderId = resultSet.getInt("order_id");

					} else {

						connection.rollback();

						return false;
					}
				}
			}

			// -------------------------------------------------
			// 2. MARK SHIPMENT AS DELIVERED
			// -------------------------------------------------

			int shipmentUpdated;

			try (PreparedStatement statement = connection.prepareStatement(updateShipmentSQL)) {

				statement.setInt(1, shipmentId);

				shipmentUpdated = statement.executeUpdate();
			}

			/*
			 * Only a DISPATCHED shipment can be delivered.
			 */
			if (shipmentUpdated == 0) {

				connection.rollback();

				return false;
			}

			// -------------------------------------------------
			// 3. MARK RELATED ORDER AS COMPLETED
			// -------------------------------------------------

			int orderUpdated;

			try (PreparedStatement statement = connection.prepareStatement(updateOrderSQL)) {

				statement.setInt(1, orderId);

				orderUpdated = statement.executeUpdate();
			}

			/*
			 * If the order was not APPROVED, rollback the shipment update too.
			 */
			if (orderUpdated == 0) {

				connection.rollback();

				return false;
			}

			// -------------------------------------------------
			// 4. COMMIT
			// -------------------------------------------------

			connection.commit();

			return true;

		} catch (Exception e) {

			e.printStackTrace();

			// Rollback if anything fails
			if (connection != null) {

				try {

					connection.rollback();

				} catch (Exception rollbackException) {

					rollbackException.printStackTrace();
				}
			}

		} finally {

			if (connection != null) {

				try {

					connection.setAutoCommit(true);

					connection.close();

				} catch (Exception closeException) {

					closeException.printStackTrace();
				}
			}
		}

		return false;
	}

	// =========================================================
	// UPDATE TEMPERATURE STATUS
	// =========================================================

	public boolean updateTemperatureStatus(int shipmentId, String temperatureStatus) {

		String sql = """
				UPDATE shipments
				SET temperature_status = ?
				WHERE shipment_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, temperatureStatus);

			statement.setInt(2, shipmentId);

			return statement.executeUpdate() > 0;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// GET ORDERS ELIGIBLE FOR SHIPMENT
	// =========================================================

	public List<Order> getOrdersEligibleForShipment() {

		List<Order> orders = new ArrayList<>();

		/*
		 * CREDIT-CYCLE WORKFLOW
		 *
		 * Dispatcher must approve the order.
		 *
		 * Accountant must create an invoice.
		 *
		 * Payment is NOT required before shipment.
		 *
		 * This allows credit customers to receive stock and pay within their approved
		 * credit period.
		 */
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

				    i.invoice_id,
				    i.invoice_number,
				    i.invoice_status,
				    i.due_date

				FROM orders o

				INNER JOIN clients c
				    ON o.client_id = c.client_id

				INNER JOIN invoices i
				    ON o.order_id = i.order_id

				LEFT JOIN shipments s
				    ON o.order_id = s.order_id

				WHERE s.shipment_id IS NULL

				AND o.order_status = 'APPROVED'

				ORDER BY o.order_id DESC
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {

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

				orders.add(order);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return orders;
	}

	// =========================================================
	// MAP RESULT SET
	// =========================================================

	private Shipment mapShipment(ResultSet resultSet) throws Exception {

		Shipment shipment = new Shipment();

		shipment.setShipmentId(resultSet.getInt("shipment_id"));

		shipment.setOrderId(resultSet.getInt("order_id"));

		shipment.setTrackingNumber(resultSet.getString("tracking_number"));

		shipment.setShipmentDate(resultSet.getDate("shipment_date"));

		shipment.setDispatchDate(resultSet.getDate("dispatch_date"));

		shipment.setDeliveryDate(resultSet.getDate("delivery_date"));

		shipment.setShipmentStatus(resultSet.getString("shipment_status"));

		shipment.setColdChainRequired(resultSet.getBoolean("cold_chain_required"));

		shipment.setTemperatureStatus(resultSet.getString("temperature_status"));

		shipment.setCreatedBy(resultSet.getInt("created_by"));

		shipment.setCreatedAt(resultSet.getTimestamp("created_at"));

		shipment.setClientName(resultSet.getString("client_name"));

		shipment.setCreatedByName(resultSet.getString("created_by_name"));

		return shipment;
	}
}