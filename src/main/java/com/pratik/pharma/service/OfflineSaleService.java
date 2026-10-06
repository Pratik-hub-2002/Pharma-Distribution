package com.pratik.pharma.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pratik.pharma.dao.BatchDAO;
import com.pratik.pharma.dao.ClientDAO;
import com.pratik.pharma.dao.InventoryDAO;
import com.pratik.pharma.dao.InvoiceDAO;
import com.pratik.pharma.dao.OrderDAO;
import com.pratik.pharma.dao.OrderItemBatchDAO;
import com.pratik.pharma.dao.OrderItemDAO;
import com.pratik.pharma.dao.PaymentDAO;
import com.pratik.pharma.dao.ProductDAO;
import com.pratik.pharma.dao.SchemeDAO;
import com.pratik.pharma.dao.StockTransactionDAO;

import com.pratik.pharma.model.BatchAllocation;
import com.pratik.pharma.model.Client;
import com.pratik.pharma.model.Invoice;
import com.pratik.pharma.model.Order;
import com.pratik.pharma.model.OrderItem;
import com.pratik.pharma.model.OrderItemBatch;
import com.pratik.pharma.model.Payment;
import com.pratik.pharma.model.Product;
import com.pratik.pharma.model.Scheme;

import com.pratik.pharma.util.DBConnection;

public class OfflineSaleService {

	private final OrderDAO orderDAO = new OrderDAO();
	private final OrderItemDAO orderItemDAO = new OrderItemDAO();

	private final OrderItemBatchDAO orderItemBatchDAO = new OrderItemBatchDAO();

	private final ClientDAO clientDAO = new ClientDAO();

	private final ProductDAO productDAO = new ProductDAO();

	private final SchemeDAO schemeDAO = new SchemeDAO();

	private final BatchDAO batchDAO = new BatchDAO();

	private final InventoryDAO inventoryDAO = new InventoryDAO();

	private final StockTransactionDAO stockTransactionDAO = new StockTransactionDAO();

	private final InvoiceDAO invoiceDAO = new InvoiceDAO();

	private final PaymentDAO paymentDAO = new PaymentDAO();

	private final ClientEligibilityService clientEligibilityService = new ClientEligibilityService();

	private final CreditService creditService = new CreditService();

	private final FEFOService fefoService = new FEFOService();

	// =========================================================
	// CREATE OFFLINE SALE
	// =========================================================

