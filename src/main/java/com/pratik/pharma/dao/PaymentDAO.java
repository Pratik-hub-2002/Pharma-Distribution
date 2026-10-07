package com.pratik.pharma.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Payment;
import com.pratik.pharma.util.DBConnection;

public class PaymentDAO {

	// =========================================================
	// GET ALL PAYMENTS
	// =========================================================

	public List<Payment> getAllPayments() {

		List<Payment> payments = new ArrayList<>();

		String sql = """
				SELECT
				    p.payment_id,
				    p.invoice_id,
				    p.payment_date,
				    p.amount,
				    p.payment_method,
				    p.transaction_reference,
				    p.payment_status,
				    p.recorded_by,
				    p.created_at,

				    i.invoice_number,
				    i.total_amount AS invoice_total_amount,
				    i.due_date AS invoice_due_date,
				    i.order_id,

				    c.client_name,

				    u.username AS recorded_by_name,

				    COALESCE(
				        (
				            SELECT SUM(p2.amount)
				            FROM payments p2
				            WHERE p2.invoice_id = p.invoice_id
				            AND p2.payment_status = 'SUCCESS'
				        ),
				        0
				    ) AS paid_amount

				FROM payments p

				INNER JOIN invoices i
				    ON p.invoice_id = i.invoice_id

				INNER JOIN orders o
				    ON i.order_id = o.order_id

				INNER JOIN clients c
				    ON o.client_id = c.client_id

				INNER JOIN users u
				    ON p.recorded_by = u.user_id

				ORDER BY p.payment_id DESC
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {

				Payment payment = mapPayment(resultSet);

				payments.add(payment);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return payments;
	}

	// =========================================================
	// GET OUTSTANDING INVOICES
	// =========================================================

	public List<Payment> getOutstandingInvoices() {

		List<Payment> outstandingInvoices = new ArrayList<>();

		String sql = "SELECT " + "i.invoice_id, " + "i.invoice_number, " + "i.order_id, " + "i.total_amount, "
				+ "i.due_date, " + "c.client_name, " + "COALESCE(SUM(p.amount), 0) AS paid_amount, "
				+ "(i.total_amount - COALESCE(SUM(p.amount), 0)) AS outstanding_amount " +

				"FROM invoices i " +

				"JOIN orders o " + "ON i.order_id = o.order_id " +

				"JOIN clients c " + "ON o.client_id = c.client_id " +

				"LEFT JOIN payments p " + "ON i.invoice_id = p.invoice_id " + "AND p.payment_status = 'SUCCESS' " +

				"GROUP BY " + "i.invoice_id, " + "i.invoice_number, " + "i.order_id, " + "i.total_amount, "
				+ "i.due_date, " + "c.client_name " +

				"HAVING " + "(i.total_amount - COALESCE(SUM(p.amount), 0)) > 0 " +

				"ORDER BY " + "i.due_date ASC, " + "i.invoice_id ASC";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {

				Payment payment = new Payment();

				payment.setInvoiceId(resultSet.getInt("invoice_id"));

				payment.setInvoiceNumber(resultSet.getString("invoice_number"));

				payment.setOrderId(resultSet.getInt("order_id"));

				payment.setInvoiceTotalAmount(resultSet.getBigDecimal("total_amount"));

				payment.setInvoiceDueDate(resultSet.getDate("due_date"));

				payment.setClientName(resultSet.getString("client_name"));

				payment.setPaidAmount(resultSet.getBigDecimal("paid_amount"));

				payment.setOutstandingAmount(resultSet.getBigDecimal("outstanding_amount"));

				outstandingInvoices.add(payment);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return outstandingInvoices;
	}

	// =========================================================
	// GET PAYMENT BY ID
	// =========================================================

	public Payment getPaymentById(int paymentId) {

		Payment payment = null;

		String sql = """
				SELECT
				    p.payment_id,
				    p.invoice_id,
				    p.payment_date,
				    p.amount,
				    p.payment_method,
				    p.transaction_reference,
				    p.payment_status,
				    p.recorded_by,
				    p.created_at,

				    i.invoice_number,
				    i.total_amount AS invoice_total_amount,
				    i.due_date AS invoice_due_date,
				    i.order_id,

				    c.client_name,

				    u.username AS recorded_by_name,

				    COALESCE(
				        (
				            SELECT SUM(p2.amount)
				            FROM payments p2
				            WHERE p2.invoice_id = p.invoice_id
				            AND p2.payment_status = 'SUCCESS'
				        ),
				        0
				    ) AS paid_amount

				FROM payments p

				INNER JOIN invoices i
				    ON p.invoice_id = i.invoice_id

				INNER JOIN orders o
				    ON i.order_id = o.order_id

				INNER JOIN clients c
				    ON o.client_id = c.client_id

				INNER JOIN users u
				    ON p.recorded_by = u.user_id

				WHERE p.payment_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, paymentId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					payment = mapPayment(resultSet);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return payment;
	}

