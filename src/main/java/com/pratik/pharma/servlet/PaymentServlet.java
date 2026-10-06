package com.pratik.pharma.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.pratik.pharma.dao.InvoiceDAO;
import com.pratik.pharma.dao.PaymentDAO;
import com.pratik.pharma.model.Invoice;
import com.pratik.pharma.model.Payment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/payments")
public class PaymentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private PaymentDAO paymentDAO;
	private InvoiceDAO invoiceDAO;

	// =========================================================
	// INIT
	// =========================================================

	@Override
	public void init() throws ServletException {

		paymentDAO = new PaymentDAO();

		invoiceDAO = new InvoiceDAO();
	}

	// =========================================================
	// GET
	// =========================================================

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if (action == null || action.equals("list")) {

			listPayments(request, response);

			return;
		}

		if (action.equals("add")) {

			showPaymentForm(request, response);

			return;
		}

		if (action.equals("view")) {

			viewPayment(request, response);

			return;
		}

		response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid payment action.");
	}

	// =========================================================
	// POST
	// =========================================================

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if ("create".equals(action)) {

			createPayment(request, response);

			return;
		}

		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid payment request.");
	}

	// =========================================================
	// LIST
	// =========================================================

	private void listPayments(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Payment> payments = paymentDAO.getAllPayments();

		request.setAttribute("payments", payments);

		request.getRequestDispatcher("payment-list.jsp").forward(request, response);
	}

	// =========================================================
	// SHOW FORM
	// =========================================================

	private void showPaymentForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String invoiceIdParameter = request.getParameter("invoiceId");

		if (invoiceIdParameter != null && !invoiceIdParameter.isBlank()) {

			try {

				int invoiceId = Integer.parseInt(invoiceIdParameter);

				Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);

				request.setAttribute("selectedInvoice", invoice);

			} catch (NumberFormatException e) {

				request.setAttribute("error", "Invalid invoice ID.");
			}
		}

		request.getRequestDispatcher("payment-form.jsp").forward(request, response);
	}

	// =========================================================
	// CREATE PAYMENT
	// =========================================================

	private void createPayment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("userId") == null) {

			response.sendRedirect("login.jsp");

			return;
		}

		String invoiceIdParameter = request.getParameter("invoiceId");

		String amountParameter = request.getParameter("amount");

		String paymentMethod = request.getParameter("paymentMethod");

		String transactionReference = request.getParameter("transactionReference");

		// -----------------------------------------------------
		// VALIDATE INVOICE
		// -----------------------------------------------------

		if (invoiceIdParameter == null || invoiceIdParameter.isBlank()) {

			request.setAttribute("error", "Invoice is required.");

			showPaymentForm(request, response);

			return;
		}

		int invoiceId;

		try {

			invoiceId = Integer.parseInt(invoiceIdParameter);

		} catch (NumberFormatException e) {

			request.setAttribute("error", "Invalid invoice ID.");

			showPaymentForm(request, response);

			return;
		}

		Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);

		if (invoice == null) {

			request.setAttribute("error", "Invoice not found.");

			showPaymentForm(request, response);

			return;
		}

		// -----------------------------------------------------
		// VALIDATE AMOUNT
		// -----------------------------------------------------

		BigDecimal amount;

		try {

			amount = new BigDecimal(amountParameter);

		} catch (Exception e) {

			request.setAttribute("error", "Invalid payment amount.");

			request.setAttribute("selectedInvoice", invoice);

			request.getRequestDispatcher("payment-form.jsp").forward(request, response);

			return;
		}

		if (amount.compareTo(BigDecimal.ZERO) <= 0) {

			request.setAttribute("error", "Payment amount must be greater than zero.");

			request.setAttribute("selectedInvoice", invoice);

			request.getRequestDispatcher("payment-form.jsp").forward(request, response);

			return;
		}

		// -----------------------------------------------------
		// CHECK OUTSTANDING
		// -----------------------------------------------------

		BigDecimal paidAmount = paymentDAO.getPaidAmountForInvoice(invoiceId);

		BigDecimal outstanding = invoice.getTotalAmount().subtract(paidAmount);

		if (outstanding.compareTo(BigDecimal.ZERO) < 0) {

			outstanding = BigDecimal.ZERO;
		}

		if (amount.compareTo(outstanding) > 0) {

			request.setAttribute("error", "Payment amount ₹" + amount + " exceeds outstanding amount ₹" + outstanding);

			request.setAttribute("selectedInvoice", invoice);

			request.getRequestDispatcher("payment-form.jsp").forward(request, response);

			return;
		}

		// -----------------------------------------------------
		// PAYMENT STATUS
		// -----------------------------------------------------

		String paymentStatus = "SUCCESS";

		// -----------------------------------------------------
		// CREATE PAYMENT
		// -----------------------------------------------------

		Payment payment = new Payment();

		payment.setInvoiceId(invoiceId);

		payment.setPaymentDate(Date.valueOf(LocalDate.now()));

		payment.setAmount(amount);

		payment.setPaymentMethod(paymentMethod);

		payment.setTransactionReference(transactionReference);

		payment.setPaymentStatus(paymentStatus);

		payment.setRecordedBy((Integer) session.getAttribute("userId"));

		boolean created = paymentDAO.addPayment(payment);

		if (created) {

			response.sendRedirect("payments?action=list");

		} else {

			request.setAttribute("error", "Unable to record payment.");

			request.setAttribute("selectedInvoice", invoice);

			request.getRequestDispatcher("payment-form.jsp").forward(request, response);
		}
	}

	// =========================================================
	// VIEW PAYMENT
	// =========================================================

	private void viewPayment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idParameter = request.getParameter("id");

		if (idParameter == null || idParameter.isBlank()) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Payment ID is required.");

			return;
		}

		int paymentId;

		try {

			paymentId = Integer.parseInt(idParameter);

		} catch (NumberFormatException e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid payment ID.");

			return;
		}

		Payment payment = paymentDAO.getPaymentById(paymentId);

		if (payment == null) {

			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Payment not found.");

			return;
		}

		request.setAttribute("payment", payment);

		request.getRequestDispatcher("payment-view.jsp").forward(request, response);
	}
}