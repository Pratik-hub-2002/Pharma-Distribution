package com.pratik.pharma.model;

import java.sql.Timestamp;

public class Inventory {

	private int inventoryId;
	private int batchId;
	private int quantity;
	private int reservedQuantity;
	private int damagedQuantity;
	private int reorderLevel;
	private Timestamp updatedAt;

	// Display fields from batch/product
	private String batchNumber;
	private String productName;
	private String productCode;
	private java.sql.Date expiryDate;

	public Inventory() {
	}

	public int getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(int inventoryId) {
		this.inventoryId = inventoryId;
	}

	public int getBatchId() {
		return batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getReservedQuantity() {
		return reservedQuantity;
	}

	public void setReservedQuantity(int reservedQuantity) {
		this.reservedQuantity = reservedQuantity;
	}

	public int getDamagedQuantity() {
		return damagedQuantity;
	}

	public void setDamagedQuantity(int damagedQuantity) {
		this.damagedQuantity = damagedQuantity;
	}

	public int getReorderLevel() {
		return reorderLevel;
	}

	public void setReorderLevel(int reorderLevel) {
		this.reorderLevel = reorderLevel;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public java.sql.Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(java.sql.Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getAvailableQuantity() {
		return quantity - reservedQuantity - damagedQuantity;
	}
}