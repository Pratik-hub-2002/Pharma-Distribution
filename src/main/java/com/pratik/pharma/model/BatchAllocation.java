package com.pratik.pharma.model;

public class BatchAllocation {

	private int batchId;
	private int allocatedQuantity;
	private int freeQuantity;

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
}