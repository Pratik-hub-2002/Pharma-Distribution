package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Manufacturer;
import com.pratik.pharma.util.DBConnection;

public class ManufacturerDAO {

	// =========================================================
	// GET ALL MANUFACTURERS
	// =========================================================

	public List<Manufacturer> getAllManufacturers() {

		List<Manufacturer> manufacturers = new ArrayList<>();

		String sql = "SELECT manufacturer_id, manufacturer_name, contact_person, "
				+ "email, phone, address, city, state, gst_number, status, created_at " + "FROM manufacturers "
				+ "ORDER BY manufacturer_name";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				manufacturers.add(mapManufacturer(rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return manufacturers;
	}

	// =========================================================
	// GET ACTIVE MANUFACTURERS
	// Used by Product dropdown
	// =========================================================

	public List<Manufacturer> getActiveManufacturers() {

		List<Manufacturer> manufacturers = new ArrayList<>();

		String sql = "SELECT manufacturer_id, manufacturer_name, contact_person, "
				+ "email, phone, address, city, state, gst_number, status, created_at " + "FROM manufacturers "
				+ "WHERE status = 'ACTIVE' " + "ORDER BY manufacturer_name";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				manufacturers.add(mapManufacturer(rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return manufacturers;
	}

	// =========================================================
	// GET BY ID
	// =========================================================

	public Manufacturer getManufacturerById(int manufacturerId) {

		String sql = "SELECT manufacturer_id, manufacturer_name, contact_person, "
				+ "email, phone, address, city, state, gst_number, status, created_at " + "FROM manufacturers "
				+ "WHERE manufacturer_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, manufacturerId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					return mapManufacturer(rs);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// =========================================================
	// ADD MANUFACTURER
	// =========================================================

	public boolean addManufacturer(Manufacturer manufacturer) {

		String sql = "INSERT INTO manufacturers " + "(manufacturer_name, contact_person, email, phone, address, "
				+ "city, state, gst_number, status) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, manufacturer.getManufacturerName());
			ps.setString(2, manufacturer.getContactPerson());
			ps.setString(3, manufacturer.getEmail());
			ps.setString(4, manufacturer.getPhone());
			ps.setString(5, manufacturer.getAddress());
			ps.setString(6, manufacturer.getCity());
			ps.setString(7, manufacturer.getState());
			ps.setString(8, manufacturer.getGstNumber());
			ps.setString(9, manufacturer.getStatus());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// UPDATE MANUFACTURER
	// =========================================================

	public boolean updateManufacturer(Manufacturer manufacturer) {

		String sql = "UPDATE manufacturers SET " + "manufacturer_name = ?, " + "contact_person = ?, " + "email = ?, "
				+ "phone = ?, " + "address = ?, " + "city = ?, " + "state = ?, " + "gst_number = ?, " + "status = ? "
				+ "WHERE manufacturer_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, manufacturer.getManufacturerName());
			ps.setString(2, manufacturer.getContactPerson());
			ps.setString(3, manufacturer.getEmail());
			ps.setString(4, manufacturer.getPhone());
			ps.setString(5, manufacturer.getAddress());
			ps.setString(6, manufacturer.getCity());
			ps.setString(7, manufacturer.getState());
			ps.setString(8, manufacturer.getGstNumber());
			ps.setString(9, manufacturer.getStatus());
			ps.setInt(10, manufacturer.getManufacturerId());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// CHANGE STATUS
	// =========================================================

	public boolean updateStatus(int manufacturerId, String status) {

		String sql = "UPDATE manufacturers " + "SET status = ? " + "WHERE manufacturer_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, status);
			ps.setInt(2, manufacturerId);

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// =========================================================
	// MAP RESULTSET
	// =========================================================

	private Manufacturer mapManufacturer(ResultSet rs) throws Exception {

		Manufacturer manufacturer = new Manufacturer();

		manufacturer.setManufacturerId(rs.getInt("manufacturer_id"));

		manufacturer.setManufacturerName(rs.getString("manufacturer_name"));

		manufacturer.setContactPerson(rs.getString("contact_person"));

		manufacturer.setEmail(rs.getString("email"));

		manufacturer.setPhone(rs.getString("phone"));

		manufacturer.setAddress(rs.getString("address"));

		manufacturer.setCity(rs.getString("city"));

		manufacturer.setState(rs.getString("state"));

		manufacturer.setGstNumber(rs.getString("gst_number"));

		manufacturer.setStatus(rs.getString("status"));

		manufacturer.setCreatedAt(rs.getTimestamp("created_at"));

		return manufacturer;
	}
}