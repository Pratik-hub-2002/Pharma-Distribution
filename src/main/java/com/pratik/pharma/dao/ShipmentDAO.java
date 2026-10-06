package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
	// UPDATE DELIVERY
	// =========================================================

	public boolean markDelivered(int shipmentId) {

		String sql = """
				UPDATE shipments
				SET
				    shipment_status = 'DELIVERED',
				    delivery_date = CURRENT_DATE
				WHERE shipment_id = ?
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