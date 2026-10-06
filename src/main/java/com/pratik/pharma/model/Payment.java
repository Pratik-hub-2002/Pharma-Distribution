package com.pratik.pharma.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Payment {

	private int paymentId;
	private int invoiceId;

	private Date paymentDate;

	private BigDecimal amount;

	private String paymentMethod;

	private String transactionReference;

	private String paymentStatus;

	private int recordedBy;

	private Timestamp createdAt;

	// Display fields
	private String invoiceNumber;
	private BigDecimal invoiceTotalAmount;
	private Date invoiceDueDate;
	private int orderId;
	private String clientName;
	private String recordedByName;

	// Calculated fields
	private BigDecimal paidAmount;
	private BigDecimal outstandingAmount;

	// =========================================================
	// PAYMENT ID
	// =========================================================

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	// =========================================================
	// INVOICE ID
	// =========================================================

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	// =========================================================
	// PAYMENT DATE
	// =========================================================

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	// =========================================================
	// AMOUNT
	// =========================================================

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	// =========================================================
	// PAYMENT METHOD
	// =========================================================

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	// =========================================================
	// TRANSACTION REFERENCE
	// =========================================================

	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}

	// =========================================================
	// PAYMENT STATUS
	// =========================================================

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	// =========================================================
	// RECORDED BY
	// =========================================================

	public int getRecordedBy() {
		return recordedBy;
	}

	public void setRecordedBy(int recordedBy) {
		this.recordedBy = recordedBy;
	}

	// =========================================================
	// CREATED AT
	// =========================================================

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	// =========================================================
	// INVOICE NUMBER
	// =========================================================

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	// =========================================================
	// INVOICE TOTAL
	// =========================================================

	public BigDecimal getInvoiceTotalAmount() {
		return invoiceTotalAmount;
	}

	public void setInvoiceTotalAmount(BigDecimal invoiceTotalAmount) {

		this.invoiceTotalAmount = invoiceTotalAmount;
	}

	// =========================================================
	// INVOICE DUE DATE
	// =========================================================

	public Date getInvoiceDueDate() {
		return invoiceDueDate;
	}

	public void setInvoiceDueDate(Date invoiceDueDate) {
		this.invoiceDueDate = invoiceDueDate;
	}

	// =========================================================
	// ORDER ID
	// =========================================================

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	// =========================================================
	// CLIENT NAME
	// =========================================================

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	// =========================================================
	// RECORDED BY NAME
	// =========================================================

	public String getRecordedByName() {
		return recordedByName;
	}

	public void setRecordedByName(String recordedByName) {
		this.recordedByName = recordedByName;
	}

	// =========================================================
	// PAID AMOUNT
	// =========================================================

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	// =========================================================
	// OUTSTANDING AMOUNT
	// =========================================================

	public BigDecimal getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(BigDecimal outstandingAmount) {

		this.outstandingAmount = outstandingAmount;
	}
}