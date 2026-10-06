package com.pratik.pharma.model;

import java.math.BigDecimal;
import java.sql.Date;

public class InvoiceItem {

	private int orderItemId;

	private int productId;
	private String productName;
	private String productCode;

	private int batchId;
	private String batchNumber;

	private Date manufacturingDate;
	private Date expiryDate;

	private String hsnCode;

	private int orderedQuantity;
	private int freeQuantity;
	private int totalQuantity;

	private BigDecimal unitPrice;
	private BigDecimal discountAmount;
	private BigDecimal taxAmount;
	private BigDecimal lineTotal;

	private BigDecimal gstRate;

	// =========================================================
	// ORDER ITEM ID
	// =========================================================

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	// =========================================================
	// PRODUCT ID
	// =========================================================

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	// =========================================================
	// PRODUCT NAME
	// =========================================================

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	// =========================================================
	// PRODUCT CODE
	// =========================================================

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	// =========================================================
	// BATCH ID
	// =========================================================

	public int getBatchId() {
		return batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	// =========================================================
	// BATCH NUMBER
	// =========================================================

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	// =========================================================
	// MANUFACTURING DATE
	// =========================================================

	public Date getManufacturingDate() {
		return manufacturingDate;
	}

	public void setManufacturingDate(Date manufacturingDate) {
		this.manufacturingDate = manufacturingDate;
	}

	// =========================================================
	// EXPIRY DATE
	// =========================================================

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	// =========================================================
	// HSN CODE
	// =========================================================

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	// =========================================================
	// ORDERED QUANTITY
	// =========================================================

	public int getOrderedQuantity() {
		return orderedQuantity;
	}

	public void setOrderedQuantity(int orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}

	// =========================================================
	// FREE QUANTITY
	// =========================================================

	public int getFreeQuantity() {
		return freeQuantity;
	}

	public void setFreeQuantity(int freeQuantity) {
		this.freeQuantity = freeQuantity;
	}

	// =========================================================
	// TOTAL QUANTITY
	// =========================================================

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	// =========================================================
	// UNIT PRICE
	// =========================================================

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	// =========================================================
	// DISCOUNT
	// =========================================================

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	// =========================================================
	// TAX
	// =========================================================

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	// =========================================================
	// LINE TOTAL
	// =========================================================

	public BigDecimal getLineTotal() {
		return lineTotal;
	}

	public void setLineTotal(BigDecimal lineTotal) {
		this.lineTotal = lineTotal;
	}

	// =========================================================
	// GST RATE
	// =========================================================

	public BigDecimal getGstRate() {
		return gstRate;
	}

	public void setGstRate(BigDecimal gstRate) {
		this.gstRate = gstRate;
	}
}