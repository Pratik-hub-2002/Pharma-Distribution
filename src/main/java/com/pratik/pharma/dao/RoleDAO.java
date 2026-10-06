package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Role;
import com.pratik.pharma.util.DBConnection;

public class RoleDAO {

	// =========================================================
	// GET ALL ROLES
	// =========================================================

	public List<Role> getAllRoles() {

		List<Role> roles = new ArrayList<>();

		String sql = "SELECT " + "role_id, " + "role_name, " + "description " + "FROM roles " + "ORDER BY role_id";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				Role role = new Role();

				role.setRoleId(rs.getInt("role_id"));
				role.setRoleName(rs.getString("role_name"));
				role.setDescription(rs.getString("description"));

				roles.add(role);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return roles;
	}

	// =========================================================
	// GET ROLE BY ID
	// =========================================================

	public Role getRoleById(int roleId) {

		String sql = "SELECT " + "role_id, " + "role_name, " + "description " + "FROM roles " + "WHERE role_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, roleId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					Role role = new Role();

					role.setRoleId(rs.getInt("role_id"));
					role.setRoleName(rs.getString("role_name"));
					role.setDescription(rs.getString("description"));

					return role;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}