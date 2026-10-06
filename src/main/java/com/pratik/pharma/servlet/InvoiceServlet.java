package com.pratik.pharma.servlet;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.pratik.pharma.dao.ClientDAO;
import com.pratik.pharma.dao.CompanyProfileDAO;
import com.pratik.pharma.dao.InvoiceDAO;
import com.pratik.pharma.dao.InvoiceItemDAO;
import com.pratik.pharma.dao.OrderDAO;
import com.pratik.pharma.model.Client;
import com.pratik.pharma.model.CompanyProfile;
import com.pratik.pharma.model.Invoice;
import com.pratik.pharma.model.InvoiceItem;
import com.pratik.pharma.model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/invoices")
public class InvoiceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// =========================================================
	// DAO OBJECTS
	// =========================================================

	private InvoiceDAO invoiceDAO;
	private InvoiceItemDAO invoiceItemDAO;
	private OrderDAO orderDAO;
	private ClientDAO clientDAO;
	private CompanyProfileDAO companyProfileDAO;

	// =========================================================
	// INIT
	// =========================================================

	@Override
	public void init() throws ServletException {

		invoiceDAO = new InvoiceDAO();
		invoiceItemDAO = new InvoiceItemDAO();
		orderDAO = new OrderDAO();
		clientDAO = new ClientDAO();
		companyProfileDAO = new CompanyProfileDAO();
	}

	// =========================================================
	// GET
	// =========================================================

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		// -----------------------------------------------------
		// DEFAULT / LIST
		// -----------------------------------------------------

		if (action == null || action.equals("list")) {

			listInvoices(request, response);

			return;
		}

		// -----------------------------------------------------
		// GENERATE
		// -----------------------------------------------------

		if (action.equals("generate")) {

			generateInvoice(request, response);

			return;
		}

		// -----------------------------------------------------
		// VIEW
		// -----------------------------------------------------

		if (action.equals("view")) {

			viewInvoice(request, response);

			return;
		}

		// -----------------------------------------------------
		// INVALID ACTION
		// -----------------------------------------------------

		response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid invoice action.");
	}

	// =========================================================
	// LIST INVOICES
	// =========================================================

	private void listInvoices(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Invoice> invoices = invoiceDAO.getAllInvoices();

		request.setAttribute("invoices", invoices);

		request.getRequestDispatcher("invoice-list.jsp").forward(request, response);
	}

	// =========================================================
	// GENERATE INVOICE
	// =========================================================

	private void generateInvoice(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String orderIdParameter = request.getParameter("orderId");

		// -----------------------------------------------------
		// VALIDATE ORDER ID
		// -----------------------------------------------------

		if (orderIdParameter == null || orderIdParameter.isBlank()) {

			request.setAttribute("error", "Order ID is required.");

			request.getRequestDispatcher("invoice-list.jsp").forward(request, response);

			return;
		}

		int orderId;

		try {

			orderId = Integer.parseInt(orderIdParameter);

		} catch (NumberFormatException e) {

			request.setAttribute("error", "Invalid Order ID.");

			request.getRequestDispatcher("invoice-list.jsp").forward(request, response);

			return;
		}

		// -----------------------------------------------------
		// CHECK EXISTING INVOICE
		// -----------------------------------------------------

		Invoice existingInvoice = invoiceDAO.getInvoiceByOrderId(orderId);

		if (existingInvoice != null) {

			response.sendRedirect("invoices?action=view&id=" + existingInvoice.getInvoiceId());

			return;
		}

		// -----------------------------------------------------
		// GET ORDER
		// -----------------------------------------------------

		Order order = orderDAO.getOrderById(orderId);

		if (order == null) {

			request.setAttribute("error", "Order not found.");

			request.getRequestDispatcher("invoice-list.jsp").forward(request, response);

			return;
		}

		// -----------------------------------------------------
		// GET CLIENT
		// -----------------------------------------------------

		Client client = clientDAO.getClientById(order.getClientId());

		if (client == null) {

			request.setAttribute("error", "Client not found.");

			request.getRequestDispatcher("invoice-list.jsp").forward(request, response);

			return;
		}

		// -----------------------------------------------------
		// CHECK LOGIN
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

			request.setAttribute("error", "Unable to generate invoice.");

			request.getRequestDispatcher("invoice-list.jsp").forward(request, response);

			return;
		}

		// -----------------------------------------------------
		// LOAD CREATED INVOICE
		// -----------------------------------------------------

		Invoice savedInvoice = invoiceDAO.getInvoiceByOrderId(orderId);

		if (savedInvoice != null) {

			response.sendRedirect("invoices?action=view&id=" + savedInvoice.getInvoiceId());

		} else {

			request.setAttribute("error", "Invoice created but could not be loaded.");

			request.getRequestDispatcher("invoice-list.jsp").forward(request, response);
		}
	}

	// =========================================================
	// VIEW INVOICE
	// =========================================================

	private void viewInvoice(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idParameter = request.getParameter("id");

		// -----------------------------------------------------
		// VALIDATE INVOICE ID
		// -----------------------------------------------------

		if (idParameter == null || idParameter.isBlank()) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invoice ID is required.");

			return;
		}

		int invoiceId;

		try {

			invoiceId = Integer.parseInt(idParameter);

		} catch (NumberFormatException e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Invoice ID.");

			return;
		}

		// -----------------------------------------------------
		// GET INVOICE
		// -----------------------------------------------------

		Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);

		if (invoice == null) {

			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invoice not found.");

			return;
		}

		// -----------------------------------------------------
		// GET ORDER
		// -----------------------------------------------------

		Order order = orderDAO.getOrderById(invoice.getOrderId());

		if (order == null) {

			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order associated with invoice not found.");

			return;
		}

		// -----------------------------------------------------
		// GET CLIENT
		// -----------------------------------------------------

		Client client = clientDAO.getClientById(order.getClientId());

		if (client == null) {

			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Client associated with invoice not found.");

			return;
		}

		// -----------------------------------------------------
		// GET COMPANY PROFILE
		// -----------------------------------------------------

		CompanyProfile companyProfile = companyProfileDAO.getCompanyProfile();

		if (companyProfile == null) {

			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Company profile not found.");

			return;
		}

		// -----------------------------------------------------
		// GET INVOICE ITEMS
		// -----------------------------------------------------

		List<InvoiceItem> invoiceItems = invoiceItemDAO.getInvoiceItemsByOrderId(invoice.getOrderId());

		// -----------------------------------------------------
		// SEND DATA TO JSP
		// -----------------------------------------------------

		request.setAttribute("invoice", invoice);

		request.setAttribute("order", order);

		request.setAttribute("client", client);

		request.setAttribute("companyProfile", companyProfile);

		request.setAttribute("invoiceItems", invoiceItems);

		// -----------------------------------------------------
		// FORWARD TO INVOICE PAGE
		// -----------------------------------------------------

		request.getRequestDispatcher("invoice.jsp").forward(request, response);
	}
}