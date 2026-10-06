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

	private InventoryDAO inventoryDAO;
	private BatchDAO batchDAO;

	@Override
	public void init() {

		inventoryDAO = new InventoryDAO();
		batchDAO = new BatchDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if ("edit".equals(action)) {

			int inventoryId = Integer.parseInt(request.getParameter("inventoryId"));

			Inventory inventory = inventoryDAO.getInventoryById(inventoryId);

			List<Batch> batches = batchDAO.getAllBatches();

			request.setAttribute("inventory", inventory);
			request.setAttribute("batches", batches);

			request.getRequestDispatcher("/inventory-form.jsp").forward(request, response);

		} else if ("add".equals(action)) {

			List<Batch> batches = batchDAO.getAllBatches();

			request.setAttribute("batches", batches);

			request.getRequestDispatcher("/inventory-form.jsp").forward(request, response);

		} else {

			List<Inventory> inventoryList = inventoryDAO.getAllInventory();

			request.setAttribute("inventoryList", inventoryList);

			request.getRequestDispatcher("/inventory.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String action = request.getParameter("action");

		Inventory inventory = new Inventory();

		if ("update".equals(action)) {

			inventory.setInventoryId(Integer.parseInt(request.getParameter("inventoryId")));

			inventory.setBatchId(Integer.parseInt(request.getParameter("batchId")));

			inventory.setQuantity(Integer.parseInt(request.getParameter("quantity")));

			inventory.setReservedQuantity(Integer.parseInt(request.getParameter("reservedQuantity")));

			inventory.setDamagedQuantity(Integer.parseInt(request.getParameter("damagedQuantity")));

			inventory.setReorderLevel(Integer.parseInt(request.getParameter("reorderLevel")));

			inventoryDAO.updateInventory(inventory);

		} else if ("add".equals(action)) {

			inventory.setBatchId(Integer.parseInt(request.getParameter("batchId")));

			inventory.setQuantity(Integer.parseInt(request.getParameter("quantity")));

			inventory.setReservedQuantity(Integer.parseInt(request.getParameter("reservedQuantity")));

			inventory.setDamagedQuantity(Integer.parseInt(request.getParameter("damagedQuantity")));

			inventory.setReorderLevel(Integer.parseInt(request.getParameter("reorderLevel")));

			inventoryDAO.addInventory(inventory);
		}

		response.sendRedirect(request.getContextPath() + "/inventory");
	}
}