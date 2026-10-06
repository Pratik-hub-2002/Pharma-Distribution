package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Manufacturer;
import com.pratik.pharma.util.DBConnection;

public class ManufacturerDAO {

	public List<Manufacturer> getAllManufacturers() {

		List<Manufacturer> manufacturers = new ArrayList<>();

		String sql = "SELECT manufacturer_id, manufacturer_name " + "FROM manufacturers "
				+ "ORDER BY manufacturer_name";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				Manufacturer manufacturer = new Manufacturer();

				manufacturer.setManufacturerId(rs.getInt("manufacturer_id"));

				manufacturer.setManufacturerName(rs.getString("manufacturer_name"));

				manufacturers.add(manufacturer);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return manufacturers;
	}

	public Manufacturer getManufacturerById(int manufacturerId) {

		String sql = "SELECT manufacturer_id, manufacturer_name " + "FROM manufacturers " + "WHERE manufacturer_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, manufacturerId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					Manufacturer manufacturer = new Manufacturer();

					manufacturer.setManufacturerId(rs.getInt("manufacturer_id"));

					manufacturer.setManufacturerName(rs.getString("manufacturer_name"));

					return manufacturer;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}