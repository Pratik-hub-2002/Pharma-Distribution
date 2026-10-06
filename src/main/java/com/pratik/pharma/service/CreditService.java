package com.pratik.pharma.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreditService {

	// =========================================================
	// CHECK CLIENT CREDIT
	// =========================================================

	public boolean hasSufficientCredit(Connection connection, int clientId, BigDecimal newOrderAmount) {

		String sql = """
				SELECT
				    c.credit_limit,
				    COALESCE(
				        SUM(
				            CASE
				                WHEN o.credit_status IN
				                    ('PENDING', 'APPROVED')
				                THEN o.total_amount
				                ELSE 0
				            END
				        ),
				        0
				    ) AS outstanding_amount
				FROM clients c
				LEFT JOIN orders o
				    ON c.client_id = o.client_id
				WHERE c.client_id = ?
				GROUP BY c.client_id, c.credit_limit
				""";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, clientId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					BigDecimal creditLimit = resultSet.getBigDecimal("credit_limit");

					BigDecimal outstandingAmount = resultSet.getBigDecimal("outstanding_amount");

					if (creditLimit == null) {
						return false;
					}

					BigDecimal availableCredit = creditLimit.subtract(outstandingAmount);

					return availableCredit.compareTo(newOrderAmount) >= 0;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// GET AVAILABLE CREDIT
	// =========================================================

	public BigDecimal getAvailableCredit(Connection connection, int clientId) {

		String sql = """
				SELECT
				    c.credit_limit,
				    COALESCE(
				        SUM(
				            CASE
				                WHEN o.credit_status IN
				                    ('PENDING', 'APPROVED')
				                THEN o.total_amount
				                ELSE 0
				            END
				        ),
				        0
				    ) AS outstanding_amount
				FROM clients c
				LEFT JOIN orders o
				    ON c.client_id = o.client_id
				WHERE c.client_id = ?
				GROUP BY c.client_id, c.credit_limit
				""";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, clientId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					BigDecimal creditLimit = resultSet.getBigDecimal("credit_limit");

					BigDecimal outstandingAmount = resultSet.getBigDecimal("outstanding_amount");

					if (creditLimit == null) {
						return BigDecimal.ZERO;
					}

					return creditLimit.subtract(outstandingAmount);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return BigDecimal.ZERO;
	}
}