	// =========================================================
	// GET PAYMENTS BY INVOICE
	// =========================================================

	public List<Payment> getPaymentsByInvoiceId(int invoiceId) {

		List<Payment> payments = new ArrayList<>();

		String sql = """
				SELECT
				    p.payment_id,
				    p.invoice_id,
				    p.payment_date,
				    p.amount,
				    p.payment_method,
				    p.transaction_reference,
				    p.payment_status,
				    p.recorded_by,
				    p.created_at,

				    i.invoice_number,
				    i.total_amount AS invoice_total_amount,
				    i.due_date AS invoice_due_date,
				    i.order_id,

				    c.client_name,

				    u.username AS recorded_by_name,

				    COALESCE(
				        (
				            SELECT SUM(p2.amount)
				            FROM payments p2
				            WHERE p2.invoice_id = p.invoice_id
				            AND p2.payment_status = 'SUCCESS'
				        ),
				        0
				    ) AS paid_amount

				FROM payments p

				INNER JOIN invoices i
				    ON p.invoice_id = i.invoice_id

				INNER JOIN orders o
				    ON i.order_id = o.order_id

				INNER JOIN clients c
				    ON o.client_id = c.client_id

				INNER JOIN users u
				    ON p.recorded_by = u.user_id

				WHERE p.invoice_id = ?

				ORDER BY p.payment_id DESC
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, invoiceId);

			try (ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {

					payments.add(mapPayment(resultSet));
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return payments;
	}

	// =========================================================
	// ADD PAYMENT
	// =========================================================

	public boolean addPayment(Payment payment) {

		String sql = """
				INSERT INTO payments (
				    invoice_id,
				    payment_date,
				    amount,
				    payment_method,
				    transaction_reference,
				    payment_status,
				    recorded_by
				)
				VALUES (?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, payment.getInvoiceId());

			statement.setDate(2, payment.getPaymentDate());

			statement.setBigDecimal(3, payment.getAmount());

			statement.setString(4, payment.getPaymentMethod());

			statement.setString(5, payment.getTransactionReference());

			statement.setString(6, payment.getPaymentStatus());

			statement.setInt(7, payment.getRecordedBy());

			return statement.executeUpdate() > 0;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// GET TOTAL SUCCESSFUL PAYMENTS
	// =========================================================

	public BigDecimal getPaidAmountForInvoice(int invoiceId) {

		String sql = """
				SELECT
				    COALESCE(
				        SUM(amount),
				        0
				    ) AS paid_amount

				FROM payments

				WHERE invoice_id = ?

				AND payment_status = 'SUCCESS'
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, invoiceId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					return resultSet.getBigDecimal("paid_amount");
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return BigDecimal.ZERO;
	}

	// =========================================================
	// MAP PAYMENT
	// =========================================================

	private Payment mapPayment(ResultSet resultSet) throws Exception {

		Payment payment = new Payment();

		payment.setPaymentId(resultSet.getInt("payment_id"));

		payment.setInvoiceId(resultSet.getInt("invoice_id"));

		payment.setPaymentDate(resultSet.getDate("payment_date"));

		payment.setAmount(resultSet.getBigDecimal("amount"));

		payment.setPaymentMethod(resultSet.getString("payment_method"));

		payment.setTransactionReference(resultSet.getString("transaction_reference"));

		payment.setPaymentStatus(resultSet.getString("payment_status"));

		payment.setRecordedBy(resultSet.getInt("recorded_by"));

		payment.setCreatedAt(resultSet.getTimestamp("created_at"));

		payment.setInvoiceNumber(resultSet.getString("invoice_number"));

		payment.setInvoiceTotalAmount(resultSet.getBigDecimal("invoice_total_amount"));

		payment.setInvoiceDueDate(resultSet.getDate("invoice_due_date"));

		payment.setOrderId(resultSet.getInt("order_id"));

		payment.setClientName(resultSet.getString("client_name"));

		payment.setRecordedByName(resultSet.getString("recorded_by_name"));

		BigDecimal paidAmount = resultSet.getBigDecimal("paid_amount");

		if (paidAmount == null) {

			paidAmount = BigDecimal.ZERO;
		}

		payment.setPaidAmount(paidAmount);

		BigDecimal invoiceTotal = payment.getInvoiceTotalAmount();

		if (invoiceTotal == null) {

			invoiceTotal = BigDecimal.ZERO;
		}

		BigDecimal outstanding = invoiceTotal.subtract(paidAmount);

		if (outstanding.compareTo(BigDecimal.ZERO) < 0) {

			outstanding = BigDecimal.ZERO;
		}

		payment.setOutstandingAmount(outstanding);

		return payment;
	}
}