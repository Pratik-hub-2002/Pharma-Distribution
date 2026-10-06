package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.pratik.pharma.model.CompanyProfile;
import com.pratik.pharma.util.DBConnection;

public class CompanyProfileDAO {

	// =========================================================
	// GET COMPANY PROFILE
	// =========================================================

	public CompanyProfile getCompanyProfile() {

		CompanyProfile company = null;

		String sql = """
				SELECT
				    company_id,
				    company_name,
				    legal_name,
				    address_line1,
				    address_line2,
				    city,
				    state,
				    country,
				    postal_code,
				    gstin,
				    pan_number,
				    drug_license_number,
				    wholesale_license_number,
				    phone,
				    email,
				    website,
				    bank_name,
				    bank_account_name,
				    bank_account_number,
				    ifsc_code,
				    created_at,
				    updated_at
				FROM company_profile
				ORDER BY company_id
				LIMIT 1
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			if (resultSet.next()) {

				company = mapCompanyProfile(resultSet);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return company;
	}

	// =========================================================
	// MAP RESULTSET → COMPANY PROFILE
	// =========================================================

	private CompanyProfile mapCompanyProfile(ResultSet resultSet) throws Exception {

		CompanyProfile company = new CompanyProfile();

		company.setCompanyId(resultSet.getInt("company_id"));

		company.setCompanyName(resultSet.getString("company_name"));

		company.setLegalName(resultSet.getString("legal_name"));

		company.setAddressLine1(resultSet.getString("address_line1"));

		company.setAddressLine2(resultSet.getString("address_line2"));

		company.setCity(resultSet.getString("city"));

		company.setState(resultSet.getString("state"));

		company.setCountry(resultSet.getString("country"));

		company.setPostalCode(resultSet.getString("postal_code"));

		company.setGstin(resultSet.getString("gstin"));

		company.setPanNumber(resultSet.getString("pan_number"));

		company.setDrugLicenseNumber(resultSet.getString("drug_license_number"));

		company.setWholesaleLicenseNumber(resultSet.getString("wholesale_license_number"));

		company.setPhone(resultSet.getString("phone"));

		company.setEmail(resultSet.getString("email"));

		company.setWebsite(resultSet.getString("website"));

		company.setBankName(resultSet.getString("bank_name"));

		company.setBankAccountName(resultSet.getString("bank_account_name"));

		company.setBankAccountNumber(resultSet.getString("bank_account_number"));

		company.setIfscCode(resultSet.getString("ifsc_code"));

		company.setCreatedAt(resultSet.getTimestamp("created_at"));

		company.setUpdatedAt(resultSet.getTimestamp("updated_at"));

		return company;
	}
}