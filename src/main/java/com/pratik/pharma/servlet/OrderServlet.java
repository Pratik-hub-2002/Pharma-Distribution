package com.pratik.pharma.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pratik.pharma.dao.BatchDAO;
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

import com.pratik.pharma.service.ClientEligibilityService;
import com.pratik.pharma.service.CreditService;
import com.pratik.pharma.service.FEFOService;

import com.pratik.pharma.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// =========================================================
	// DAOs
	// =========================================================

	private OrderDAO orderDAO;
	private OrderItemDAO orderItemDAO;
	private ClientDAO clientDAO;
	private ProductDAO productDAO;
	private SchemeDAO schemeDAO;
	private BatchDAO batchDAO;
	private InventoryDAO inventoryDAO;
	private OrderItemBatchDAO orderItemBatchDAO;
	private StockTransactionDAO stockTransactionDAO;

	// =========================================================
	// SERVICES
	// =========================================================

	private ClientEligibilityService clientEligibilityService;
	private CreditService creditService;
	private FEFOService fefoService;

	// =========================================================
	// INIT
	// =========================================================

	@Override
	public void init() throws ServletException {

		System.out.println("======================================");
		System.out.println("OrderServlet INITIALIZED");
		System.out.println("======================================");

		orderDAO = new OrderDAO();
		orderItemDAO = new OrderItemDAO();

		clientDAO = new ClientDAO();
		productDAO = new ProductDAO();
		schemeDAO = new SchemeDAO();

		batchDAO = new BatchDAO();
		inventoryDAO = new InventoryDAO();
		orderItemBatchDAO = new OrderItemBatchDAO();
		stockTransactionDAO = new StockTransactionDAO();

		clientEligibilityService = new ClientEligibilityService();

		creditService = new CreditService();

		fefoService = new FEFOService();
	}

	// =========================================================
	// GET
	// =========================================================

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if (action == null || action.isBlank()) {

			action = "list";
		}

		switch (action) {

		case "add":

			showAddOrderForm(request, response);

			break;

		case "view":

			viewOrder(request, response);

			break;

		default:

			listOrders(request, response);

			break;
		}
	}

	// =========================================================
	// LIST ORDERS
	// =========================================================

	private void listOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Order> orders = orderDAO.getAllOrders();

		request.setAttribute("orders", orders);

		request.getRequestDispatcher("/orders.jsp").forward(request, response);
	}

	// =========================================================
	// ADD ORDER FORM
	// =========================================================

	private void showAddOrderForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Client> clients = clientDAO.getAllClients();

		List<Product> products = productDAO.getAllProducts();

		request.setAttribute("clients", clients);

		request.setAttribute("products", products);

		// -----------------------------------------------------
		// ACTIVE SCHEMES
		// -----------------------------------------------------

		Map<Integer, Scheme> activeSchemes = new HashMap<>();

		for (Product product : products) {

			Scheme scheme = schemeDAO.getActiveSchemeByProduct(product.getProductId());

			if (scheme != null) {

				activeSchemes.put(product.getProductId(), scheme);
			}
		}

		request.setAttribute("activeSchemes", activeSchemes);

		request.getRequestDispatcher("/order-form.jsp").forward(request, response);
	}

	// =========================================================
	// VIEW ORDER
	// =========================================================

	private void viewOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String orderIdParameter = request.getParameter("id");

		if (orderIdParameter == null || orderIdParameter.isBlank()) {

			response.sendRedirect(request.getContextPath() + "/orders");

			return;
		}

		try {

			int orderId = Integer.parseInt(orderIdParameter);

			Order order = orderDAO.getOrderById(orderId);

			if (order == null) {

				response.sendRedirect(request.getContextPath() + "/orders");

				return;
			}

			List<OrderItem> items = orderItemDAO.getItemsByOrderId(orderId);

			request.setAttribute("order", order);

			request.setAttribute("items", items);

			request.getRequestDispatcher("/order-view.jsp").forward(request, response);

		} catch (NumberFormatException e) {

			response.sendRedirect(request.getContextPath() + "/orders");
		}
	}

	// =========================================================
	// POST
	// =========================================================

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if ("create".equals(action)) {

			createOrder(request, response);

		} else {

			response.sendRedirect(request.getContextPath() + "/orders");
		}
	}

	// =========================================================
	// CREATE MULTI-ITEM ORDER
	// =========================================================

	private void createOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// =====================================================
		// LOGIN CHECK
		// =====================================================

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("userId") == null) {

			response.sendRedirect(request.getContextPath() + "/login.jsp");

			return;
		}

		Connection connection = null;

		try {

			// =================================================
			// LOGGED-IN USER
			// =================================================

			int createdBy = (Integer) session.getAttribute("userId");

			// =================================================
			// CLIENT
			// =================================================

			String clientParameter = request.getParameter("clientId");

			if (clientParameter == null || clientParameter.isBlank()) {

				throw new IllegalStateException("Customer is required.");
			}

			int clientId = Integer.parseInt(clientParameter);

			Client client = clientDAO.getClientById(clientId);

			if (client == null) {

				throw new IllegalStateException("Selected customer was not found.");
			}

			// =================================================
			// MULTIPLE PRODUCTS
			// =================================================

			String[] productParameters = request.getParameterValues("productId");

			String[] quantityParameters = request.getParameterValues("quantity");

			if (productParameters == null || quantityParameters == null) {

				throw new IllegalStateException("At least one product is required.");
			}

			if (productParameters.length == 0 || productParameters.length != quantityParameters.length) {

				throw new IllegalStateException("Invalid order item data.");
			}

			// =================================================
			// PREVENT DUPLICATE PRODUCTS
			// =================================================

			Set<Integer> productIds = new HashSet<>();

			// =================================================
			// TEMPORARY ITEM DATA
			// =================================================

			List<OrderItemData> orderItems = new ArrayList<>();

			// =================================================
			// ORDER TOTALS
			// =================================================

			BigDecimal orderSubtotal = BigDecimal.ZERO;

			BigDecimal orderDiscount = BigDecimal.ZERO;

			BigDecimal orderTax = BigDecimal.ZERO;

			BigDecimal orderTotal = BigDecimal.ZERO;

			// =================================================
			// PROCESS EACH PRODUCT
			// =================================================

			for (int i = 0; i < productParameters.length; i++) {

				// -------------------------------------------------
				// PRODUCT ID
				// -------------------------------------------------

				if (productParameters[i] == null || productParameters[i].isBlank()) {

					throw new IllegalStateException("Product is required.");
				}

				int productId = Integer.parseInt(productParameters[i]);

				// -------------------------------------------------
				// DUPLICATE PRODUCT CHECK
				// -------------------------------------------------

				if (!productIds.add(productId)) {

					throw new IllegalStateException("The same product cannot be added twice.");
				}

				// -------------------------------------------------
				// QUANTITY
				// -------------------------------------------------

				if (quantityParameters[i] == null || quantityParameters[i].isBlank()) {

					throw new IllegalStateException("Quantity is required.");
				}

				int quantity = Integer.parseInt(quantityParameters[i]);

				if (quantity <= 0) {

					throw new IllegalStateException("Quantity must be greater than 0.");
				}

				// -------------------------------------------------
				// GET PRODUCT
				// -------------------------------------------------

				Product product = productDAO.getProductById(productId);

				if (product == null) {

					throw new IllegalStateException("Product ID " + productId + " was not found.");
				}

				// -------------------------------------------------
				// PRODUCT STATUS
				// -------------------------------------------------

				if (product.getStatus() != null && !"ACTIVE".equalsIgnoreCase(product.getStatus())) {

					throw new IllegalStateException("Product " + product.getProductName() + " is not active.");
				}

				// -------------------------------------------------
				// SELLING PRICE
				// -------------------------------------------------

				BigDecimal unitPrice = product.getSellingPrice();

				if (unitPrice == null) {

					throw new IllegalStateException("Selling price is missing for " + product.getProductName());
				}

				// -------------------------------------------------
				// SUBTOTAL
				// -------------------------------------------------

				BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

				// -------------------------------------------------
				// SCHEME VALUES
				// -------------------------------------------------

				BigDecimal discountAmount = BigDecimal.ZERO;

				int freeQuantity = 0;

				// -------------------------------------------------
				// ACTIVE SCHEME
				// -------------------------------------------------

				Scheme scheme = schemeDAO.getActiveSchemeByProduct(productId);

				if (scheme != null) {

					// =============================================
					// BUY X GET Y FREE
					// =============================================

					if ("BUY_GET_FREE".equalsIgnoreCase(scheme.getSchemeType())) {

						int minimumQuantity = scheme.getMinimumQuantity();

						int schemeFreeQuantity = scheme.getFreeQuantity();

						if (minimumQuantity > 0 && quantity >= minimumQuantity) {

							freeQuantity = (quantity / minimumQuantity) * schemeFreeQuantity;
						}
					}

					// =============================================
					// PERCENTAGE DISCOUNT
					// =============================================

					else if ("PERCENTAGE_DISCOUNT".equalsIgnoreCase(scheme.getSchemeType())) {

						int minimumQuantity = scheme.getMinimumQuantity();

						if (quantity >= minimumQuantity) {

							BigDecimal percentage = scheme.getDiscountPercentage();

							if (percentage != null) {

								discountAmount = subtotal.multiply(percentage).divide(BigDecimal.valueOf(100));
							}
						}
					}
				}

				// -------------------------------------------------
				// TAXABLE AMOUNT
				// -------------------------------------------------

				BigDecimal taxableAmount = subtotal.subtract(discountAmount);

				// -------------------------------------------------
				// GST
				// -------------------------------------------------

				BigDecimal gstRate = product.getGstRate();

				if (gstRate == null) {

					gstRate = BigDecimal.ZERO;
				}

				BigDecimal taxAmount = taxableAmount.multiply(gstRate).divide(BigDecimal.valueOf(100));

				// -------------------------------------------------
				// LINE TOTAL
				// -------------------------------------------------

				BigDecimal lineTotal = taxableAmount.add(taxAmount);

				// -------------------------------------------------
				// TOTAL PHYSICAL QUANTITY
				// -------------------------------------------------

				int totalQuantity = quantity + freeQuantity;

				// -------------------------------------------------
				// ADD TO ORDER TOTALS
				// -------------------------------------------------

				orderSubtotal = orderSubtotal.add(subtotal);

				orderDiscount = orderDiscount.add(discountAmount);

				orderTax = orderTax.add(taxAmount);

				orderTotal = orderTotal.add(lineTotal);

				// -------------------------------------------------
				// STORE ITEM TEMPORARILY
				// -------------------------------------------------

				OrderItemData itemData = new OrderItemData();

				itemData.productId = productId;

				itemData.quantity = quantity;

				itemData.freeQuantity = freeQuantity;

				itemData.totalQuantity = totalQuantity;

				itemData.unitPrice = unitPrice;

				itemData.discountAmount = discountAmount;

				itemData.taxAmount = taxAmount;

				itemData.lineTotal = lineTotal;

				itemData.scheme = scheme;

				orderItems.add(itemData);
			}

			// =====================================================
			// DATABASE TRANSACTION
			// =====================================================

			connection = DBConnection.getConnection();

			connection.setAutoCommit(false);

			// =====================================================
			// CLIENT ELIGIBILITY
			// =====================================================

			boolean clientEligible = clientEligibilityService.isEligible(connection, clientId);

			if (!clientEligible) {

				throw new IllegalStateException("Client is not ACTIVE and VERIFIED.");
			}

			// =====================================================
			// CREDIT CHECK
			// =====================================================

			boolean sufficientCredit = creditService.hasSufficientCredit(connection, clientId, orderTotal);

			if (!sufficientCredit) {

				throw new IllegalStateException("Insufficient client credit.");
			}

			// =====================================================
			// CREATE ORDER
			// =====================================================

			Order order = new Order();

			order.setClientId(clientId);

			order.setOrderStatus("PENDING");

			order.setSubtotal(orderSubtotal);

			order.setDiscountAmount(orderDiscount);

			order.setTaxAmount(orderTax);

			order.setTotalAmount(orderTotal);

			order.setCreditStatus("PENDING");

			order.setApprovedBy(null);

			order.setCreatedBy(createdBy);

			int orderId = orderDAO.addOrder(connection, order);

			if (orderId <= 0) {

				throw new IllegalStateException("Failed to create order.");
			}

			// =====================================================
			// CREATE EVERY ORDER ITEM
			// =====================================================

			for (OrderItemData itemData : orderItems) {

				// -------------------------------------------------
				// ORDER ITEM
				// -------------------------------------------------

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

				// -------------------------------------------------
				// SAVE ORDER ITEM
				// -------------------------------------------------

				int orderItemId = orderItemDAO.addOrderItem(connection, item);

				if (orderItemId <= 0) {

					throw new IllegalStateException("Failed to create order item.");
				}

				// =================================================
				// FEFO
				// =================================================

				List<BatchAllocation> allocations = fefoService.allocateBatches(itemData.productId,
						itemData.totalQuantity);

				if (allocations == null || allocations.isEmpty()) {

					throw new IllegalStateException("No available stock for product ID: " + itemData.productId);
				}

				// -------------------------------------------------
				// DISTRIBUTE FREE QUANTITY
				// -------------------------------------------------

				fefoService.distributeFreeQuantity(allocations, itemData.freeQuantity);

				// =================================================
				// PROCESS EACH BATCH
				// =================================================

				for (BatchAllocation allocation : allocations) {

					int allocatedQuantity = allocation.getAllocatedQuantity();

					if (allocatedQuantity <= 0) {

						continue;
					}

					// ---------------------------------------------
					// REDUCE INVENTORY
					// ---------------------------------------------

					boolean inventoryUpdated = inventoryDAO.reduceAvailableQuantity(connection, allocation.getBatchId(),
							allocatedQuantity);

					if (!inventoryUpdated) {

						throw new IllegalStateException("Insufficient stock for batch ID: " + allocation.getBatchId());
					}

					// ---------------------------------------------
					// ORDER ITEM BATCH
					// ---------------------------------------------

					OrderItemBatch orderItemBatch = new OrderItemBatch();

					orderItemBatch.setOrderItemId(orderItemId);

					orderItemBatch.setBatchId(allocation.getBatchId());

					orderItemBatch.setAllocatedQuantity(allocatedQuantity);

					orderItemBatch.setFreeQuantity(allocation.getFreeQuantity());

					int orderItemBatchId = orderItemBatchDAO.addOrderItemBatch(connection, orderItemBatch);

					if (orderItemBatchId <= 0) {

						throw new IllegalStateException("Failed to save batch allocation.");
					}

					// ---------------------------------------------
					// STOCK OUT TRANSACTION
					// ---------------------------------------------

					int transactionId = stockTransactionDAO.addTransaction(connection, allocation.getBatchId(), "OUT",
							allocatedQuantity, "ORDER", orderId, "Stock issued against order", createdBy);

					if (transactionId <= 0) {

						throw new IllegalStateException("Failed to create stock transaction.");
					}
				}
			}

			// =====================================================
			// COMMIT
			// =====================================================

			connection.commit();

			System.out.println("======================================");

			System.out.println("MULTI-ITEM ORDER CREATED");

			System.out.println("Order ID = " + orderId);

			System.out.println("Customer = " + client.getClientName());

			System.out.println("Items = " + orderItems.size());

			System.out.println("Grand Total = ₹" + orderTotal);

			System.out.println("======================================");

			session.setAttribute("orderSuccess",
					"Order #" + orderId + " created successfully with " + orderItems.size() + " product(s).");

			response.sendRedirect(request.getContextPath() + "/orders?action=view&id=" + orderId);

		} catch (NumberFormatException e) {

			rollback(connection);

			e.printStackTrace();

			session.setAttribute("orderError", "Invalid numeric value entered.");

			response.sendRedirect(request.getContextPath() + "/orders?action=add");

		} catch (Exception e) {

			rollback(connection);

			e.printStackTrace();

			String message = e.getMessage();

			if (message == null || message.isBlank()) {

				message = "Order creation failed. Check Tomcat console.";
			}

			session.setAttribute("orderError", message);

			response.sendRedirect(request.getContextPath() + "/orders?action=add");

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
	// ROLLBACK
	// =========================================================

	private void rollback(Connection connection) {

		if (connection != null) {

			try {

				connection.rollback();

				System.out.println("Order transaction rolled back.");

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	// =========================================================
	// TEMPORARY ORDER ITEM DATA
	// =========================================================

	private static class OrderItemData {

		int productId;

		int quantity;

		int freeQuantity;

		int totalQuantity;

		BigDecimal unitPrice;

		BigDecimal discountAmount;

		BigDecimal taxAmount;

		BigDecimal lineTotal;

		Scheme scheme;
	}
}