package com.pratik.pharma.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

import com.pratik.pharma.dao.ClientDAO;
import com.pratik.pharma.dao.InventoryDAO;
import com.pratik.pharma.dao.OrderDAO;
import com.pratik.pharma.dao.OrderItemBatchDAO;
import com.pratik.pharma.dao.OrderItemDAO;
import com.pratik.pharma.dao.ProductDAO;
import com.pratik.pharma.dao.SchemeDAO;
import com.pratik.pharma.dao.StockTransactionDAO;

import com.pratik.pharma.model.BatchAllocation;
import com.pratik.pharma.model.Client;
import com.pratik.pharma.model.Order;
import com.pratik.pharma.model.OrderItem;
import com.pratik.pharma.model.OrderItemBatch;
import com.pratik.pharma.model.Product;
import com.pratik.pharma.model.Scheme;

import com.pratik.pharma.util.DBConnection;

public class OfflineSaleService {

	private ClientDAO clientDAO;
	private ProductDAO productDAO;
	private SchemeDAO schemeDAO;
	private OrderDAO orderDAO;
	private OrderItemDAO orderItemDAO;
	private InventoryDAO inventoryDAO;
	private OrderItemBatchDAO orderItemBatchDAO;
	private StockTransactionDAO stockTransactionDAO;

	private ClientEligibilityService clientEligibilityService;
	private CreditService creditService;
	private FEFOService fefoService;

	public OfflineSaleService() {

		clientDAO = new ClientDAO();
		productDAO = new ProductDAO();
		schemeDAO = new SchemeDAO();

		orderDAO = new OrderDAO();
		orderItemDAO = new OrderItemDAO();

		inventoryDAO = new InventoryDAO();
		orderItemBatchDAO = new OrderItemBatchDAO();
		stockTransactionDAO = new StockTransactionDAO();

		clientEligibilityService = new ClientEligibilityService();
		creditService = new CreditService();
		fefoService = new FEFOService();
	}

	// =========================================================
	// CREATE OFFLINE SALE
	// =========================================================

	public OfflineSaleResult createOfflineSale(int clientId, int createdBy, int[] productIds, int[] quantities,
			String paymentMethod, String transactionReference) throws Exception {

		// -----------------------------------------------------
		// BASIC VALIDATION
		// -----------------------------------------------------

		if (productIds == null || quantities == null) {
			throw new IllegalArgumentException("Product and quantity data is required.");
		}

		if (productIds.length == 0 || productIds.length != quantities.length) {

			throw new IllegalArgumentException("Invalid product and quantity data.");
		}

		if (paymentMethod == null || paymentMethod.isBlank()) {

			throw new IllegalArgumentException("Payment method is required.");
		}

		paymentMethod = paymentMethod.trim().toUpperCase();

		if (!paymentMethod.equals("CASH") && !paymentMethod.equals("UPI") && !paymentMethod.equals("CARD")
				&& !paymentMethod.equals("CREDIT")) {

			throw new IllegalArgumentException("Invalid payment method.");
		}

		// -----------------------------------------------------
		// GET CLIENT
		// -----------------------------------------------------

		Client client = clientDAO.getClientById(clientId);

		if (client == null) {

			throw new IllegalArgumentException("Client not found.");
		}

		// -----------------------------------------------------
		// CHECK DUPLICATE PRODUCTS
		// -----------------------------------------------------

		for (int i = 0; i < productIds.length; i++) {

			if (quantities[i] <= 0) {

				throw new IllegalArgumentException("Quantity must be greater than zero.");
			}

			for (int j = i + 1; j < productIds.length; j++) {

				if (productIds[i] == productIds[j]) {

					throw new IllegalArgumentException("Duplicate product selected.");
				}
			}
		}

		Connection connection = null;

		try {

			// -------------------------------------------------
			// OPEN CONNECTION
			// -------------------------------------------------

			connection = DBConnection.getConnection();

			connection.setAutoCommit(false);

			// -------------------------------------------------
			// CLIENT ELIGIBILITY
			// ACTIVE + VERIFIED
			// -------------------------------------------------

			boolean eligible = clientEligibilityService.isEligible(connection, clientId);

			if (!eligible) {

				throw new IllegalStateException("Client must be ACTIVE and VERIFIED.");
			}

			// -------------------------------------------------
			// CALCULATE TOTALS
			// -------------------------------------------------

			BigDecimal subtotal = BigDecimal.ZERO;

			BigDecimal discountAmount = BigDecimal.ZERO;

			BigDecimal taxAmount = BigDecimal.ZERO;

			BigDecimal totalAmount = BigDecimal.ZERO;

			// Temporary arrays for calculated values

			BigDecimal[] unitPrices = new BigDecimal[productIds.length];

			BigDecimal[] discounts = new BigDecimal[productIds.length];

			BigDecimal[] taxes = new BigDecimal[productIds.length];

			BigDecimal[] lineTotals = new BigDecimal[productIds.length];

			int[] freeQuantities = new int[productIds.length];

			int[] totalQuantities = new int[productIds.length];

			Scheme[] schemes = new Scheme[productIds.length];

			// -------------------------------------------------
			// PROCESS PRODUCTS
			// -------------------------------------------------

			for (int i = 0; i < productIds.length; i++) {

				Product product = productDAO.getProductById(productIds[i]);

				if (product == null) {

					throw new IllegalArgumentException("Product not found: " + productIds[i]);
				}

				if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {

					throw new IllegalStateException("Product is not active: " + product.getProductName());
				}

				int quantity = quantities[i];

				BigDecimal unitPrice = product.getSellingPrice();

				BigDecimal lineSubtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

				BigDecimal discount = BigDecimal.ZERO;

				int freeQuantity = 0;

				// -------------------------------------------------
				// SCHEME
				// -------------------------------------------------

				Scheme scheme = schemeDAO.getActiveSchemeByProduct(product.getProductId());

				if (scheme != null) {

					schemes[i] = scheme;

					// BUY X GET Y FREE

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

								discount = lineSubtotal.multiply(percentage).divide(BigDecimal.valueOf(100));
							}
						}
					}
				}

