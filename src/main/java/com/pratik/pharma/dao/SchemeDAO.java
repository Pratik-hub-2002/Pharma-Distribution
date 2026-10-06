package com.pratik.pharma.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Scheme;
import com.pratik.pharma.util.DBConnection;

public class SchemeDAO {

	// =========================================================
	// GET ALL SCHEMES
	// =========================================================

	public List<Scheme> getAllSchemes() {

		List<Scheme> schemes = new ArrayList<>();

		String sql = "SELECT s.scheme_id, " + "       s.scheme_name, " + "       s.product_id, "
				+ "       p.product_name, " + "       s.scheme_type, " + "       s.minimum_quantity, "
				+ "       s.free_quantity, " + "       s.discount_percentage, " + "       s.start_date, "
				+ "       s.end_date, " + "       s.status, " + "       s.created_by " + "FROM schemes s "
				+ "JOIN products p " + "ON s.product_id = p.product_id " + "ORDER BY s.scheme_id DESC";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				schemes.add(mapScheme(rs));

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return schemes;
	}

	// =========================================================
	// GET SCHEME BY ID
	// =========================================================

	public Scheme getSchemeById(int schemeId) {

		String sql = "SELECT s.scheme_id, " + "       s.scheme_name, " + "       s.product_id, "
				+ "       p.product_name, " + "       s.scheme_type, " + "       s.minimum_quantity, "
				+ "       s.free_quantity, " + "       s.discount_percentage, " + "       s.start_date, "
				+ "       s.end_date, " + "       s.status, " + "       s.created_by " + "FROM schemes s "
				+ "JOIN products p " + "ON s.product_id = p.product_id " + "WHERE s.scheme_id = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setInt(1, schemeId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					return mapScheme(rs);

				}
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;
	}

	// =========================================================
	// ADD SCHEME
	// =========================================================

	public boolean addScheme(Scheme scheme) {

		String sql = "INSERT INTO schemes " + "(scheme_name, product_id, scheme_type, "
				+ " minimum_quantity, free_quantity, discount_percentage, "
				+ " start_date, end_date, status, created_by) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setString(1, scheme.getSchemeName());

			ps.setInt(2, scheme.getProductId());

			ps.setString(3, scheme.getSchemeType());

			ps.setInt(4, scheme.getMinimumQuantity());

			ps.setInt(5, scheme.getFreeQuantity());

			ps.setBigDecimal(6, scheme.getDiscountPercentage());

			ps.setDate(7, scheme.getStartDate());

			ps.setDate(8, scheme.getEndDate());

			ps.setString(9, scheme.getStatus());

			ps.setInt(10, scheme.getCreatedBy());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return false;
	}

	// =========================================================
	// UPDATE SCHEME
	// =========================================================

	public boolean updateScheme(Scheme scheme) {

		String sql = "UPDATE schemes SET " + "scheme_name = ?, " + "product_id = ?, " + "scheme_type = ?, "
				+ "minimum_quantity = ?, " + "free_quantity = ?, " + "discount_percentage = ?, " + "start_date = ?, "
				+ "end_date = ? " + "WHERE scheme_id = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setString(1, scheme.getSchemeName());

			ps.setInt(2, scheme.getProductId());

			ps.setString(3, scheme.getSchemeType());

			ps.setInt(4, scheme.getMinimumQuantity());

			ps.setInt(5, scheme.getFreeQuantity());

			ps.setBigDecimal(6, scheme.getDiscountPercentage());

			ps.setDate(7, scheme.getStartDate());

			ps.setDate(8, scheme.getEndDate());

			ps.setInt(9, scheme.getSchemeId());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return false;
	}

	// =========================================================
	// UPDATE STATUS
	// =========================================================

	public boolean updateStatus(int schemeId, String status) {

		String sql = "UPDATE schemes " + "SET status = ? " + "WHERE scheme_id = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setString(1, status);

			ps.setInt(2, schemeId);

			return ps.executeUpdate() > 0;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return false;
	}

	// =========================================================
	// GET ACTIVE SCHEME FOR PRODUCT
	// =========================================================
	// IMPORTANT:
	// OrderServlet already uses this method.
	// =========================================================

	public Scheme getActiveSchemeByProduct(int productId) {

		String sql = "SELECT scheme_id, " + "       scheme_name, " + "       product_id, " + "       scheme_type, "
				+ "       minimum_quantity, " + "       free_quantity, " + "       discount_percentage, "
				+ "       start_date, " + "       end_date, " + "       status, " + "       created_by "
				+ "FROM schemes " + "WHERE product_id = ? " + "AND status = 'ACTIVE' "
				+ "AND CURDATE() BETWEEN start_date AND end_date " + "ORDER BY scheme_id " + "LIMIT 1";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setInt(1, productId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					Scheme scheme = new Scheme();

					scheme.setSchemeId(rs.getInt("scheme_id"));

					scheme.setSchemeName(rs.getString("scheme_name"));

					scheme.setProductId(rs.getInt("product_id"));

					scheme.setSchemeType(rs.getString("scheme_type"));

					scheme.setMinimumQuantity(rs.getInt("minimum_quantity"));

					scheme.setFreeQuantity(rs.getInt("free_quantity"));

					scheme.setDiscountPercentage(rs.getBigDecimal("discount_percentage"));

					scheme.setStartDate(rs.getDate("start_date"));

					scheme.setEndDate(rs.getDate("end_date"));

					scheme.setStatus(rs.getString("status"));

					scheme.setCreatedBy(rs.getInt("created_by"));

					return scheme;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;
	}

	// =========================================================
	// MAP RESULT
	// =========================================================

	private Scheme mapScheme(ResultSet rs) throws Exception {

		Scheme scheme = new Scheme();

		scheme.setSchemeId(rs.getInt("scheme_id"));

		scheme.setSchemeName(rs.getString("scheme_name"));

		scheme.setProductId(rs.getInt("product_id"));

		scheme.setProductName(rs.getString("product_name"));

		scheme.setSchemeType(rs.getString("scheme_type"));

		scheme.setMinimumQuantity(rs.getInt("minimum_quantity"));

		scheme.setFreeQuantity(rs.getInt("free_quantity"));

		scheme.setDiscountPercentage(rs.getBigDecimal("discount_percentage"));

		scheme.setStartDate(rs.getDate("start_date"));

		scheme.setEndDate(rs.getDate("end_date"));

		scheme.setStatus(rs.getString("status"));

		scheme.setCreatedBy(rs.getInt("created_by"));

		return scheme;
	}
}