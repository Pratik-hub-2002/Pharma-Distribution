package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Client;
import com.pratik.pharma.util.DBConnection;

public class ClientDAO {

	// =========================================================
	// GET ALL CLIENTS
	// =========================================================
	public List<Client> getAllClients() {

		List<Client> clients = new ArrayList<>();

		String sql = """
				SELECT
				    client_id,
				    client_name,
				    client_type,
				    owner_name,
				    contact_person,
				    email,
				    phone,
				    address,
				    city,
				    state,
				    pincode,
				    gst_number,
				    pan_number,
				    drug_license_number,
				    wholesale_license_number,
				    purchase_permit_number,
				    credit_limit,
				    credit_period_days,
				    status,
				    verification_status,
				    created_at,
				    updated_at
				FROM clients
				ORDER BY client_id DESC
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {

				Client client = mapClient(resultSet);

				clients.add(client);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clients;
	}

	// =========================================================
	// ADD CLIENT
	// =========================================================
	public boolean addClient(Client client) {

		String sql = """
				INSERT INTO clients (
				    client_name,
				    client_type,
				    owner_name,
				    contact_person,
				    email,
				    phone,
				    address,
				    city,
				    state,
				    pincode,
				    gst_number,
				    pan_number,
				    drug_license_number,
				    wholesale_license_number,
				    purchase_permit_number,
				    credit_limit,
				    credit_period_days,
				    status,
				    verification_status
				)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, client.getClientName());
			statement.setString(2, client.getClientType());
			statement.setString(3, client.getOwnerName());
			statement.setString(4, client.getContactPerson());
			statement.setString(5, client.getEmail());
			statement.setString(6, client.getPhone());
			statement.setString(7, client.getAddress());
			statement.setString(8, client.getCity());
			statement.setString(9, client.getState());
			statement.setString(10, client.getPincode());

			// Business / Legal Details
			statement.setString(11, client.getGstNumber());
			statement.setString(12, client.getPanNumber());
			statement.setString(13, client.getDrugLicenseNumber());
			statement.setString(14, client.getWholesaleLicenseNumber());
			statement.setString(15, client.getPurchasePermitNumber());

			// Credit Details
			statement.setBigDecimal(16, client.getCreditLimit());
			statement.setInt(17, client.getCreditPeriodDays());

			statement.setString(18, client.getStatus());
			statement.setString(19, client.getVerificationStatus());

			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// GET CLIENT BY ID
	// =========================================================
	public Client getClientById(int clientId) {

		Client client = null;

		String sql = """
				SELECT
				    client_id,
				    client_name,
				    client_type,
				    owner_name,
				    contact_person,
				    email,
				    phone,
				    address,
				    city,
				    state,
				    pincode,
				    gst_number,
				    pan_number,
				    drug_license_number,
				    wholesale_license_number,
				    purchase_permit_number,
				    credit_limit,
				    credit_period_days,
				    status,
				    verification_status,
				    created_at,
				    updated_at
				FROM clients
				WHERE client_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, clientId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					client = mapClient(resultSet);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return client;
	}

	// =========================================================
	// UPDATE CLIENT
	// =========================================================
	public boolean updateClient(Client client) {

		String sql = """
				UPDATE clients
				SET
				    client_name = ?,
				    client_type = ?,
				    owner_name = ?,
				    contact_person = ?,
				    email = ?,
				    phone = ?,
				    address = ?,
				    city = ?,
				    state = ?,
				    pincode = ?,
				    gst_number = ?,
				    pan_number = ?,
				    drug_license_number = ?,
				    wholesale_license_number = ?,
				    purchase_permit_number = ?,
				    credit_limit = ?,
				    credit_period_days = ?,
				    status = ?,
				    verification_status = ?
				WHERE client_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, client.getClientName());
			statement.setString(2, client.getClientType());
			statement.setString(3, client.getOwnerName());
			statement.setString(4, client.getContactPerson());
			statement.setString(5, client.getEmail());
			statement.setString(6, client.getPhone());
			statement.setString(7, client.getAddress());
			statement.setString(8, client.getCity());
			statement.setString(9, client.getState());
			statement.setString(10, client.getPincode());

			// Business / Legal Details
			statement.setString(11, client.getGstNumber());
			statement.setString(12, client.getPanNumber());
			statement.setString(13, client.getDrugLicenseNumber());
			statement.setString(14, client.getWholesaleLicenseNumber());
			statement.setString(15, client.getPurchasePermitNumber());

			// Credit Details
			statement.setBigDecimal(16, client.getCreditLimit());
			statement.setInt(17, client.getCreditPeriodDays());

			statement.setString(18, client.getStatus());
			statement.setString(19, client.getVerificationStatus());

			statement.setInt(20, client.getClientId());

			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// UPDATE CLIENT STATUS
	// =========================================================
	public boolean updateClientStatus(int clientId, String status) {

		String sql = """
				UPDATE clients
				SET status = ?
				WHERE client_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, status);
			preparedStatement.setInt(2, clientId);

			int rows = preparedStatement.executeUpdate();

			return rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// =========================================================
	// MAP RESULTSET → CLIENT
	// =========================================================
	private Client mapClient(ResultSet resultSet) throws SQLException {

		Client client = new Client();

		client.setClientId(resultSet.getInt("client_id"));

		client.setClientName(resultSet.getString("client_name"));

		client.setClientType(resultSet.getString("client_type"));

		client.setOwnerName(resultSet.getString("owner_name"));

		client.setContactPerson(resultSet.getString("contact_person"));

		client.setEmail(resultSet.getString("email"));

		client.setPhone(resultSet.getString("phone"));

		client.setAddress(resultSet.getString("address"));

		client.setCity(resultSet.getString("city"));

		client.setState(resultSet.getString("state"));

		client.setPincode(resultSet.getString("pincode"));

		// Business / Legal Details
		client.setGstNumber(resultSet.getString("gst_number"));

		client.setPanNumber(resultSet.getString("pan_number"));

		client.setDrugLicenseNumber(resultSet.getString("drug_license_number"));

		client.setWholesaleLicenseNumber(resultSet.getString("wholesale_license_number"));

		client.setPurchasePermitNumber(resultSet.getString("purchase_permit_number"));

		// Credit Details
		client.setCreditLimit(resultSet.getBigDecimal("credit_limit"));

		client.setCreditPeriodDays(resultSet.getInt("credit_period_days"));

		client.setStatus(resultSet.getString("status"));

		client.setVerificationStatus(resultSet.getString("verification_status"));

		client.setCreatedAt(resultSet.getTimestamp("created_at"));

		client.setUpdatedAt(resultSet.getTimestamp("updated_at"));

		return client;
	}
}