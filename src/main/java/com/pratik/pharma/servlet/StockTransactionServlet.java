package com.pratik.pharma.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.pratik.pharma.dao.BatchDAO;
import com.pratik.pharma.dao.InventoryDAO;
import com.pratik.pharma.dao.StockTransactionDAO;
import com.pratik.pharma.model.Batch;
import com.pratik.pharma.model.StockTransaction;
import com.pratik.pharma.util.DBConnection;

@WebServlet("/stock-transactions")
public class StockTransactionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private StockTransactionDAO transactionDAO;
	private BatchDAO batchDAO;
	private InventoryDAO inventoryDAO;

	@Override
	public void init() {

		transactionDAO = new StockTransactionDAO();
		batchDAO = new BatchDAO();
		inventoryDAO = new InventoryDAO();
	}

	// =========================================================
	// GET
	// =========================================================

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		// -----------------------------------------------------
		// Open Add Stock Transaction Form
		// -----------------------------------------------------

		if ("add".equals(action)) {

			List<Batch> batches = batchDAO.getAllBatches();

			request.setAttribute("batches", batches);

			request.getRequestDispatcher("/stock-transaction-form.jsp").forward(request, response);

			return;
		}

		// -----------------------------------------------------
		// Display All Stock Transactions
		// -----------------------------------------------------

		List<StockTransaction> transactions = transactionDAO.getAllTransactions();

		request.setAttribute("transactions", transactions);

		request.getRequestDispatcher("/stock-transactions.jsp").forward(request, response);
	}

	// =========================================================
	// POST
	// =========================================================

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		// -----------------------------------------------------
		// Check Login Session
		// -----------------------------------------------------

		HttpSession session = request.getSession(false);

		System.out.println("====================================");
		System.out.println("STOCK TRANSACTION POST");
		System.out.println("Session exists: " + (session != null));

		if (session != null) {
			System.out.println("Session ID: " + session.getId());
			System.out.println("User ID: " + session.getAttribute("userId"));
			System.out.println("Username: " + session.getAttribute("username"));
		}

		System.out.println("====================================");

		if (session == null || session.getAttribute("userId") == null) {

			response.sendRedirect(request.getContextPath() + "/login.jsp");

			return;
		}

		int loggedInUserId = (Integer) session.getAttribute("userId");

		Connection connection = null;

		try {

			// =================================================
			// READ FORM DATA
			// =================================================

			int batchId = Integer.parseInt(request.getParameter("batchId"));

			String transactionType = request.getParameter("transactionType");

			int quantity = Integer.parseInt(request.getParameter("quantity"));

			String referenceType = request.getParameter("referenceType");

			String referenceIdParameter = request.getParameter("referenceId");

			Integer referenceId = null;

			if (referenceIdParameter != null && !referenceIdParameter.trim().isEmpty()) {

				referenceId = Integer.parseInt(referenceIdParameter);
			}

			String remarks = request.getParameter("remarks");

			// =================================================
			// BASIC VALIDATION
			// =================================================

			if (quantity <= 0) {

				session.setAttribute("stockError", "Quantity must be greater than 0.");

				response.sendRedirect(request.getContextPath() + "/stock-transactions?action=add");

				return;
			}

			if (!"IN".equals(transactionType) && !"OUT".equals(transactionType)) {

				session.setAttribute("stockError", "Invalid transaction type.");

				response.sendRedirect(request.getContextPath() + "/stock-transactions?action=add");

				return;
			}

			// =================================================
			// CREATE ONE DATABASE CONNECTION
			// =================================================

			connection = DBConnection.getConnection();

			// Disable auto commit
			connection.setAutoCommit(false);

			boolean inventoryUpdated;

			// =================================================
			// STOCK IN
			// =================================================

			if ("IN".equals(transactionType)) {

				inventoryUpdated = inventoryDAO.increaseQuantity(connection, batchId, quantity);
			}

			// =================================================
			// STOCK OUT
			// =================================================

			else {

				inventoryUpdated = inventoryDAO.reduceAvailableQuantity(connection, batchId, quantity);
			}

			// =================================================
			// INVENTORY UPDATE FAILED
			// =================================================

			if (!inventoryUpdated) {

				connection.rollback();

				if ("OUT".equals(transactionType)) {

					session.setAttribute("stockError",
							"Insufficient stock! " + "Available quantity is less than " + quantity + ".");

				} else {

					session.setAttribute("stockError",
							"Stock IN failed. " + "Batch not found or inventory could not be updated.");
				}

				response.sendRedirect(request.getContextPath() + "/stock-transactions?action=add");

				return;
			}

			// =================================================
			// INSERT STOCK TRANSACTION
			// =================================================

			int transactionId = transactionDAO.addTransaction(connection, batchId, transactionType, quantity,
					referenceType, referenceId == null ? 0 : referenceId, remarks, loggedInUserId);

			// =================================================
			// TRANSACTION INSERT FAILED
			// =================================================

			if (transactionId == 0) {

				connection.rollback();

				session.setAttribute("stockError", "Stock transaction could not be saved.");

				response.sendRedirect(request.getContextPath() + "/stock-transactions?action=add");

				return;
			}

			// =================================================
			// EVERYTHING SUCCESSFUL
			// =================================================

			connection.commit();

			session.setAttribute("stockSuccess", "Stock transaction completed successfully.");

			response.sendRedirect(request.getContextPath() + "/stock-transactions");

		} catch (NumberFormatException e) {

			e.printStackTrace();

			try {

				if (connection != null) {
					connection.rollback();
				}

			} catch (Exception rollbackException) {

				rollbackException.printStackTrace();
			}

			session.setAttribute("stockError", "Please enter valid numeric values.");

			response.sendRedirect(request.getContextPath() + "/stock-transactions?action=add");

		} catch (Exception e) {

			e.printStackTrace();

			// -------------------------------------------------
			// Rollback
			// -------------------------------------------------

			try {

				if (connection != null) {
					connection.rollback();
				}

			} catch (Exception rollbackException) {

				rollbackException.printStackTrace();
			}

			session.setAttribute("stockError", "Something went wrong while processing the stock transaction.");

			response.sendRedirect(request.getContextPath() + "/stock-transactions?action=add");

		} finally {

			// -------------------------------------------------
			// Close Connection
			// -------------------------------------------------

			try {

				if (connection != null) {
					connection.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}
}