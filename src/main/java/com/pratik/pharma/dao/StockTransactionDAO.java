package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.StockTransaction;
import com.pratik.pharma.util.DBConnection;

public class StockTransactionDAO {

	public List<StockTransaction> getAllTransactions() {

		List<StockTransaction> list = new ArrayList<>();

		String sql = "SELECT st.transaction_id, st.batch_id, " + "st.transaction_type, st.quantity, "
				+ "st.reference_type, st.reference_id, " + "st.remarks, st.created_by, st.created_at, "
				+ "b.batch_number, " + "p.product_name, p.product_code, " + "u.username AS created_by_name "
				+ "FROM stock_transactions st " + "INNER JOIN batches b " + "ON st.batch_id = b.batch_id "
				+ "INNER JOIN products p " + "ON b.product_id = p.product_id " + "INNER JOIN users u "
				+ "ON st.created_by = u.user_id " + "ORDER BY st.created_at DESC";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				list.add(mapTransaction(rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public boolean addTransaction(StockTransaction transaction) {

		String sql = "INSERT INTO stock_transactions " + "(batch_id, transaction_type, quantity, "
				+ "reference_type, reference_id, remarks, created_by) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, transaction.getBatchId());

			ps.setString(2, transaction.getTransactionType());

			ps.setInt(3, transaction.getQuantity());

			ps.setString(4, transaction.getReferenceType());

			if (transaction.getReferenceId() != null) {
				ps.setInt(5, transaction.getReferenceId());
			} else {
				ps.setNull(5, java.sql.Types.INTEGER);
			}

			ps.setString(6, transaction.getRemarks());

			ps.setInt(7, transaction.getCreatedBy());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private StockTransaction mapTransaction(ResultSet rs) throws Exception {

		StockTransaction transaction = new StockTransaction();

		transaction.setTransactionId(rs.getInt("transaction_id"));

		transaction.setBatchId(rs.getInt("batch_id"));

		transaction.setTransactionType(rs.getString("transaction_type"));

		transaction.setQuantity(rs.getInt("quantity"));

		transaction.setReferenceType(rs.getString("reference_type"));

		int referenceId = rs.getInt("reference_id");

		if (!rs.wasNull()) {
			transaction.setReferenceId(referenceId);
		}

		transaction.setRemarks(rs.getString("remarks"));

		transaction.setCreatedBy(rs.getInt("created_by"));

		transaction.setCreatedAt(rs.getTimestamp("created_at"));

		transaction.setBatchNumber(rs.getString("batch_number"));

		transaction.setProductName(rs.getString("product_name"));

		transaction.setProductCode(rs.getString("product_code"));

		transaction.setCreatedByName(rs.getString("created_by_name"));

		return transaction;
	}

	public int addTransaction(Connection connection, int batchId, String transactionType, int quantity,
			String referenceType, int referenceId, String remarks, int createdBy) {

		int transactionId = 0;

		String sql = """
				INSERT INTO stock_transactions
				(
				    batch_id,
				    transaction_type,
				    quantity,
				    reference_type,
				    reference_id,
				    remarks,
				    created_by
				)
				VALUES (?, ?, ?, ?, ?, ?, ?)
				""";

		try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

			statement.setInt(1, batchId);
			statement.setString(2, transactionType);
			statement.setInt(3, quantity);
			statement.setString(4, referenceType);
			statement.setInt(5, referenceId);
			statement.setString(6, remarks);
			statement.setInt(7, createdBy);

			statement.executeUpdate();

			try (ResultSet resultSet = statement.getGeneratedKeys()) {

				if (resultSet.next()) {
					transactionId = resultSet.getInt(1);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return transactionId;
	}
	
	
}