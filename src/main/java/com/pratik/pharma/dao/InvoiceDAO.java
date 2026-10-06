package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.pratik.pharma.model.Invoice;
import com.pratik.pharma.util.DBConnection;

public class InvoiceDAO {

	// =========================================================
	// GET INVOICE BY ORDER ID
	// =========================================================
	public Invoice getInvoiceByOrderId(int orderId) {

		Invoice invoice = null;

		String sql = """
				SELECT
				    invoice_id,
				    order_id,
				    invoice_number,
				    invoice_date,
				    subtotal,
				    discount_amount,
				    tax_amount,
				    total_amount,
				    due_date,
				    invoice_status,
				    created_by,
				    created_at
				FROM invoices
				WHERE order_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, orderId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {
					invoice = mapInvoice(resultSet);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return invoice;
	}

	// =========================================================
	// GET INVOICE BY ID
	// =========================================================
	public Invoice getInvoiceById(int invoiceId) {

		Invoice invoice = null;

		String sql = """
				SELECT
				    invoice_id,
				    order_id,
				    invoice_number,
				    invoice_date,
				    subtotal,
				    discount_amount,
				    tax_amount,
				    total_amount,
				    due_date,
				    invoice_status,
				    created_by,
				    created_at
				FROM invoices
				WHERE invoice_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, invoiceId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {
					invoice = mapInvoice(resultSet);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return invoice;
	}

	// =========================================================
	// CREATE INVOICE
	// =========================================================
	public boolean createInvoice(Invoice invoice) {

		String sql = """
				INSERT INTO invoices (
				    order_id,
				    invoice_number,
				    invoice_date,
				    subtotal,
				    discount_amount,
				    tax_amount,
				    total_amount,
				    due_date,
				    invoice_status,
				    created_by
				)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, invoice.getOrderId());

			statement.setString(2, invoice.getInvoiceNumber());

			statement.setDate(3, invoice.getInvoiceDate());

			statement.setBigDecimal(4, invoice.getSubtotal());

			statement.setBigDecimal(5, invoice.getDiscountAmount());

			statement.setBigDecimal(6, invoice.getTaxAmount());

			statement.setBigDecimal(7, invoice.getTotalAmount());

			if (invoice.getDueDate() != null) {

				statement.setDate(8, invoice.getDueDate());

			} else {

				statement.setNull(8, java.sql.Types.DATE);
			}

			statement.setString(9, invoice.getInvoiceStatus());

			statement.setInt(10, invoice.getCreatedBy());

			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return false;
	}

	// =========================================================
	// MAP RESULTSET → INVOICE
	// =========================================================
	private Invoice mapInvoice(ResultSet resultSet) throws Exception {

		Invoice invoice = new Invoice();

		invoice.setInvoiceId(resultSet.getInt("invoice_id"));

		invoice.setOrderId(resultSet.getInt("order_id"));

		invoice.setInvoiceNumber(resultSet.getString("invoice_number"));

		invoice.setInvoiceDate(resultSet.getDate("invoice_date"));

		invoice.setSubtotal(resultSet.getBigDecimal("subtotal"));

		invoice.setDiscountAmount(resultSet.getBigDecimal("discount_amount"));

		invoice.setTaxAmount(resultSet.getBigDecimal("tax_amount"));

		invoice.setTotalAmount(resultSet.getBigDecimal("total_amount"));

		invoice.setDueDate(resultSet.getDate("due_date"));

		invoice.setInvoiceStatus(resultSet.getString("invoice_status"));

		invoice.setCreatedBy(resultSet.getInt("created_by"));

		invoice.setCreatedAt(resultSet.getTimestamp("created_at"));

		return invoice;
	}
}