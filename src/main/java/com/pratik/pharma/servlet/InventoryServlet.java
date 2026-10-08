package com.pratik.pharma.servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.pratik.pharma.dao.BatchDAO;
import com.pratik.pharma.dao.InventoryDAO;
import com.pratik.pharma.model.Batch;
import com.pratik.pharma.model.Inventory;

@WebServlet("/inventory")
public class InventoryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private InventoryDAO inventoryDAO;
	private BatchDAO batchDAO;

	// =========================================================
	// INIT
	// =========================================================

	@Override
	public void init() throws ServletException {

		inventoryDAO = new InventoryDAO();
		batchDAO = new BatchDAO();
	}

	// =========================================================
	// GET
	// =========================================================

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		// =====================================================
		// EDIT INVENTORY
		// =====================================================

		if ("edit".equals(action)) {

			String inventoryIdParameter = request.getParameter("inventoryId");

			if (inventoryIdParameter == null || inventoryIdParameter.isBlank()) {

				response.sendRedirect(request.getContextPath() + "/inventory");

				return;
			}

			try {

				int inventoryId = Integer.parseInt(inventoryIdParameter);

				Inventory inventory = inventoryDAO.getInventoryById(inventoryId);

				if (inventory == null) {

					response.sendRedirect(request.getContextPath() + "/inventory");

					return;
				}

				List<Batch> batches = batchDAO.getAllBatches();

				request.setAttribute("inventory", inventory);

				request.setAttribute("batches", batches);

				request.getRequestDispatcher("/inventory-form.jsp").forward(request, response);

			} catch (NumberFormatException e) {

				response.sendRedirect(request.getContextPath() + "/inventory");
			}

			return;
		}

		// =====================================================
		// ADD INVENTORY
		// =====================================================

		if ("add".equals(action)) {

			List<Batch> batches = batchDAO.getAllBatches();

			request.setAttribute("batches", batches);

			request.getRequestDispatcher("/inventory-form.jsp").forward(request, response);

			return;
		}

		// =====================================================
		// LOW STOCK
		// =====================================================

		if ("low-stock".equals(action)) {

			List<Inventory> inventoryList = inventoryDAO.getLowStockInventory();

			request.setAttribute("inventoryList", inventoryList);

			request.setAttribute("alertTitle", "Low Stock Inventory");

			request.getRequestDispatcher("/inventory-alert.jsp").forward(request, response);

			return;
		}

		// =====================================================
		// NEAR EXPIRY
		// =====================================================

		if ("near-expiry".equals(action)) {

			List<Inventory> inventoryList = inventoryDAO.getNearExpiryInventory();

			request.setAttribute("inventoryList", inventoryList);

			request.setAttribute("alertTitle", "Batches Expiring Within 90 Days");

			request.getRequestDispatcher("/inventory-alert.jsp").forward(request, response);

			return;
		}

		// =====================================================
		// EXPIRED
		// =====================================================

		if ("expired".equals(action)) {

			List<Inventory> inventoryList = inventoryDAO.getExpiredInventory();

			request.setAttribute("inventoryList", inventoryList);

			request.setAttribute("alertTitle", "Expired Batches");

			request.getRequestDispatcher("/inventory-alert.jsp").forward(request, response);

			return;
		}

		// =====================================================
		// NORMAL INVENTORY LIST
		// =====================================================

		List<Inventory> inventoryList = inventoryDAO.getAllInventory();

		// -----------------------------------------------------
		// INVENTORY RECORDS
		// -----------------------------------------------------

		request.setAttribute("inventoryList", inventoryList);

		// -----------------------------------------------------
		// SUMMARY CARDS
		// -----------------------------------------------------

		request.setAttribute("totalBatches", inventoryDAO.getTotalBatches());

		request.setAttribute("totalQuantity", inventoryDAO.getTotalQuantity());

		request.setAttribute("totalAvailable", inventoryDAO.getTotalAvailableQuantity());

		request.setAttribute("totalReserved", inventoryDAO.getTotalReservedQuantity());

		request.setAttribute("totalDamaged", inventoryDAO.getTotalDamagedQuantity());

		// -----------------------------------------------------
		// ALERT COUNTS
		// -----------------------------------------------------

		request.setAttribute("lowStockCount", inventoryDAO.getLowStockCount());

		request.setAttribute("nearExpiryCount", inventoryDAO.getNearExpiryCount());

		request.setAttribute("expiredCount", inventoryDAO.getExpiredCount());

		// -----------------------------------------------------
		// OPEN INVENTORY PAGE
		// -----------------------------------------------------

		request.getRequestDispatcher("/inventory.jsp").forward(request, response);
	}

	// =========================================================
	// POST
	// =========================================================

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String action = request.getParameter("action");

		// =====================================================
		// UPDATE INVENTORY
		// =====================================================

		if ("update".equals(action)) {

			try {

				Inventory inventory = new Inventory();

				inventory.setInventoryId(Integer.parseInt(request.getParameter("inventoryId")));

				inventory.setBatchId(Integer.parseInt(request.getParameter("batchId")));

				inventory.setQuantity(Integer.parseInt(request.getParameter("quantity")));

				inventory.setReservedQuantity(Integer.parseInt(request.getParameter("reservedQuantity")));

				inventory.setDamagedQuantity(Integer.parseInt(request.getParameter("damagedQuantity")));

				inventory.setReorderLevel(Integer.parseInt(request.getParameter("reorderLevel")));

				inventoryDAO.updateInventory(inventory);

			} catch (NumberFormatException e) {

				e.printStackTrace();
			}
		}

		// =====================================================
		// ADD INVENTORY
		// =====================================================

		else if ("add".equals(action)) {

			try {

				Inventory inventory = new Inventory();

				inventory.setBatchId(Integer.parseInt(request.getParameter("batchId")));

				inventory.setQuantity(Integer.parseInt(request.getParameter("quantity")));

				inventory.setReservedQuantity(Integer.parseInt(request.getParameter("reservedQuantity")));

				inventory.setDamagedQuantity(Integer.parseInt(request.getParameter("damagedQuantity")));

				inventory.setReorderLevel(Integer.parseInt(request.getParameter("reorderLevel")));

				inventoryDAO.addInventory(inventory);

			} catch (NumberFormatException e) {

				e.printStackTrace();
			}
		}

		// =====================================================
		// REDIRECT
		// =====================================================

		response.sendRedirect(request.getContextPath() + "/inventory");
	}
}