package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Batch;
import com.pratik.pharma.util.DBConnection;

public class BatchDAO {

	// =========================================================
	// GET ALL BATCHES
	// =========================================================
	public List<Batch> getAllBatches() {

		List<Batch> batches = new ArrayList<>();

		String sql = """
				SELECT
				    b.batch_id,
				    b.product_id,
				    b.batch_number,
				    b.manufacturing_date,
				    b.expiry_date,
				    b.purchase_price,
				    b.status,
				    b.created_at,
				    p.product_name,
				    p.product_code,
				    COALESCE(i.quantity, 0) AS quantity,
				    COALESCE(i.reserved_quantity, 0) AS reserved_quantity,
				    COALESCE(i.damaged_quantity, 0) AS damaged_quantity,
				    (
				        COALESCE(i.quantity, 0)
				        - COALESCE(i.reserved_quantity, 0)
				        - COALESCE(i.damaged_quantity, 0)
				    ) AS available_quantity
				FROM batches b
				INNER JOIN products p
				    ON b.product_id = p.product_id
				LEFT JOIN inventory i
				    ON b.batch_id = i.batch_id
				ORDER BY b.expiry_date ASC
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				batches.add(mapBatch(resultSet));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return batches;
	}

	// =========================================================
	// GET BATCH BY ID
	// =========================================================
	public Batch getBatchById(int batchId) {

		String sql = """
				SELECT
				    b.batch_id,
				    b.product_id,
				    b.batch_number,
				    b.manufacturing_date,
				    b.expiry_date,
				    b.purchase_price,
				    b.status,
				    b.created_at,
				    p.product_name,
				    p.product_code,
				    COALESCE(i.quantity, 0) AS quantity,
				    COALESCE(i.reserved_quantity, 0) AS reserved_quantity,
				    COALESCE(i.damaged_quantity, 0) AS damaged_quantity,
				    (
				        COALESCE(i.quantity, 0)
				        - COALESCE(i.reserved_quantity, 0)
				        - COALESCE(i.damaged_quantity, 0)
				    ) AS available_quantity
				FROM batches b
				INNER JOIN products p
				    ON b.product_id = p.product_id
				LEFT JOIN inventory i
				    ON b.batch_id = i.batch_id
				WHERE b.batch_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setInt(1, batchId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				if (resultSet.next()) {
					return mapBatch(resultSet);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// =========================================================
	// GET BATCHES FOR A PRODUCT USING FEFO
	// FEFO = First Expiry First Out
	// =========================================================
	public List<Batch> getAvailableBatchesByProductFEFO(int productId) {

		List<Batch> batches = new ArrayList<>();

		String sql = """
				SELECT
				    b.batch_id,
				    b.product_id,
				    b.batch_number,
				    b.manufacturing_date,
				    b.expiry_date,
				    b.purchase_price,
				    b.status,
				    b.created_at,
				    p.product_name,
				    p.product_code,
				    COALESCE(i.quantity, 0) AS quantity,
				    COALESCE(i.reserved_quantity, 0) AS reserved_quantity,
				    COALESCE(i.damaged_quantity, 0) AS damaged_quantity,
				    (
				        COALESCE(i.quantity, 0)
				        - COALESCE(i.reserved_quantity, 0)
				        - COALESCE(i.damaged_quantity, 0)
				    ) AS available_quantity
				FROM batches b
				INNER JOIN products p
				    ON b.product_id = p.product_id
				INNER JOIN inventory i
				    ON b.batch_id = i.batch_id
				WHERE b.product_id = ?
				  AND b.status = 'ACTIVE'
				  AND b.expiry_date >= CURDATE()
				  AND (
				      i.quantity
				      - i.reserved_quantity
				      - i.damaged_quantity
				  ) > 0
				ORDER BY b.expiry_date ASC, b.batch_id ASC
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setInt(1, productId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					batches.add(mapBatch(resultSet));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return batches;
	}

	// =========================================================
	// ADD BATCH
	// =========================================================
	public boolean addBatch(Batch batch) {

		String sql = """
				INSERT INTO batches
				(
				    product_id,
				    batch_number,
				    manufacturing_date,
				    expiry_date,
				    purchase_price,
				    status
				)
				VALUES (?, ?, ?, ?, ?, ?)
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setInt(1, batch.getProductId());
			preparedStatement.setString(2, batch.getBatchNumber());
			preparedStatement.setDate(3, batch.getManufacturingDate());
			preparedStatement.setDate(4, batch.getExpiryDate());
			preparedStatement.setBigDecimal(5, batch.getPurchasePrice());
			preparedStatement.setString(6, batch.getStatus());

			return preparedStatement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// UPDATE BATCH
	// =========================================================
	public boolean updateBatch(Batch batch) {

		String sql = """
				UPDATE batches
				SET
				    product_id = ?,
				    batch_number = ?,
				    manufacturing_date = ?,
				    expiry_date = ?,
				    purchase_price = ?,
				    status = ?
				WHERE batch_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setInt(1, batch.getProductId());
			preparedStatement.setString(2, batch.getBatchNumber());
			preparedStatement.setDate(3, batch.getManufacturingDate());
			preparedStatement.setDate(4, batch.getExpiryDate());
			preparedStatement.setBigDecimal(5, batch.getPurchasePrice());
			preparedStatement.setString(6, batch.getStatus());
			preparedStatement.setInt(7, batch.getBatchId());

			return preparedStatement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// UPDATE BATCH STATUS
	// =========================================================
	public boolean updateBatchStatus(int batchId, String status) {

		String sql = """
				UPDATE batches
				SET status = ?
				WHERE batch_id = ?
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, status);
			preparedStatement.setInt(2, batchId);

			return preparedStatement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// MAP RESULTSET TO BATCH OBJECT
	// =========================================================
	private Batch mapBatch(ResultSet resultSet) throws SQLException {

		Batch batch = new Batch();

		batch.setBatchId(resultSet.getInt("batch_id"));
		batch.setProductId(resultSet.getInt("product_id"));
		batch.setBatchNumber(resultSet.getString("batch_number"));

		Date manufacturingDate = resultSet.getDate("manufacturing_date");

		Date expiryDate = resultSet.getDate("expiry_date");

		batch.setManufacturingDate(manufacturingDate);
		batch.setExpiryDate(expiryDate);

		batch.setPurchasePrice(resultSet.getBigDecimal("purchase_price"));

		batch.setStatus(resultSet.getString("status"));

		batch.setCreatedAt(resultSet.getTimestamp("created_at"));

		// Product information
		batch.setProductName(resultSet.getString("product_name"));

		batch.setProductCode(resultSet.getString("product_code"));

		// Inventory information
		batch.setAvailableQuantity(resultSet.getInt("available_quantity"));

		return batch;
	}
}