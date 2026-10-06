package com.pratik.pharma.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import com.pratik.pharma.dao.ProductDAO;
import com.pratik.pharma.dao.SchemeDAO;
import com.pratik.pharma.model.Product;
import com.pratik.pharma.model.Scheme;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/schemes")
public class SchemeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private SchemeDAO schemeDAO;

	private ProductDAO productDAO;

	// =========================================================
	// INIT
	// =========================================================

	@Override
	public void init() throws ServletException {

		schemeDAO = new SchemeDAO();

		productDAO = new ProductDAO();
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

		case "list":

			listSchemes(request, response);

			break;

		case "add":

			showAddForm(request, response);

			break;

		case "edit":

			showEditForm(request, response);

			break;

		case "activate":

			updateStatus(request, response, "ACTIVE");

			break;

		case "deactivate":

			updateStatus(request, response, "INACTIVE");

			break;

		default:

			response.sendError(HttpServletResponse.SC_NOT_FOUND);

			break;
		}
	}

	// =========================================================
	// POST
	// =========================================================

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if ("add".equals(action)) {

			addScheme(request, response);

		}

		else if ("update".equals(action)) {

			updateScheme(request, response);

		}

		else {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	// =========================================================
	// LIST
	// =========================================================

	private void listSchemes(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Scheme> schemes = schemeDAO.getAllSchemes();

		request.setAttribute("schemes", schemes);

		request.getRequestDispatcher("/scheme-list.jsp").forward(request, response);
	}

	// =========================================================
	// ADD FORM
	// =========================================================

	private void showAddForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Product> products = productDAO.getAllProducts();

		request.setAttribute("products", products);

		request.getRequestDispatcher("/scheme-form.jsp").forward(request, response);
	}

	// =========================================================
	// EDIT FORM
	// =========================================================

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idParameter = request.getParameter("id");

		if (idParameter == null) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Scheme ID is required");

			return;
		}

		int schemeId = Integer.parseInt(idParameter);

		Scheme scheme = schemeDAO.getSchemeById(schemeId);

		if (scheme == null) {

			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Scheme not found");

			return;
		}

		List<Product> products = productDAO.getAllProducts();

		request.setAttribute("scheme", scheme);

		request.setAttribute("products", products);

		request.getRequestDispatcher("/scheme-form.jsp").forward(request, response);
	}

	// =========================================================
	// ADD SCHEME
	// =========================================================

	private void addScheme(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {

			HttpSession session = request.getSession(false);

			if (session == null || session.getAttribute("userId") == null) {

				response.sendRedirect("login.jsp");

				return;
			}

			int userId = (Integer) session.getAttribute("userId");

			Scheme scheme = readSchemeFromRequest(request);

			scheme.setStatus("ACTIVE");

			scheme.setCreatedBy(userId);

			boolean success = schemeDAO.addScheme(scheme);

			if (success) {

				response.sendRedirect("schemes?action=list");

			} else {

				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to add scheme");
			}

		} catch (Exception e) {

			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid scheme data");
		}
	}

	// =========================================================
	// UPDATE SCHEME
	// =========================================================

	private void updateScheme(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {

			int schemeId = Integer.parseInt(request.getParameter("schemeId"));

			Scheme scheme = readSchemeFromRequest(request);

			scheme.setSchemeId(schemeId);

			boolean success = schemeDAO.updateScheme(scheme);

			if (success) {

				response.sendRedirect("schemes?action=list");

			} else {

				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to update scheme");
			}

		} catch (Exception e) {

			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid scheme data");
		}
	}

	// =========================================================
	// ACTIVATE / DEACTIVATE
	// =========================================================

	private void updateStatus(HttpServletRequest request, HttpServletResponse response, String status)
			throws IOException {

		try {

			int schemeId = Integer.parseInt(request.getParameter("id"));

			schemeDAO.updateStatus(schemeId, status);

			response.sendRedirect("schemes?action=list");

		} catch (Exception e) {

			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid scheme ID");
		}
	}

	// =========================================================
	// READ FORM DATA
	// =========================================================

	private Scheme readSchemeFromRequest(HttpServletRequest request) {

		Scheme scheme = new Scheme();

		scheme.setSchemeName(request.getParameter("schemeName"));

		scheme.setProductId(Integer.parseInt(request.getParameter("productId")));

		scheme.setSchemeType(request.getParameter("schemeType"));

		scheme.setMinimumQuantity(Integer.parseInt(request.getParameter("minimumQuantity")));

		String freeQuantity = request.getParameter("freeQuantity");

		if (freeQuantity == null || freeQuantity.isBlank()) {

			scheme.setFreeQuantity(0);

		} else {

			scheme.setFreeQuantity(Integer.parseInt(freeQuantity));
		}

		String discount = request.getParameter("discountPercentage");

		if (discount == null || discount.isBlank()) {

			scheme.setDiscountPercentage(BigDecimal.ZERO);

		} else {

			scheme.setDiscountPercentage(new BigDecimal(discount));
		}

		scheme.setStartDate(Date.valueOf(request.getParameter("startDate")));

		scheme.setEndDate(Date.valueOf(request.getParameter("endDate")));

		return scheme;
	}
}