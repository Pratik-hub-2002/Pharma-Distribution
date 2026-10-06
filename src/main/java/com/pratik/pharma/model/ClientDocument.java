package com.pratik.pharma.model;

import java.sql.Date;
import java.sql.Timestamp;

public class ClientDocument {

	private int documentId;
	private int clientId;

	private String documentType;
	private String documentNumber;

	private Date issueDate;
	private Date expiryDate;

	private String documentStatus;

	private String documentFilePath;

	// Nullable database column
	private Integer verifiedBy;

	private Timestamp verifiedAt;
	private Timestamp createdAt;

	public ClientDocument() {
	}

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(String documentStatus) {
		this.documentStatus = documentStatus;
	}

	public String getDocumentFilePath() {
		return documentFilePath;
	}

	public void setDocumentFilePath(String documentFilePath) {
		this.documentFilePath = documentFilePath;
	}

	public Integer getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(Integer verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public Timestamp getVerifiedAt() {
		return verifiedAt;
	}

	public void setVerifiedAt(Timestamp verifiedAt) {
		this.verifiedAt = verifiedAt;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
}