package com.pratik.pharma.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Client {

	private int clientId;
	private String clientName;
	private String clientType;
	private String ownerName;
	private String contactPerson;
	private String email;
	private String phone;
	private String address;
	private String city;
	private String state;
	private String pincode;

	// Business / Legal Details
	private String gstNumber;
	private String panNumber;
	private String drugLicenseNumber;
	private String wholesaleLicenseNumber;
	private String purchasePermitNumber;

	// Credit Details
	private BigDecimal creditLimit;
	private int creditPeriodDays;

	private String status;
	private String verificationStatus;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public Client() {
	}

	// clientId
	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	// clientName
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	// clientType
	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	// ownerName
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	// contactPerson
	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	// email
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// phone
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	// address
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// city
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	// state
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	// pincode
	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	// gstNumber
	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	// panNumber
	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	// drugLicenseNumber
	public String getDrugLicenseNumber() {
		return drugLicenseNumber;
	}

	public void setDrugLicenseNumber(String drugLicenseNumber) {
		this.drugLicenseNumber = drugLicenseNumber;
	}

	// wholesaleLicenseNumber
	public String getWholesaleLicenseNumber() {
		return wholesaleLicenseNumber;
	}

	public void setWholesaleLicenseNumber(String wholesaleLicenseNumber) {
		this.wholesaleLicenseNumber = wholesaleLicenseNumber;
	}

	// purchasePermitNumber
	public String getPurchasePermitNumber() {
		return purchasePermitNumber;
	}

	public void setPurchasePermitNumber(String purchasePermitNumber) {
		this.purchasePermitNumber = purchasePermitNumber;
	}

	// creditLimit
	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	// creditPeriodDays
	public int getCreditPeriodDays() {
		return creditPeriodDays;
	}

	public void setCreditPeriodDays(int creditPeriodDays) {
		this.creditPeriodDays = creditPeriodDays;
	}

	// status
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	// verificationStatus
	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	// createdAt
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	// updatedAt
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
}