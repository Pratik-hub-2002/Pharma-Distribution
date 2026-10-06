package com.pratik.pharma.servlet;

import java.io.IOException;
import java.util.List;

import com.pratik.pharma.dao.ClientDAO;
import com.pratik.pharma.dao.ProductDAO;
import com.pratik.pharma.model.Client;
import com.pratik.pharma.model.Product;
import com.pratik.pharma.service.OfflineSaleService;
import com.pratik.pharma.service.OfflineSaleService.OfflineSaleResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/offline-sale")
public class OfflineSaleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ClientDAO clientDAO;
	private ProductDAO productDAO;
	private OfflineSaleService offlineSaleService;

	// =========================================================
	// INIT
	// =========================================================

	@Override
	public void init() throws ServletException {

		clientDAO = new ClientDAO();

		productDAO = new ProductDAO();

		offlineSaleService = new OfflineSaleService();

		System.out.println("======================================");

		System.out.println("OfflineSaleServlet INITIALIZED");

		System.out.println("======================================");
	}

	// =========================================================
	// GET
	// =========================================================

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("userId") == null) {

			response.sendRedirect(request.getContextPath() + "/login.jsp");

			return;
		}

		List<Client> clients = clientDAO.getAllClients();

		List<Product> products = productDAO.getAllProducts();

		request.setAttribute("clients", clients);

		request.setAttribute("products", products);

		request.getRequestDispatcher("/offline-sale.jsp").forward(request, response);
	}

	// =========================================================
	// POST
	// =========================================================

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("userId") == null) {

			response.sendRedirect(request.getContextPath() + "/login.jsp");

			return;
		}

		try {

			// -------------------------------------------------
			// USER
			// -------------------------------------------------

			int createdBy = (Integer) session.getAttribute("userId");

			// -------------------------------------------------
			// CLIENT
			// -------------------------------------------------

			int clientId = Integer.parseInt(request.getParameter("clientId"));

			// -------------------------------------------------
			// PRODUCTS
			// -------------------------------------------------

			String[] productValues = request.getParameterValues("productId");

			String[] quantityValues = request.getParameterValues("quantity");

			if (productValues == null || quantityValues == null) {

				throw new IllegalArgumentException("At least one product is required.");
			}

			if (productValues.length == 0 || productValues.length != quantityValues.length) {

				throw new IllegalArgumentException("Invalid product data.");
			}

			int[] productIds = new int[productValues.length];

			int[] quantities = new int[quantityValues.length];

			for (int i = 0; i < productValues.length; i++) {

				productIds[i] = Integer.parseInt(productValues[i]);

				quantities[i] = Integer.parseInt(quantityValues[i]);
			}

			// -------------------------------------------------
			// PAYMENT
			// -------------------------------------------------

			String paymentMethod = request.getParameter("paymentMethod");

			String transactionReference = request.getParameter("transactionReference");

			// -------------------------------------------------
			// CREATE SALE
			// -------------------------------------------------

			OfflineSaleResult result = offlineSaleService.createOfflineSale(clientId, createdBy, productIds, quantities,
					paymentMethod, transactionReference);

			session.setAttribute("offlineSaleSuccess",
					"Offline sale #" + result.getOrderId() + " created successfully. " + "Invoice #"
							+ result.getInvoiceId() + " generated. " + "Total ₹" + result.getTotalAmount());

			response.sendRedirect(request.getContextPath() + "/orders?action=view&id=" + result.getOrderId());

		} catch (NumberFormatException e) {

			session.setAttribute("offlineSaleError", "Invalid numeric value entered.");

			response.sendRedirect(request.getContextPath() + "/offline-sale");

		} catch (Exception e) {

			e.printStackTrace();

			String message = e.getMessage();

			if (message == null || message.isBlank()) {

				message = "Offline sale failed.";
			}

			session.setAttribute("offlineSaleError", message);

			response.sendRedirect(request.getContextPath() + "/offline-sale");
		}
	}
}