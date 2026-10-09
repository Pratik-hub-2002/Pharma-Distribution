package com.pratik.pharma.servlet;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.pratik.pharma.dao.AccountantBillingDAO;
import com.pratik.pharma.dao.ClientDAO;
import com.pratik.pharma.dao.InvoiceDAO;
import com.pratik.pharma.model.Client;
import com.pratik.pharma.model.Invoice;
import com.pratik.pharma.model.Order;
import com.pratik.pharma.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/accountant-billing")
public class AccountantBillingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private AccountantBillingDAO accountantBillingDAO;
	private InvoiceDAO invoiceDAO;
	private ClientDAO clientDAO;

	// =========================================================
	// INIT
	// =========================================================

	@Override
	public void init() throws ServletException {

		accountantBillingDAO = new AccountantBillingDAO();

		invoiceDAO = new InvoiceDAO();

		clientDAO = new ClientDAO();
	}

	// =========================================================
	// GET
	// =========================================================

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (!hasAccountantAccess(request)) {

			response.sendError(HttpServletResponse.SC_FORBIDDEN,
					"You do not have permission to access Accountant Billing.");

			return;
		}

		String action = request.getParameter("action");

		// -----------------------------------------------------
		// CREATE INVOICE
		// -----------------------------------------------------

		if ("create".equalsIgnoreCase(action)) {

			createInvoice(request, response);

			return;
		}

		// -----------------------------------------------------
		// DEFAULT
		// -----------------------------------------------------

		showBillingPage(request, response);
	}

	// =========================================================
	// SHOW BILLING PAGE
	// =========================================================

	private void showBillingPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Order> orders = accountantBillingDAO.getOrdersReadyForBilling();

		request.setAttribute("orders", orders);

		request.getRequestDispatcher("/accountant-billing.jsp").forward(request, response);
	}

	// =========================================================
	// CREATE INVOICE
	// =========================================================

	private void createInvoice(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String orderIdParameter = request.getParameter("orderId");

		if (orderIdParameter == null || orderIdParameter.isBlank()) {

			request.getSession().setAttribute("errorMessage", "Order ID is required.");

			response.sendRedirect("accountant-billing");

			return;
		}

		int orderId;

		try {

			orderId = Integer.parseInt(orderIdParameter);

		} catch (NumberFormatException e) {

			request.getSession().setAttribute("errorMessage", "Invalid Order ID.");

			response.sendRedirect("accountant-billing");

			return;
		}

		// -----------------------------------------------------
		// GET APPROVED ORDER
		// -----------------------------------------------------

		Order order = accountantBillingDAO.getApprovedOrderById(orderId);

		if (order == null) {

			request.getSession().setAttribute("errorMessage", "Order #" + orderId + " is not eligible for billing.");

			response.sendRedirect("accountant-billing");

			return;
		}

		// -----------------------------------------------------
		// CHECK EXISTING INVOICE
		// -----------------------------------------------------

		Invoice existingInvoice = invoiceDAO.getInvoiceByOrderId(orderId);

		if (existingInvoice != null) {

			request.getSession().setAttribute("errorMessage", "Invoice already exists for Order #" + orderId + ".");

			response.sendRedirect("invoices?action=view&id=" + existingInvoice.getInvoiceId());

			return;
		}

		// -----------------------------------------------------
		// GET CLIENT
		// -----------------------------------------------------

		Client client = clientDAO.getClientById(order.getClientId());

		if (client == null) {

			request.getSession().setAttribute("errorMessage", "Client not found for Order #" + orderId + ".");

			response.sendRedirect("accountant-billing");

			return;
		}

		// -----------------------------------------------------
		// GET LOGGED-IN USER
		// -----------------------------------------------------

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("userId") == null) {

			response.sendRedirect("login.jsp");

			return;
		}

		int createdBy = (Integer) session.getAttribute("userId");

		// -----------------------------------------------------
		// GENERATE INVOICE NUMBER
		// -----------------------------------------------------

		String invoiceNumber = "INV-" + LocalDate.now().getYear() + "-" + String.format("%06d", orderId);

		// -----------------------------------------------------
		// INVOICE DATE
		// -----------------------------------------------------

		Date invoiceDate = Date.valueOf(LocalDate.now());

		// -----------------------------------------------------
		// DUE DATE
		// -----------------------------------------------------

		Date dueDate = null;

		if (client.getCreditPeriodDays() > 0) {

			dueDate = Date.valueOf(LocalDate.now().plusDays(client.getCreditPeriodDays()));
		}

		// -----------------------------------------------------
		// CREATE INVOICE OBJECT
		// -----------------------------------------------------

		Invoice invoice = new Invoice();

		invoice.setOrderId(order.getOrderId());

		invoice.setInvoiceNumber(invoiceNumber);

		invoice.setInvoiceDate(invoiceDate);

		invoice.setSubtotal(order.getSubtotal());

		invoice.setDiscountAmount(order.getDiscountAmount());

		invoice.setTaxAmount(order.getTaxAmount());

		invoice.setTotalAmount(order.getTotalAmount());

		invoice.setDueDate(dueDate);

		invoice.setInvoiceStatus("PENDING");

		invoice.setCreatedBy(createdBy);

		// -----------------------------------------------------
		// SAVE INVOICE
		// -----------------------------------------------------

		boolean created = invoiceDAO.createInvoice(invoice);

		if (!created) {

			request.getSession().setAttribute("errorMessage", "Unable to create invoice for Order #" + orderId + ".");

			response.sendRedirect("accountant-billing");

			return;
		}

		// -----------------------------------------------------
		// GET CREATED INVOICE
		// -----------------------------------------------------

		Invoice savedInvoice = invoiceDAO.getInvoiceByOrderId(orderId);

		if (savedInvoice != null) {

			request.getSession().setAttribute("successMessage",
					"Invoice " + savedInvoice.getInvoiceNumber() + " created successfully for Order #" + orderId + ".");

			response.sendRedirect("invoices?action=view&id=" + savedInvoice.getInvoiceId());

		} else {

			request.getSession().setAttribute("errorMessage", "Invoice was created but could not be loaded.");

			response.sendRedirect("accountant-billing");
		}
	}

	// =========================================================
	// ACCOUNTANT ACCESS
	// =========================================================

	private boolean hasAccountantAccess(HttpServletRequest request) {

		HttpSession session = request.getSession(false);

		if (session == null) {

			return false;
		}

		Object loggedInUser = session.getAttribute("loggedInUser");

		if (!(loggedInUser instanceof User)) {

			return false;
		}

		User user = (User) loggedInUser;

		String roleName = user.getRoleName();

		if (roleName == null) {

			return false;
		}

		return "SUPER_ADMIN".equalsIgnoreCase(roleName) || "ACCOUNTANT".equalsIgnoreCase(roleName);
	}
}