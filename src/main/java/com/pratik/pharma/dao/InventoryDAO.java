package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Inventory;
import com.pratik.pharma.util.DBConnection;

public class InventoryDAO {

	// =========================================================
	// GET ALL INVENTORY
	// =========================================================

	public List<Inventory> getAllInventory() {

		List<Inventory> inventoryList = new ArrayList<>();

		String sql = """
				SELECT i.inventory_id,
				       i.batch_id,
				       i.quantity,
				       i.reserved_quantity,
				       i.damaged_quantity,
				       i.reorder_level,
				       i.updated_at,
				       b.batch_number,
				       b.expiry_date,
				       p.product_name,
				       p.product_code
				FROM inventory i
				INNER JOIN batches b
				    ON i.batch_id = b.batch_id
				INNER JOIN products p
				    ON b.product_id = p.product_id
				ORDER BY b.expiry_date ASC
				""";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				Inventory inventory = new Inventory();

				inventory.setInventoryId(rs.getInt("inventory_id"));

				inventory.setBatchId(rs.getInt("batch_id"));

				inventory.setQuantity(rs.getInt("quantity"));

				inventory.setReservedQuantity(rs.getInt("reserved_quantity"));

				inventory.setDamagedQuantity(rs.getInt("damaged_quantity"));

				inventory.setReorderLevel(rs.getInt("reorder_level"));

				inventory.setUpdatedAt(rs.getTimestamp("updated_at"));

				inventory.setBatchNumber(rs.getString("batch_number"));

				inventory.setExpiryDate(rs.getDate("expiry_date"));

				inventory.setProductName(rs.getString("product_name"));

				inventory.setProductCode(rs.getString("product_code"));

				inventoryList.add(inventory);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return inventoryList;
	}

	// =========================================================
	// GET INVENTORY BY ID
	// =========================================================

	public Inventory getInventoryById(int inventoryId) {

		Inventory inventory = null;

		String sql = """
				SELECT i.inventory_id,
				       i.batch_id,
				       i.quantity,
				       i.reserved_quantity,
				       i.damaged_quantity,
				       i.reorder_level,
				       i.updated_at,
				       b.batch_number,
				       b.expiry_date,
				       p.product_name,
				       p.product_code
				FROM inventory i
				INNER JOIN batches b
				    ON i.batch_id = b.batch_id
				INNER JOIN products p
				    ON b.product_id = p.product_id
				WHERE i.inventory_id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, inventoryId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					inventory = new Inventory();

					inventory.setInventoryId(rs.getInt("inventory_id"));

					inventory.setBatchId(rs.getInt("batch_id"));

					inventory.setQuantity(rs.getInt("quantity"));

					inventory.setReservedQuantity(rs.getInt("reserved_quantity"));

					inventory.setDamagedQuantity(rs.getInt("damaged_quantity"));

					inventory.setReorderLevel(rs.getInt("reorder_level"));

					inventory.setUpdatedAt(rs.getTimestamp("updated_at"));

					inventory.setBatchNumber(rs.getString("batch_number"));

					inventory.setExpiryDate(rs.getDate("expiry_date"));

					inventory.setProductName(rs.getString("product_name"));

					inventory.setProductCode(rs.getString("product_code"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return inventory;
	}

	// =========================================================
	// ADD INVENTORY
	// =========================================================

	public boolean addInventory(Inventory inventory) {

		String sql = """
				INSERT INTO inventory
				(
				    batch_id,
				    quantity,
				    reserved_quantity,
				    damaged_quantity,
				    reorder_level
				)
				VALUES (?, ?, ?, ?, ?)
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, inventory.getBatchId());
			ps.setInt(2, inventory.getQuantity());
			ps.setInt(3, inventory.getReservedQuantity());
			ps.setInt(4, inventory.getDamagedQuantity());
			ps.setInt(5, inventory.getReorderLevel());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// UPDATE INVENTORY
	// =========================================================

