package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.ClientDocument;
import com.pratik.pharma.util.DBConnection;

public class ClientDocumentDAO {

	// =========================================================
	// GET ALL DOCUMENTS FOR A CLIENT
	// =========================================================

	public List<ClientDocument> getDocumentsByClientId(int clientId) {

		List<ClientDocument> documents = new ArrayList<>();

		String sql = "SELECT document_id, " + "client_id, " + "document_type, " + "document_number, " + "issue_date, "
				+ "expiry_date, " + "document_status, " + "document_file_path, " + "verified_by, " + "verified_at, "
				+ "created_at " + "FROM client_documents " + "WHERE client_id = ? " + "ORDER BY document_id";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, clientId);

			System.out.println("Fetching documents for Client ID: " + clientId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					ClientDocument document = new ClientDocument();

					document.setDocumentId(rs.getInt("document_id"));

					document.setClientId(rs.getInt("client_id"));

					document.setDocumentType(rs.getString("document_type"));

					document.setDocumentNumber(rs.getString("document_number"));

					document.setIssueDate(rs.getDate("issue_date"));

					document.setExpiryDate(rs.getDate("expiry_date"));

					document.setDocumentStatus(rs.getString("document_status"));

					document.setDocumentFilePath(rs.getString("document_file_path"));

					// verified_by can be NULL
					int verifiedBy = rs.getInt("verified_by");

					if (rs.wasNull()) {

						document.setVerifiedBy(null);

					} else {

						document.setVerifiedBy(verifiedBy);
					}

					document.setVerifiedAt(rs.getTimestamp("verified_at"));

					document.setCreatedAt(rs.getTimestamp("created_at"));

					documents.add(document);
				}
			}

			System.out.println("Documents found: " + documents.size());

		} catch (Exception e) {

			e.printStackTrace();
		}

		return documents;
	}

	// =========================================================
	// VERIFY / REJECT DOCUMENT
	// =========================================================

	public void updateDocumentStatus(int documentId, String status, int verifiedBy) {

		String sql = "UPDATE client_documents " + "SET document_status = ?, " + "verified_by = ?, "
				+ "verified_at = CURRENT_TIMESTAMP " + "WHERE document_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, status);

			ps.setInt(2, verifiedBy);

			ps.setInt(3, documentId);

			int rowsUpdated = ps.executeUpdate();

			System.out.println("Document ID " + documentId + " updated. Rows affected: " + rowsUpdated);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}