				BigDecimal taxableAmount = lineSubtotal.subtract(discount);

				BigDecimal gstRate = product.getGstRate();

				if (gstRate == null) {

					gstRate = BigDecimal.ZERO;
				}

				BigDecimal tax = taxableAmount.multiply(gstRate).divide(BigDecimal.valueOf(100));

				BigDecimal lineTotal = taxableAmount.add(tax);

				int totalQuantity = quantity + freeQuantity;

				unitPrices[i] = unitPrice;

				discounts[i] = discount;

				taxes[i] = tax;

				lineTotals[i] = lineTotal;

				freeQuantities[i] = freeQuantity;

				totalQuantities[i] = totalQuantity;

				subtotal = subtotal.add(lineSubtotal);

				discountAmount = discountAmount.add(discount);

				taxAmount = taxAmount.add(tax);

				totalAmount = totalAmount.add(lineTotal);
			}

			// -------------------------------------------------
			// CREDIT CHECK
			// -------------------------------------------------

			if ("CREDIT".equals(paymentMethod)) {

				boolean sufficientCredit = creditService.hasSufficientCredit(connection, clientId, totalAmount);

				if (!sufficientCredit) {

					throw new IllegalStateException("Insufficient client credit.");
				}
			}

			// -------------------------------------------------
			// CREATE ORDER
			// -------------------------------------------------

			Order order = new Order();

			order.setClientId(clientId);

			order.setOrderStatus("COMPLETED");

			order.setSubtotal(subtotal);

			order.setDiscountAmount(discountAmount);

			order.setTaxAmount(taxAmount);

			order.setTotalAmount(totalAmount);

			if ("CREDIT".equals(paymentMethod)) {

				order.setCreditStatus("PENDING");

			} else {

				order.setCreditStatus("PAID");
			}

			order.setApprovedBy(null);

			order.setCreatedBy(createdBy);

			int orderId = orderDAO.addOrder(connection, order);

			if (orderId <= 0) {

				throw new IllegalStateException("Failed to create offline order.");
			}

			// -------------------------------------------------
			// MARK ORDER AS OFFLINE
			// -------------------------------------------------

			updateOrderAsOffline(connection, orderId);

			// -------------------------------------------------
			// CREATE ORDER ITEMS
			// -------------------------------------------------

			for (int i = 0; i < productIds.length; i++) {

				OrderItem item = new OrderItem();

				item.setOrderId(orderId);

				item.setProductId(productIds[i]);

				item.setOrderedQuantity(quantities[i]);

				item.setFreeQuantity(freeQuantities[i]);

				item.setTotalQuantity(totalQuantities[i]);

				item.setUnitPrice(unitPrices[i]);

				item.setDiscountAmount(discounts[i]);

				item.setTaxAmount(taxes[i]);

				item.setLineTotal(lineTotals[i]);

				if (schemes[i] != null) {

					item.setSchemeId(schemes[i].getSchemeId());

				} else {

					item.setSchemeId(null);
				}

				int orderItemId = orderItemDAO.addOrderItem(connection, item);

				if (orderItemId <= 0) {

					throw new IllegalStateException("Failed to create order item.");
				}

				// -------------------------------------------------
				// FEFO
				// -------------------------------------------------

				List<BatchAllocation> allocations = fefoService.allocateBatches(productIds[i], totalQuantities[i]);

				if (allocations == null || allocations.isEmpty()) {

					throw new IllegalStateException("No available stock for product ID: " + productIds[i]);
				}

				// Distribute free quantity

				fefoService.distributeFreeQuantity(allocations, freeQuantities[i]);

				// -------------------------------------------------
				// PROCESS BATCHES
				// -------------------------------------------------

				for (BatchAllocation allocation : allocations) {

					int allocatedQuantity = allocation.getAllocatedQuantity();

					if (allocatedQuantity <= 0) {
						continue;
					}

					// REDUCE INVENTORY

					boolean updated = inventoryDAO.reduceAvailableQuantity(connection, allocation.getBatchId(),
							allocatedQuantity);

					if (!updated) {

						throw new IllegalStateException("Insufficient stock for batch ID: " + allocation.getBatchId());
					}

					// SAVE BATCH ALLOCATION

					OrderItemBatch orderItemBatch = new OrderItemBatch();

					orderItemBatch.setOrderItemId(orderItemId);

					orderItemBatch.setBatchId(allocation.getBatchId());

					orderItemBatch.setAllocatedQuantity(allocatedQuantity);

					orderItemBatch.setFreeQuantity(allocation.getFreeQuantity());

					int batchId = orderItemBatchDAO.addOrderItemBatch(connection, orderItemBatch);

					if (batchId <= 0) {

						throw new IllegalStateException("Failed to save batch allocation.");
					}

					// STOCK OUT

					int transactionId = stockTransactionDAO.addTransaction(connection, allocation.getBatchId(), "OUT",
							allocatedQuantity, "OFFLINE_ORDER", orderId, "Offline sale stock issue", createdBy);

					if (transactionId <= 0) {

						throw new IllegalStateException("Failed to create stock transaction.");
					}
				}
			}

			// -------------------------------------------------
			// CREATE INVOICE
			// -------------------------------------------------

			int invoiceId = createInvoice(connection, orderId, subtotal, discountAmount, taxAmount, totalAmount, client,
					createdBy, paymentMethod);

			if (invoiceId <= 0) {

				throw new IllegalStateException("Failed to create invoice.");
			}

			// -------------------------------------------------
			// CASH / UPI / CARD
			// CREATE PAYMENT
			// -------------------------------------------------

			if (!"CREDIT".equals(paymentMethod)) {

				createPayment(connection, invoiceId, totalAmount, paymentMethod, transactionReference, createdBy);
			}

			// -------------------------------------------------
			// COMMIT
			// -------------------------------------------------

			connection.commit();

			return new OfflineSaleResult(orderId, invoiceId, totalAmount);

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
	// MARK ORDER AS OFFLINE
	// =========================================================

	private void updateOrderAsOffline(Connection connection, int orderId) throws Exception {

		String sql = "UPDATE orders SET " + "order_source = 'OFFLINE', " + "order_status = 'COMPLETED' "
				+ "WHERE order_id = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, orderId);

			statement.executeUpdate();
		}
	}

	// =========================================================
	// CREATE INVOICE
	// =========================================================

	private int createInvoice(Connection connection, int orderId, BigDecimal subtotal, BigDecimal discountAmount,
			BigDecimal taxAmount, BigDecimal totalAmount, Client client, int createdBy, String paymentMethod)
			throws Exception {

		String invoiceNumber = "INV-" + LocalDate.now().getYear() + "-" + String.format("%06d", orderId);

		LocalDate dueDate;

		if ("CREDIT".equals(paymentMethod)) {

			dueDate = LocalDate.now().plusDays(client.getCreditPeriodDays());

		} else {

			dueDate = LocalDate.now();
		}

		String invoiceStatus = "CREDIT".equals(paymentMethod) ? "PENDING" : "PAID";

		String sql = "INSERT INTO invoices " + "(order_id, invoice_number, invoice_date, "
				+ "subtotal, discount_amount, tax_amount, " + "total_amount, due_date, invoice_status, created_by) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

			statement.setInt(1, orderId);

			statement.setString(2, invoiceNumber);

			statement.setDate(3, Date.valueOf(LocalDate.now()));

			statement.setBigDecimal(4, subtotal);

			statement.setBigDecimal(5, discountAmount);

			statement.setBigDecimal(6, taxAmount);

			statement.setBigDecimal(7, totalAmount);

			statement.setDate(8, Date.valueOf(dueDate));

			statement.setString(9, invoiceStatus);

			statement.setInt(10, createdBy);

			int rows = statement.executeUpdate();

			if (rows == 0) {

				return 0;
			}

			try (ResultSet rs = statement.getGeneratedKeys()) {

				if (rs.next()) {

					return rs.getInt(1);
				}
			}
		}

		return 0;
	}

	// =========================================================
	// CREATE PAYMENT
	// =========================================================

	private void createPayment(Connection connection, int invoiceId, BigDecimal amount, String paymentMethod,
			String transactionReference, int recordedBy) throws Exception {

		String sql = "INSERT INTO payments " + "(invoice_id, payment_date, amount, "
				+ "payment_method, transaction_reference, " + "payment_status, recorded_by) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, invoiceId);

			statement.setDate(2, Date.valueOf(LocalDate.now()));

			statement.setBigDecimal(3, amount);

			statement.setString(4, paymentMethod);

			statement.setString(5, transactionReference);

			statement.setString(6, "SUCCESS");

			statement.setInt(7, recordedBy);

			int rows = statement.executeUpdate();

			if (rows == 0) {

				throw new IllegalStateException("Failed to create payment.");
			}
		}
	}

	// =========================================================
	// RESULT CLASS
	// =========================================================

	public static class OfflineSaleResult {

		private int orderId;
		private int invoiceId;
		private BigDecimal totalAmount;

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