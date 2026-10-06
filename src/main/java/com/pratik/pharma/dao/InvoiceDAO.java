package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Invoice;
import com.pratik.pharma.util.DBConnection;

public class InvoiceDAO {

	// =========================================================
	// GET ALL INVOICES
	// =========================================================

	public List<Invoice> getAllInvoices() {

		List<Invoice> invoices = new ArrayList<>();

		String sql = """
				SELECT
				    i.invoice_id,
				    i.order_id,
				    i.invoice_number,
				    i.invoice_date,
				    i.subtotal,
				    i.discount_amount,
				    i.tax_amount,
				    i.total_amount,
				    i.due_date,
				    i.invoice_status,
				    i.created_by,
				    i.created_at
				FROM invoices i
				ORDER BY i.invoice_id DESC
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {

				Invoice invoice = mapInvoice(resultSet);

				invoices.add(invoice);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return invoices;
	}

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
	// CREATE INVOICE - TRANSACTION VERSION
	// =========================================================

	public int createInvoice(Connection connection, Invoice invoice) throws Exception {

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

		try (PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

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

			if (rowsAffected == 0) {
				throw new Exception("Invoice creation failed.");
			}

			try (ResultSet resultSet = statement.getGeneratedKeys()) {

				if (resultSet.next()) {

					return resultSet.getInt(1);
				}
			}
		}

		throw new Exception("Invoice ID could not be generated.");
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