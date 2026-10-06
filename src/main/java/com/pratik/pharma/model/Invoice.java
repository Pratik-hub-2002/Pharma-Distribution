package com.pratik.pharma.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Invoice {

	private int invoiceId;
	private int orderId;
	private String invoiceNumber;
	private Date invoiceDate;

	private BigDecimal subtotal;
	private BigDecimal discountAmount;
	private BigDecimal taxAmount;
	private BigDecimal totalAmount;

	private Date dueDate;

	private String invoiceStatus;
	private int createdBy;
	private Timestamp createdAt;

	public Invoice() {
	}

	// =========================================================
	// invoiceId
	// =========================================================

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	// =========================================================
	// orderId
	// =========================================================

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	// =========================================================
	// invoiceNumber
	// =========================================================

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	// =========================================================
	// invoiceDate
	// =========================================================

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	// =========================================================
	// subtotal
	// =========================================================

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	// =========================================================
	// discountAmount
	// =========================================================

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	// =========================================================
	// taxAmount
	// =========================================================

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	// =========================================================
	// totalAmount
	// =========================================================

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	// =========================================================
	// dueDate
	// =========================================================

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	// =========================================================
	// invoiceStatus
	// =========================================================

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	// =========================================================
	// createdBy
	// =========================================================

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	// =========================================================
	// createdAt
	// =========================================================

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
}