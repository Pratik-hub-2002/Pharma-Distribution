package com.pratik.pharma.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import com.pratik.pharma.dao.BatchDAO;
import com.pratik.pharma.dao.ProductDAO;
import com.pratik.pharma.model.Batch;
import com.pratik.pharma.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/batches")
public class BatchServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private BatchDAO batchDAO;
	private ProductDAO productDAO;

	@Override
	public void init() {

		batchDAO = new BatchDAO();
		productDAO = new ProductDAO();
	}

	// =========================
	// GET
	// =========================

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		// =========================
		// EDIT BATCH
		// =========================

		if ("edit".equalsIgnoreCase(action)) {

			try {

				int batchId = Integer.parseInt(request.getParameter("batchId"));

				Batch batch = batchDAO.getBatchById(batchId);

				if (batch == null) {

					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Batch not found");

					return;
				}

				List<Product> products = productDAO.getAllProducts();

				request.setAttribute("batch", batch);

				request.setAttribute("products", products);

				request.getRequestDispatcher("/batch-form.jsp").forward(request, response);

			} catch (NumberFormatException e) {

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Batch ID");
			}

			return;
		}

		// =========================
		// ADD BATCH FORM
		// =========================

		if ("add".equalsIgnoreCase(action)) {

			List<Product> products = productDAO.getAllProducts();

			request.setAttribute("products", products);

			request.getRequestDispatcher("/batch-form.jsp").forward(request, response);

			return;
		}

		// =========================
		// ACTIVATE / DEACTIVATE
		// =========================

		if ("status".equalsIgnoreCase(action)) {

			try {

				int batchId = Integer.parseInt(request.getParameter("batchId"));

				Batch batch = batchDAO.getBatchById(batchId);

				if (batch != null) {

					String newStatus;

					if ("ACTIVE".equalsIgnoreCase(batch.getStatus())) {

						newStatus = "INACTIVE";

					} else {

						newStatus = "ACTIVE";
					}

					batchDAO.updateBatchStatus(batchId, newStatus);
				}

				response.sendRedirect(request.getContextPath() + "/batches");

			} catch (NumberFormatException e) {

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Batch ID");
			}

			return;
		}

		// =========================
		// DEFAULT → LIST
		// =========================

		List<Batch> batches = batchDAO.getAllBatches();

		request.setAttribute("batches", batches);

		request.getRequestDispatcher("/batches.jsp").forward(request, response);
	}

	// =========================
	// POST
	// =========================

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		// =========================
		// UPDATE
		// =========================

		if ("update".equalsIgnoreCase(action)) {

			try {

				Batch batch = new Batch();

				batch.setBatchId(Integer.parseInt(request.getParameter("batchId")));

				batch.setProductId(Integer.parseInt(request.getParameter("productId")));

				batch.setBatchNumber(request.getParameter("batchNumber"));

				batch.setManufacturingDate(Date.valueOf(request.getParameter("manufacturingDate")));

				batch.setExpiryDate(Date.valueOf(request.getParameter("expiryDate")));

				batch.setPurchasePrice(new BigDecimal(request.getParameter("purchasePrice")));

				batch.setStatus(request.getParameter("status"));

				batchDAO.updateBatch(batch);

				response.sendRedirect(request.getContextPath() + "/batches");

			} catch (Exception e) {

				e.printStackTrace();

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid batch data");
			}

			return;
		}

		// =========================
		// ADD
		// =========================

		try {

			Batch batch = new Batch();

			batch.setProductId(Integer.parseInt(request.getParameter("productId")));

			batch.setBatchNumber(request.getParameter("batchNumber"));

			batch.setManufacturingDate(Date.valueOf(request.getParameter("manufacturingDate")));

			batch.setExpiryDate(Date.valueOf(request.getParameter("expiryDate")));

			batch.setPurchasePrice(new BigDecimal(request.getParameter("purchasePrice")));

			batch.setStatus("ACTIVE");

			batchDAO.addBatch(batch);

			response.sendRedirect(request.getContextPath() + "/batches");

		} catch (Exception e) {

			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid batch data");
		}
	}
}