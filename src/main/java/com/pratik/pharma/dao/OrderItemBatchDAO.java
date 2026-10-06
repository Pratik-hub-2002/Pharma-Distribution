package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.OrderItemBatch;
import com.pratik.pharma.util.DBConnection;

public class OrderItemBatchDAO {

	// =========================================================
	// ADD ORDER ITEM BATCH
	// =========================================================

	public int addOrderItemBatch(OrderItemBatch allocation) {

		int generatedId = 0;

		String sql = """
				INSERT INTO order_item_batches
				(
				    order_item_id,
				    batch_id,
				    allocated_quantity,
				    free_quantity
				)
				VALUES (?, ?, ?, ?)
				""";

		try (Connection connection = DBConnection.getConnection();

				PreparedStatement statement = connection.prepareStatement(sql,
						PreparedStatement.RETURN_GENERATED_KEYS)) {

			statement.setInt(1, allocation.getOrderItemId());

			statement.setInt(2, allocation.getBatchId());

			statement.setInt(3, allocation.getAllocatedQuantity());

			statement.setInt(4, allocation.getFreeQuantity());

			statement.executeUpdate();

			try (ResultSet resultSet = statement.getGeneratedKeys()) {

				if (resultSet.next()) {

					generatedId = resultSet.getInt(1);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return generatedId;
	}

	public int addOrderItemBatch(Connection connection, OrderItemBatch allocation) {

		int generatedId = 0;

		String sql = """
				INSERT INTO order_item_batches
				(
				    order_item_id,
				    batch_id,
				    allocated_quantity,
				    free_quantity
				)
				VALUES (?, ?, ?, ?)
				""";

		try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

			statement.setInt(1, allocation.getOrderItemId());

			statement.setInt(2, allocation.getBatchId());

			statement.setInt(3, allocation.getAllocatedQuantity());

			statement.setInt(4, allocation.getFreeQuantity());

			statement.executeUpdate();

			try (ResultSet resultSet = statement.getGeneratedKeys()) {

				if (resultSet.next()) {

					generatedId = resultSet.getInt(1);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return generatedId;
	}

	// =========================================================
	// ADD ORDER ITEM BATCH
	// USING EXISTING DATABASE CONNECTION
	// =========================================================

	// =========================================================
	// GET ALLOCATIONS BY ORDER ITEM
	// =========================================================

	public List<OrderItemBatch> getByOrderItemId(int orderItemId) {

		List<OrderItemBatch> allocations = new ArrayList<>();

		String sql = """
				SELECT
				    order_item_batch_id,
				    order_item_id,
				    batch_id,
				    allocated_quantity,
				    free_quantity,
				    created_at
				FROM order_item_batches
				WHERE order_item_id = ?
				ORDER BY order_item_batch_id
				""";

		try (Connection connection = DBConnection.getConnection();

				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, orderItemId);

			try (ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {

					OrderItemBatch allocation = new OrderItemBatch();

					allocation.setOrderItemBatchId(resultSet.getInt("order_item_batch_id"));

					allocation.setOrderItemId(resultSet.getInt("order_item_id"));

					allocation.setBatchId(resultSet.getInt("batch_id"));

					allocation.setAllocatedQuantity(resultSet.getInt("allocated_quantity"));

					allocation.setFreeQuantity(resultSet.getInt("free_quantity"));

					allocation.setCreatedAt(resultSet.getTimestamp("created_at"));

					allocations.add(allocation);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return allocations;
	}
}