	public boolean updateInventory(Inventory inventory) {

		String sql = """
				UPDATE inventory
				SET batch_id = ?,
				    quantity = ?,
				    reserved_quantity = ?,
				    damaged_quantity = ?,
				    reorder_level = ?
				WHERE inventory_id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, inventory.getBatchId());
			ps.setInt(2, inventory.getQuantity());
			ps.setInt(3, inventory.getReservedQuantity());
			ps.setInt(4, inventory.getDamagedQuantity());
			ps.setInt(5, inventory.getReorderLevel());
			ps.setInt(6, inventory.getInventoryId());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// REDUCE AVAILABLE QUANTITY
	// USED FOR STOCK OUT
	// =========================================================

	public boolean reduceAvailableQuantity(Connection connection, int batchId, int quantity) {

		String sql = """
				UPDATE inventory
				SET quantity = quantity - ?
				WHERE batch_id = ?
				  AND (
				        quantity
				        - reserved_quantity
				        - damaged_quantity
				      ) >= ?
				""";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, quantity);
			statement.setInt(2, batchId);
			statement.setInt(3, quantity);

			int rowsUpdated = statement.executeUpdate();

			return rowsUpdated == 1;

		} catch (Exception e) {

			e.printStackTrace();

			return false;
		}
	}

	// =========================================================
	// INCREASE QUANTITY
	// USED FOR STOCK IN
	// =========================================================

	public boolean increaseQuantity(Connection connection, int batchId, int quantity) {

		String sql = """
				UPDATE inventory
				SET quantity = quantity + ?
				WHERE batch_id = ?
				""";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, quantity);
			statement.setInt(2, batchId);

			int rowsUpdated = statement.executeUpdate();

			return rowsUpdated == 1;

		} catch (Exception e) {

			e.printStackTrace();

			return false;
		}
	}

	// =========================================================
	// LOW STOCK INVENTORY
	// =========================================================

	public List<Inventory> getLowStockInventory() {

		List<Inventory> inventoryList = new ArrayList<>();

		String sql = """
				SELECT i.inventory_id,
				       i.batch_id,
				       i.quantity,
				       i.reserved_quantity,
				       i.damaged_quantity,
				       i.reorder_level,
				       i.updated_at,
				       b.batch_number,
				       b.expiry_date,
				       p.product_name,
				       p.product_code
				FROM inventory i
				INNER JOIN batches b
				    ON i.batch_id = b.batch_id
				INNER JOIN products p
				    ON b.product_id = p.product_id
				WHERE (
				    i.quantity
				    - i.reserved_quantity
				    - i.damaged_quantity
				) <= i.reorder_level
				ORDER BY (
				    i.quantity
				    - i.reserved_quantity
				    - i.damaged_quantity
				) ASC
				""";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				Inventory inventory = new Inventory();

				inventory.setInventoryId(rs.getInt("inventory_id"));

				inventory.setBatchId(rs.getInt("batch_id"));

				inventory.setQuantity(rs.getInt("quantity"));

				inventory.setReservedQuantity(rs.getInt("reserved_quantity"));

				inventory.setDamagedQuantity(rs.getInt("damaged_quantity"));

				inventory.setReorderLevel(rs.getInt("reorder_level"));

				inventory.setUpdatedAt(rs.getTimestamp("updated_at"));

				inventory.setBatchNumber(rs.getString("batch_number"));

				inventory.setExpiryDate(rs.getDate("expiry_date"));

				inventory.setProductName(rs.getString("product_name"));

				inventory.setProductCode(rs.getString("product_code"));

				inventoryList.add(inventory);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return inventoryList;
	}

	// =========================================================
	// NEAR EXPIRY INVENTORY
	// WITHIN 90 DAYS
	// =========================================================

	public List<Inventory> getNearExpiryInventory() {

		List<Inventory> inventoryList = new ArrayList<>();

		String sql = """
				SELECT i.inventory_id,
				       i.batch_id,
				       i.quantity,
				       i.reserved_quantity,
				       i.damaged_quantity,
				       i.reorder_level,
				       i.updated_at,
				       b.batch_number,
				       b.expiry_date,
				       p.product_name,
				       p.product_code
				FROM inventory i
				INNER JOIN batches b
				    ON i.batch_id = b.batch_id
				INNER JOIN products p
				    ON b.product_id = p.product_id
				WHERE b.expiry_date >= CURDATE()
				  AND b.expiry_date <=
				      DATE_ADD(CURDATE(), INTERVAL 90 DAY)
				ORDER BY b.expiry_date ASC
				""";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				Inventory inventory = new Inventory();

				inventory.setInventoryId(rs.getInt("inventory_id"));

				inventory.setBatchId(rs.getInt("batch_id"));

				inventory.setQuantity(rs.getInt("quantity"));

				inventory.setReservedQuantity(rs.getInt("reserved_quantity"));

				inventory.setDamagedQuantity(rs.getInt("damaged_quantity"));

				inventory.setReorderLevel(rs.getInt("reorder_level"));

				inventory.setUpdatedAt(rs.getTimestamp("updated_at"));

				inventory.setBatchNumber(rs.getString("batch_number"));

				inventory.setExpiryDate(rs.getDate("expiry_date"));

				inventory.setProductName(rs.getString("product_name"));

				inventory.setProductCode(rs.getString("product_code"));

				inventoryList.add(inventory);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return inventoryList;
	}

	// =========================================================
	// EXPIRED INVENTORY
	// =========================================================

	public List<Inventory> getExpiredInventory() {

		List<Inventory> inventoryList = new ArrayList<>();

		String sql = """
				SELECT i.inventory_id,
				       i.batch_id,
				       i.quantity,
				       i.reserved_quantity,
				       i.damaged_quantity,
				       i.reorder_level,
				       i.updated_at,
				       b.batch_number,
				       b.expiry_date,
				       p.product_name,
				       p.product_code
				FROM inventory i
				INNER JOIN batches b
				    ON i.batch_id = b.batch_id
				INNER JOIN products p
				    ON b.product_id = p.product_id
				WHERE b.expiry_date < CURDATE()
				ORDER BY b.expiry_date ASC
				""";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				Inventory inventory = new Inventory();

				inventory.setInventoryId(rs.getInt("inventory_id"));

				inventory.setBatchId(rs.getInt("batch_id"));

				inventory.setQuantity(rs.getInt("quantity"));

				inventory.setReservedQuantity(rs.getInt("reserved_quantity"));

				inventory.setDamagedQuantity(rs.getInt("damaged_quantity"));

				inventory.setReorderLevel(rs.getInt("reorder_level"));

				inventory.setUpdatedAt(rs.getTimestamp("updated_at"));

				inventory.setBatchNumber(rs.getString("batch_number"));

				inventory.setExpiryDate(rs.getDate("expiry_date"));

				inventory.setProductName(rs.getString("product_name"));

				inventory.setProductCode(rs.getString("product_code"));

				inventoryList.add(inventory);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return inventoryList;
	}

	// =========================================================
	// DASHBOARD - TOTAL BATCHES
	// =========================================================

	public int getTotalBatches() {

		String sql = "SELECT COUNT(*) FROM inventory";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	// =========================================================
	// DASHBOARD - TOTAL QUANTITY
	// =========================================================

	public int getTotalQuantity() {

		String sql = "SELECT COALESCE(SUM(quantity), 0) " + "FROM inventory";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	// =========================================================
	// DASHBOARD - TOTAL AVAILABLE QUANTITY
	// =========================================================

	public int getTotalAvailableQuantity() {

		String sql = """
				SELECT COALESCE(
				    SUM(
				        quantity
				        - reserved_quantity
				        - damaged_quantity
				    ),
				    0
				)
				FROM inventory
				""";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	// =========================================================
	// DASHBOARD - TOTAL RESERVED QUANTITY
	// =========================================================

	public int getTotalReservedQuantity() {

		String sql = "SELECT COALESCE(SUM(reserved_quantity), 0) " + "FROM inventory";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	// =========================================================
	// DASHBOARD - TOTAL DAMAGED QUANTITY
	// =========================================================

	public int getTotalDamagedQuantity() {

		String sql = "SELECT COALESCE(SUM(damaged_quantity), 0) " + "FROM inventory";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	// =========================================================
	// DASHBOARD - LOW STOCK COUNT
	// =========================================================

	public int getLowStockCount() {

		String sql = """
				SELECT COUNT(*)
				FROM inventory
				WHERE (
				    quantity
				    - reserved_quantity
				    - damaged_quantity
				) <= reorder_level
				""";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	// =========================================================
	// DASHBOARD - NEAR EXPIRY COUNT
	// =========================================================

	public int getNearExpiryCount() {

		String sql = """
				SELECT COUNT(*)
				FROM inventory i
				INNER JOIN batches b
				    ON i.batch_id = b.batch_id
				WHERE b.expiry_date >= CURDATE()
				  AND b.expiry_date <=
				      DATE_ADD(CURDATE(), INTERVAL 90 DAY)
				""";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	// =========================================================
	// DASHBOARD - EXPIRED COUNT
	// =========================================================

	public int getExpiredCount() {

		String sql = """
				SELECT COUNT(*)
				FROM inventory i
				INNER JOIN batches b
				    ON i.batch_id = b.batch_id
				WHERE b.expiry_date < CURDATE()
				""";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
}