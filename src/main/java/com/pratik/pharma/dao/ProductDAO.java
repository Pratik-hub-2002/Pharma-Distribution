package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Product;
import com.pratik.pharma.util.DBConnection;

public class ProductDAO {

	// =========================
	// GET ALL PRODUCTS
	// =========================

	public List<Product> getAllProducts() {

		List<Product> products = new ArrayList<>();

		String sql = "SELECT p.product_id, " + "p.product_name, " + "p.product_code, " + "p.category_id, "
				+ "p.manufacturer_id, " + "p.description, " + "p.dosage_form, " + "p.strength, " + "p.unit, "
				+ "p.hsn_code, " + "p.gst_rate, " + "p.mrp, " + "p.selling_price, " + "p.cold_chain_required, "
				+ "p.status, " + "p.created_at, " + "c.category_name, " + "m.manufacturer_name " + "FROM products p "
				+ "JOIN categories c " + "ON p.category_id = c.category_id " + "JOIN manufacturers m "
				+ "ON p.manufacturer_id = m.manufacturer_id " + "ORDER BY p.product_id";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				Product product = mapProduct(rs);

				products.add(product);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return products;
	}

	// =========================
	// GET PRODUCT BY ID
	// =========================

	public Product getProductById(int productId) {

		String sql = "SELECT p.product_id, " + "p.product_name, " + "p.product_code, " + "p.category_id, "
				+ "p.manufacturer_id, " + "p.description, " + "p.dosage_form, " + "p.strength, " + "p.unit, "
				+ "p.hsn_code, " + "p.gst_rate, " + "p.mrp, " + "p.selling_price, " + "p.cold_chain_required, "
				+ "p.status, " + "p.created_at, " + "c.category_name, " + "m.manufacturer_name " + "FROM products p "
				+ "JOIN categories c " + "ON p.category_id = c.category_id " + "JOIN manufacturers m "
				+ "ON p.manufacturer_id = m.manufacturer_id " + "WHERE p.product_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, productId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					return mapProduct(rs);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// =========================
	// ADD PRODUCT
	// =========================

	public boolean addProduct(Product product) {

		String sql = "INSERT INTO products (" + "product_name, " + "product_code, " + "category_id, "
				+ "manufacturer_id, " + "description, " + "dosage_form, " + "strength, " + "unit, " + "hsn_code, "
				+ "gst_rate, " + "mrp, " + "selling_price, " + "cold_chain_required, " + "status"
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, product.getProductName());
			ps.setString(2, product.getProductCode());
			ps.setInt(3, product.getCategoryId());
			ps.setInt(4, product.getManufacturerId());
			ps.setString(5, product.getDescription());
			ps.setString(6, product.getDosageForm());
			ps.setString(7, product.getStrength());
			ps.setString(8, product.getUnit());
			ps.setString(9, product.getHsnCode());
			ps.setBigDecimal(10, product.getGstRate());
			ps.setBigDecimal(11, product.getMrp());
			ps.setBigDecimal(12, product.getSellingPrice());
			ps.setBoolean(13, product.isColdChainRequired());
			ps.setString(14, product.getStatus());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// =========================
	// UPDATE PRODUCT
	// =========================

	public boolean updateProduct(Product product) {

		String sql = "UPDATE products SET " + "product_name = ?, " + "product_code = ?, " + "category_id = ?, "
				+ "manufacturer_id = ?, " + "description = ?, " + "dosage_form = ?, " + "strength = ?, " + "unit = ?, "
				+ "hsn_code = ?, " + "gst_rate = ?, " + "mrp = ?, " + "selling_price = ?, "
				+ "cold_chain_required = ?, " + "status = ? " + "WHERE product_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, product.getProductName());
			ps.setString(2, product.getProductCode());
			ps.setInt(3, product.getCategoryId());
			ps.setInt(4, product.getManufacturerId());
			ps.setString(5, product.getDescription());
			ps.setString(6, product.getDosageForm());
			ps.setString(7, product.getStrength());
			ps.setString(8, product.getUnit());
			ps.setString(9, product.getHsnCode());
			ps.setBigDecimal(10, product.getGstRate());
			ps.setBigDecimal(11, product.getMrp());
			ps.setBigDecimal(12, product.getSellingPrice());
			ps.setBoolean(13, product.isColdChainRequired());
			ps.setString(14, product.getStatus());
			ps.setInt(15, product.getProductId());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// =========================
	// ACTIVATE / DEACTIVATE
	// =========================

	public boolean updateProductStatus(int productId, String status) {

		String sql = "UPDATE products " + "SET status = ? " + "WHERE product_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, status);
			ps.setInt(2, productId);

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// =========================
	// MAP RESULTSET TO PRODUCT
	// =========================

	private Product mapProduct(ResultSet rs) throws Exception {

		Product product = new Product();

		product.setProductId(rs.getInt("product_id"));

		product.setProductName(rs.getString("product_name"));

		product.setProductCode(rs.getString("product_code"));

		product.setCategoryId(rs.getInt("category_id"));

		product.setManufacturerId(rs.getInt("manufacturer_id"));

		product.setDescription(rs.getString("description"));

		product.setDosageForm(rs.getString("dosage_form"));

		product.setStrength(rs.getString("strength"));

		product.setUnit(rs.getString("unit"));

		product.setHsnCode(rs.getString("hsn_code"));

		product.setGstRate(rs.getBigDecimal("gst_rate"));

		product.setMrp(rs.getBigDecimal("mrp"));

		product.setSellingPrice(rs.getBigDecimal("selling_price"));

		product.setColdChainRequired(rs.getBoolean("cold_chain_required"));

		product.setStatus(rs.getString("status"));

		product.setCreatedAt(rs.getTimestamp("created_at"));

		product.setCategoryName(rs.getString("category_name"));

		product.setManufacturerName(rs.getString("manufacturer_name"));

		return product;
	}
}