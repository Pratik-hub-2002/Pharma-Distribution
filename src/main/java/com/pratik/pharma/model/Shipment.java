package com.pratik.pharma.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Shipment {

	private int shipmentId;
	private int orderId;

	private String trackingNumber;

	private Date shipmentDate;
	private Date dispatchDate;
	private Date deliveryDate;

	private String shipmentStatus;

	private boolean coldChainRequired;

	private String temperatureStatus;

	private int createdBy;

	private Timestamp createdAt;

	// Display fields
	private String clientName;
	private String createdByName;

	// =========================================================
	// SHIPMENT ID
	// =========================================================

	public int getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(int shipmentId) {
		this.shipmentId = shipmentId;
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
	// TRACKING NUMBER
	// =========================================================

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	// =========================================================
	// SHIPMENT DATE
	// =========================================================

	public Date getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(Date shipmentDate) {
		this.shipmentDate = shipmentDate;
	}

	// =========================================================
	// DISPATCH DATE
	// =========================================================

	public Date getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	// =========================================================
	// DELIVERY DATE
	// =========================================================

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	// =========================================================
	// SHIPMENT STATUS
	// =========================================================

	public String getShipmentStatus() {
		return shipmentStatus;
	}

	public void setShipmentStatus(String shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
	}

	// =========================================================
	// COLD CHAIN REQUIRED
	// =========================================================

	public boolean isColdChainRequired() {
		return coldChainRequired;
	}

	public void setColdChainRequired(boolean coldChainRequired) {
		this.coldChainRequired = coldChainRequired;
	}

	// =========================================================
	// TEMPERATURE STATUS
	// =========================================================

	public String getTemperatureStatus() {
		return temperatureStatus;
	}

	public void setTemperatureStatus(String temperatureStatus) {
		this.temperatureStatus = temperatureStatus;
	}

	// =========================================================
	// CREATED BY
	// =========================================================

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
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
	// CLIENT NAME
	// =========================================================

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	// =========================================================
	// CREATED BY NAME
	// =========================================================

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
}