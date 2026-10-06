package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.User;
import com.pratik.pharma.util.DBConnection;

public class UserDAO {

	// =========================================================
	// LOGIN
	// =========================================================

	public User login(String username, String password) {

		String sql = "SELECT " + "u.user_id, " + "u.username, " + "u.password_hash, " + "u.first_name, "
				+ "u.last_name, " + "u.email, " + "u.phone, " + "u.role_id, " + "r.role_name, " + "u.client_id, "
				+ "u.status, " + "u.created_at " + "FROM users u " + "JOIN roles r ON u.role_id = r.role_id "
				+ "WHERE u.username = ? " + "AND u.password_hash = ? " + "AND u.status = 'ACTIVE'";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				if (resultSet.next()) {
					return mapUser(resultSet);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// =========================================================
	// GET USER BY ID
	// =========================================================

	public User getUserById(int userId) {

		String sql = "SELECT " + "u.user_id, " + "u.username, " + "u.password_hash, " + "u.first_name, "
				+ "u.last_name, " + "u.email, " + "u.phone, " + "u.role_id, " + "r.role_name, " + "u.client_id, "
				+ "u.status, " + "u.created_at " + "FROM users u " + "JOIN roles r ON u.role_id = r.role_id "
				+ "WHERE u.user_id = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setInt(1, userId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				if (resultSet.next()) {
					return mapUser(resultSet);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// =========================================================
	// GET ALL USERS
	// =========================================================

	public List<User> getAllUsers() {

		List<User> users = new ArrayList<>();

		String sql = "SELECT " + "u.user_id, " + "u.username, " + "u.password_hash, " + "u.first_name, "
				+ "u.last_name, " + "u.email, " + "u.phone, " + "u.role_id, " + "r.role_name, " + "u.client_id, "
				+ "u.status, " + "u.created_at " + "FROM users u " + "JOIN roles r ON u.role_id = r.role_id "
				+ "ORDER BY u.user_id";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				users.add(mapUser(resultSet));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}

	// =========================================================
	// ADD USER
	// =========================================================

	public boolean addUser(User user) {

		String sql = "INSERT INTO users " + "(username, password_hash, first_name, last_name, "
				+ "email, phone, role_id, client_id, status) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPasswordHash());
			preparedStatement.setString(3, user.getFirstName());
			preparedStatement.setString(4, user.getLastName());
			preparedStatement.setString(5, user.getEmail());
			preparedStatement.setString(6, user.getPhone());
			preparedStatement.setInt(7, user.getRoleId());

			if (user.getClientId() != null) {
				preparedStatement.setInt(8, user.getClientId());
			} else {
				preparedStatement.setNull(8, java.sql.Types.INTEGER);
			}

			preparedStatement.setString(9, user.getStatus());

			int rows = preparedStatement.executeUpdate();

			return rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// =========================================================
	// UPDATE USER
	// =========================================================

	public boolean updateUser(User user) {

		String sql = "UPDATE users SET " + "username = ?, " + "first_name = ?, " + "last_name = ?, " + "email = ?, "
				+ "phone = ?, " + "role_id = ?, " + "client_id = ?, " + "status = ? " + "WHERE user_id = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getFirstName());
			preparedStatement.setString(3, user.getLastName());
			preparedStatement.setString(4, user.getEmail());
			preparedStatement.setString(5, user.getPhone());
			preparedStatement.setInt(6, user.getRoleId());

			if (user.getClientId() != null) {
				preparedStatement.setInt(7, user.getClientId());
			} else {
				preparedStatement.setNull(7, java.sql.Types.INTEGER);
			}

			preparedStatement.setString(8, user.getStatus());
			preparedStatement.setInt(9, user.getUserId());

			preparedStatement.executeUpdate();

			// SQL executed successfully.
			// Even if no values changed, this is not an error.
			return true;

		} catch (SQLException e) {

			e.printStackTrace();
			return false;
		}
	}

	// =========================================================
	// UPDATE USER STATUS
	// =========================================================

	public boolean updateStatus(int userId, String status) {

		String sql = "UPDATE users SET status = ? " + "WHERE user_id = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, status);
			preparedStatement.setInt(2, userId);

			int rows = preparedStatement.executeUpdate();

			return rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// =========================================================
	// MAP RESULTSET TO USER
	// =========================================================

	private User mapUser(ResultSet resultSet) throws SQLException {

		User user = new User();

		user.setUserId(resultSet.getInt("user_id"));
		user.setUsername(resultSet.getString("username"));
		user.setPasswordHash(resultSet.getString("password_hash"));

		user.setFirstName(resultSet.getString("first_name"));
		user.setLastName(resultSet.getString("last_name"));

		user.setEmail(resultSet.getString("email"));
		user.setPhone(resultSet.getString("phone"));

		user.setRoleId(resultSet.getInt("role_id"));
		user.setRoleName(resultSet.getString("role_name"));

		int clientId = resultSet.getInt("client_id");

		if (resultSet.wasNull()) {
			user.setClientId(null);
		} else {
			user.setClientId(clientId);
		}

		user.setStatus(resultSet.getString("status"));
		user.setCreatedAt(resultSet.getTimestamp("created_at"));

		return user;
	}
}