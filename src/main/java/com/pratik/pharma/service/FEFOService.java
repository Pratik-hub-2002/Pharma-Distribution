package com.pratik.pharma.service;

import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.dao.BatchDAO;
import com.pratik.pharma.model.Batch;
import com.pratik.pharma.model.BatchAllocation;

public class FEFOService {

	private BatchDAO batchDAO;

	public FEFOService() {
		batchDAO = new BatchDAO();
	}

	// =========================================================
	// FEFO BATCH ALLOCATION
	// =========================================================

	public List<BatchAllocation> allocateBatches(int productId, int requiredQuantity) {

		List<BatchAllocation> allocations = new ArrayList<>();

		if (requiredQuantity <= 0) {
			return allocations;
		}

		List<Batch> batches = batchDAO.getAvailableBatchesByProductFEFO(productId);

		int remainingQuantity = requiredQuantity;

		for (Batch batch : batches) {

			if (remainingQuantity <= 0) {
				break;
			}

			int availableQuantity = batch.getAvailableQuantity();

			if (availableQuantity <= 0) {
				continue;
			}

			int allocatedQuantity = Math.min(remainingQuantity, availableQuantity);

			BatchAllocation allocation = new BatchAllocation();

			allocation.setBatchId(batch.getBatchId());

			allocation.setAllocatedQuantity(allocatedQuantity);

			allocation.setFreeQuantity(0);

			allocations.add(allocation);

			remainingQuantity -= allocatedQuantity;
		}

		if (remainingQuantity > 0) {

			throw new IllegalStateException("Insufficient stock. Required: " + requiredQuantity + ", Available: "
					+ (requiredQuantity - remainingQuantity));
		}

		return allocations;
	}

	public void distributeFreeQuantity(List<BatchAllocation> allocations, int freeQuantity) {

		int remainingFree = freeQuantity;

		for (int i = allocations.size() - 1; i >= 0 && remainingFree > 0; i--) {

			BatchAllocation allocation = allocations.get(i);

			int allocated = allocation.getAllocatedQuantity();

			int freeForThisBatch = Math.min(allocated, remainingFree);

			allocation.setFreeQuantity(freeForThisBatch);

			remainingFree -= freeForThisBatch;
		}
	}
}