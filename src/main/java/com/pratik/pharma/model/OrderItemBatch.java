package com.pratik.pharma.model;

import java.sql.Timestamp;

public class OrderItemBatch {

	private int orderItemBatchId;

	private int orderItemId;

	private int batchId;

	private int allocatedQuantity;

	private int freeQuantity;

	private Timestamp createdAt;

	// =========================================
	// Getters and Setters
	// =========================================

	public int getOrderItemBatchId() {
		return orderItemBatchId;
	}

	public void setOrderItemBatchId(int orderItemBatchId) {
		this.orderItemBatchId = orderItemBatchId;
	}

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getBatchId() {
		return batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public int getAllocatedQuantity() {
		return allocatedQuantity;
	}

	public void setAllocatedQuantity(int allocatedQuantity) {
		this.allocatedQuantity = allocatedQuantity;
	}

	public int getFreeQuantity() {
		return freeQuantity;
	}

	public void setFreeQuantity(int freeQuantity) {
		this.freeQuantity = freeQuantity;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
}