package com.pratik.pharma.model;

public class Role {

	private int roleId;
	private String roleName;
	private String description;

	// =========================================================
	// CONSTRUCTOR
	// =========================================================

	public Role() {
	}

	// =========================================================
	// GETTERS AND SETTERS
	// =========================================================

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}