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

	@Override
	public void init() throws ServletException {

		clientDAO = new ClientDAO();
		productDAO = new ProductDAO();

		offlineSaleService = new OfflineSaleService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			List<Client> clients = clientDAO.getAllClients();
			List<Product> products = productDAO.getAllProducts();

			request.setAttribute("clients", clients);
			request.setAttribute("products", products);

			request.getRequestDispatcher("offline-sale.jsp").forward(request, response);

		} catch (Exception e) {

			e.printStackTrace();

			request.setAttribute("error", "Unable to load clients and products.");

			request.getRequestDispatcher("offline-sale.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			// -----------------------------
			// GET LOGGED-IN USER
			// -----------------------------

			HttpSession session = request.getSession(false);

			if (session == null || session.getAttribute("userId") == null) {

				response.sendRedirect("login.jsp");
				return;
			}

			int createdBy = (Integer) session.getAttribute("userId");

			// -----------------------------
			// GET FORM DATA
			// -----------------------------

			int clientId = Integer.parseInt(request.getParameter("clientId"));

			int productId = Integer.parseInt(request.getParameter("productId"));

			int quantity = Integer.parseInt(request.getParameter("quantity"));

			String paymentMethod = request.getParameter("paymentMethod");

			String transactionReference = request.getParameter("transactionReference");

			// -----------------------------
			// BASIC VALIDATION
			// -----------------------------

			if (quantity <= 0) {

				request.setAttribute("error", "Quantity must be greater than zero.");

				loadFormData(request, response);
				return;
			}

			if (paymentMethod == null || paymentMethod.trim().isEmpty()) {

				request.setAttribute("error", "Please select a payment method.");

				loadFormData(request, response);
				return;
			}

			// -----------------------------
			// CREATE OFFLINE SALE
			// -----------------------------

			int[] productIds = { productId };

			int[] quantities = { quantity };

			OfflineSaleResult result = offlineSaleService.createOfflineSale(clientId, createdBy, productIds, quantities,
					paymentMethod, transactionReference);

			// -----------------------------
			// SUCCESS
			// -----------------------------

			response.sendRedirect("orders?action=view&id=" + result.getOrderId());

		} catch (Exception e) {

			e.printStackTrace();

			request.setAttribute("error", e.getMessage() != null ? e.getMessage() : "Unable to create offline sale.");

			loadFormData(request, response);
		}
	}

	// -----------------------------------------
	// LOAD FORM DATA AFTER ERROR
	// -----------------------------------------

	private void loadFormData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			List<Client> clients = clientDAO.getAllClients();

			List<Product> products = productDAO.getAllProducts();

			request.setAttribute("clients", clients);

			request.setAttribute("products", products);

			request.getRequestDispatcher("offline-sale.jsp").forward(request, response);

		} catch (Exception e) {

			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load offline sale page.");
		}
	}
}

//package com.pratik.pharma.servlet;
//
//import java.io.IOException;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@WebServlet("/offline-sale")
//public class OfflineSaleServlet extends HttpServlet {
//
//	private static final long serialVersionUID = 1L;
//
//	@Override
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		response.getWriter().println("Offline Sale Servlet is working!");
//	}
//}