	public OfflineSaleResult createOfflineSale(int clientId, int createdBy, int[] productIds, int[] quantities,
			String paymentMethod, String transactionReference) throws Exception {

		if (productIds == null || quantities == null || productIds.length == 0) {

			throw new IllegalArgumentException("At least one product is required.");
		}

		if (productIds.length != quantities.length) {

			throw new IllegalArgumentException("Invalid product and quantity data.");
		}

		if (createdBy <= 0) {

			throw new IllegalArgumentException("Invalid logged-in user.");
		}

		// -----------------------------------------------------
		// DUPLICATE PRODUCT CHECK
		// -----------------------------------------------------

		Set<Integer> productSet = new HashSet<>();

		for (int productId : productIds) {

			if (!productSet.add(productId)) {

				throw new IllegalArgumentException("The same product cannot be added twice.");
			}
		}

		// -----------------------------------------------------
		// PAYMENT TYPE
		// -----------------------------------------------------

		if (paymentMethod == null || paymentMethod.isBlank()) {

			throw new IllegalArgumentException("Payment method is required.");
		}

		paymentMethod = paymentMethod.trim().toUpperCase();

		boolean creditSale = "CREDIT".equals(paymentMethod);

		Connection connection = null;

		try {

			connection = DBConnection.getConnection();

			connection.setAutoCommit(false);

			// =================================================
			// CLIENT
			// =================================================

			Client client = clientDAO.getClientById(clientId);

			if (client == null) {

				throw new IllegalStateException("Selected client was not found.");
			}

			// =================================================
			// CLIENT ELIGIBILITY
			// =================================================

			boolean eligible = clientEligibilityService.isEligible(connection, clientId);

			if (!eligible) {

				throw new IllegalStateException("Client must be ACTIVE and VERIFIED.");
			}

			// =================================================
			// TEMPORARY ORDER ITEMS
			// =================================================

			List<OfflineItemData> items = new ArrayList<>();

			BigDecimal orderSubtotal = BigDecimal.ZERO;

			BigDecimal orderDiscount = BigDecimal.ZERO;

			BigDecimal orderTax = BigDecimal.ZERO;

			BigDecimal orderTotal = BigDecimal.ZERO;

			// =================================================
			// PROCESS PRODUCTS
			// =================================================

			for (int i = 0; i < productIds.length; i++) {

				int productId = productIds[i];

				int quantity = quantities[i];

				if (quantity <= 0) {

					throw new IllegalArgumentException("Quantity must be greater than 0.");
				}

				// -------------------------------------------------
				// PRODUCT
				// -------------------------------------------------

				Product product = productDAO.getProductById(productId);

				if (product == null) {

					throw new IllegalStateException("Product ID " + productId + " was not found.");
				}

				if (product.getStatus() != null && !"ACTIVE".equalsIgnoreCase(product.getStatus())) {

					throw new IllegalStateException("Product " + product.getProductName() + " is not active.");
				}

				BigDecimal unitPrice = product.getSellingPrice();

				if (unitPrice == null) {

					throw new IllegalStateException("Selling price is missing for " + product.getProductName());
				}

				// -------------------------------------------------
				// SUBTOTAL
				// -------------------------------------------------

				BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

				// -------------------------------------------------
				// SCHEME
				// -------------------------------------------------

				BigDecimal discountAmount = BigDecimal.ZERO;

				int freeQuantity = 0;

				Scheme scheme = schemeDAO.getActiveSchemeByProduct(productId);

				if (scheme != null) {

					// BUY GET FREE
					if ("BUY_GET_FREE".equalsIgnoreCase(scheme.getSchemeType())) {

						int minimum = scheme.getMinimumQuantity();

						int free = scheme.getFreeQuantity();

						if (minimum > 0 && quantity >= minimum) {

							freeQuantity = (quantity / minimum) * free;
						}
					}

					// PERCENTAGE DISCOUNT
					else if ("PERCENTAGE_DISCOUNT".equalsIgnoreCase(scheme.getSchemeType())) {

						int minimum = scheme.getMinimumQuantity();

						if (quantity >= minimum) {

							BigDecimal percentage = scheme.getDiscountPercentage();

							if (percentage != null) {

								discountAmount = subtotal.multiply(percentage).divide(BigDecimal.valueOf(100));
							}
						}
					}
				}

				// -------------------------------------------------
				// GST
				// -------------------------------------------------

				BigDecimal taxableAmount = subtotal.subtract(discountAmount);

				BigDecimal gstRate = product.getGstRate();

				if (gstRate == null) {
					gstRate = BigDecimal.ZERO;
				}

				BigDecimal taxAmount = taxableAmount.multiply(gstRate).divide(BigDecimal.valueOf(100));

				BigDecimal lineTotal = taxableAmount.add(taxAmount);

				// -------------------------------------------------
				// PHYSICAL QUANTITY
				// -------------------------------------------------

				int totalQuantity = quantity + freeQuantity;

				// -------------------------------------------------
				// TOTALS
				// -------------------------------------------------

				orderSubtotal = orderSubtotal.add(subtotal);

				orderDiscount = orderDiscount.add(discountAmount);

				orderTax = orderTax.add(taxAmount);

				orderTotal = orderTotal.add(lineTotal);

				// -------------------------------------------------
				// STORE TEMP DATA
				// -------------------------------------------------

				OfflineItemData item = new OfflineItemData();

				item.productId = productId;

				item.quantity = quantity;

				item.freeQuantity = freeQuantity;

				item.totalQuantity = totalQuantity;

				item.unitPrice = unitPrice;

				item.discountAmount = discountAmount;

				item.taxAmount = taxAmount;

				item.lineTotal = lineTotal;

				item.scheme = scheme;

				item.product = product;

				items.add(item);
			}

			// =================================================
			// CREDIT CHECK
			// =================================================

			if (creditSale) {

				boolean sufficientCredit = creditService.hasSufficientCredit(connection, clientId, orderTotal);

				if (!sufficientCredit) {

					throw new IllegalStateException("Insufficient client credit.");
				}
			}

			// =================================================
			// CREATE ORDER
			// =================================================

			Order order = new Order();

			order.setClientId(clientId);

			order.setOrderStatus("COMPLETED");

			order.setSubtotal(orderSubtotal);

			order.setDiscountAmount(orderDiscount);

			order.setTaxAmount(orderTax);

			order.setTotalAmount(orderTotal);

			if (creditSale) {

				order.setCreditStatus("PENDING");

			} else {

				order.setCreditStatus("PAID");
			}

			order.setApprovedBy(null);

			order.setCreatedBy(createdBy);

			order.setOrderSource("OFFLINE");

			int orderId = orderDAO.addOrder(connection, order);

			if (orderId <= 0) {

				throw new IllegalStateException("Failed to create offline order.");
			}

			// =================================================
			// CREATE ORDER ITEMS
			// =================================================

			for (OfflineItemData itemData : items) {

				OrderItem item = new OrderItem();

				item.setOrderId(orderId);

				item.setProductId(itemData.productId);

				item.setOrderedQuantity(itemData.quantity);

				item.setFreeQuantity(itemData.freeQuantity);

				item.setTotalQuantity(itemData.totalQuantity);

				item.setUnitPrice(itemData.unitPrice);

				item.setDiscountAmount(itemData.discountAmount);

				item.setTaxAmount(itemData.taxAmount);

				item.setLineTotal(itemData.lineTotal);

				if (itemData.scheme != null) {

					item.setSchemeId(itemData.scheme.getSchemeId());

				} else {

					item.setSchemeId(null);
				}

				int orderItemId = orderItemDAO.addOrderItem(connection, item);

				if (orderItemId <= 0) {

					throw new IllegalStateException("Failed to create order item.");
				}

				// =============================================
				// FEFO
				// =============================================

				List<BatchAllocation> allocations = fefoService.allocateBatches(itemData.productId,
						itemData.totalQuantity);

				if (allocations == null || allocations.isEmpty()) {

					throw new IllegalStateException("No available stock for " + itemData.product.getProductName());
				}

				// =============================================
				// FREE QUANTITY
				// =============================================

				fefoService.distributeFreeQuantity(allocations, itemData.freeQuantity);

				// =============================================
				// BATCHES
				// =============================================

				for (BatchAllocation allocation : allocations) {

					int allocatedQuantity = allocation.getAllocatedQuantity();

					if (allocatedQuantity <= 0) {
						continue;
					}

					// -----------------------------------------
					// REDUCE INVENTORY
					// -----------------------------------------

					boolean inventoryUpdated = inventoryDAO.reduceAvailableQuantity(connection, allocation.getBatchId(),
							allocatedQuantity);

					if (!inventoryUpdated) {

						throw new IllegalStateException("Insufficient stock for batch ID: " + allocation.getBatchId());
					}

					// -----------------------------------------
					// ORDER ITEM BATCH
					// -----------------------------------------

					OrderItemBatch orderItemBatch = new OrderItemBatch();

					orderItemBatch.setOrderItemId(orderItemId);

					orderItemBatch.setBatchId(allocation.getBatchId());

					orderItemBatch.setAllocatedQuantity(allocatedQuantity);

					orderItemBatch.setFreeQuantity(allocation.getFreeQuantity());

					int batchRecord = orderItemBatchDAO.addOrderItemBatch(connection, orderItemBatch);

					if (batchRecord <= 0) {

						throw new IllegalStateException("Failed to save batch allocation.");
					}

					// -----------------------------------------
					// STOCK OUT
					// -----------------------------------------

					int transactionId = stockTransactionDAO.addTransaction(connection, allocation.getBatchId(), "OUT",
							allocatedQuantity, "OFFLINE_ORDER", orderId, "Stock issued against offline sale",
							createdBy);

					if (transactionId <= 0) {

						throw new IllegalStateException("Failed to create stock transaction.");
					}
				}
			}

			// =================================================
			// CREATE INVOICE
			// =================================================

			LocalDate today = LocalDate.now();

			Invoice invoice = new Invoice();

			invoice.setOrderId(orderId);

			invoice.setInvoiceNumber(String.format("INV-%d-%06d", today.getYear(), orderId));

			invoice.setInvoiceDate(Date.valueOf(today));

			invoice.setSubtotal(orderSubtotal);

			invoice.setDiscountAmount(orderDiscount);

			invoice.setTaxAmount(orderTax);

			invoice.setTotalAmount(orderTotal);

			// -------------------------------------------------
			// DUE DATE
			// -------------------------------------------------

			LocalDate dueDate;

			if (creditSale) {

				int creditDays = client.getCreditPeriodDays();

				if (creditDays < 0) {
					creditDays = 0;
				}

				dueDate = today.plusDays(creditDays);

			} else {

				dueDate = today;
			}

			invoice.setDueDate(Date.valueOf(dueDate));

			// -------------------------------------------------
			// INVOICE STATUS
			// -------------------------------------------------

			if (creditSale) {

				invoice.setInvoiceStatus("PENDING");

			} else {

				invoice.setInvoiceStatus("PAID");
			}

			invoice.setCreatedBy(createdBy);

			int invoiceId = invoiceDAO.createInvoice(connection, invoice);

			if (invoiceId <= 0) {

				throw new IllegalStateException("Failed to create invoice.");
			}

			// =================================================
			// PAYMENT
			// =================================================

			if (!creditSale) {

				Payment payment = new Payment();

				payment.setInvoiceId(invoiceId);

				payment.setPaymentDate(Date.valueOf(today));

				payment.setAmount(orderTotal);

				payment.setPaymentMethod(paymentMethod);

				payment.setTransactionReference(transactionReference);

				payment.setPaymentStatus("SUCCESS");

				payment.setRecordedBy(createdBy);

				int paymentId = paymentDAO.addPayment(connection, payment);

				if (paymentId <= 0) {

					throw new IllegalStateException("Failed to record payment.");
				}
			}

			// =================================================
			// COMMIT
			// =================================================

			connection.commit();

			return new OfflineSaleResult(orderId, invoiceId, orderTotal);

		} catch (Exception e) {

			if (connection != null) {

				try {
					connection.rollback();
				} catch (Exception rollbackException) {
					rollbackException.printStackTrace();
				}
			}

			throw e;

		} finally {

			if (connection != null) {

				try {
					connection.setAutoCommit(true);
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// =========================================================
	// TEMPORARY ITEM DATA
	// =========================================================

	private static class OfflineItemData {

		int productId;

		int quantity;

		int freeQuantity;

		int totalQuantity;

		BigDecimal unitPrice;

		BigDecimal discountAmount;

		BigDecimal taxAmount;

		BigDecimal lineTotal;

		Scheme scheme;

		Product product;
	}

	// =========================================================
	// RESULT
	// =========================================================

	public static class OfflineSaleResult {

		private final int orderId;

		private final int invoiceId;

		private final BigDecimal totalAmount;

		public OfflineSaleResult(int orderId, int invoiceId, BigDecimal totalAmount) {

			this.orderId = orderId;
			this.invoiceId = invoiceId;
			this.totalAmount = totalAmount;
		}

		public int getOrderId() {
			return orderId;
		}

		public int getInvoiceId() {
			return invoiceId;
		}

		public BigDecimal getTotalAmount() {
			return totalAmount;
		}
	}
}