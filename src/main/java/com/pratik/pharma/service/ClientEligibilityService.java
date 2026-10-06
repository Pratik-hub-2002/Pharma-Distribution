package com.pratik.pharma.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClientEligibilityService {

	public boolean isEligible(Connection connection, int clientId) {

		String sql = """
				SELECT status, verification_status
				FROM clients
				WHERE client_id = ?
				""";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, clientId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (!resultSet.next()) {
					return false;
				}

				String status = resultSet.getString("status");

				String verificationStatus = resultSet.getString("verification_status");

				return "ACTIVE".equalsIgnoreCase(status) && "VERIFIED".equalsIgnoreCase(verificationStatus);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}