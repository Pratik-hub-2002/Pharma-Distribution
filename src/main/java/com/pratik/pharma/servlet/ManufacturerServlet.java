package com.pratik.pharma.servlet;

import java.io.IOException;
import java.util.List;

import com.pratik.pharma.dao.ManufacturerDAO;
import com.pratik.pharma.model.Manufacturer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/manufacturers")
public class ManufacturerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ManufacturerDAO manufacturerDAO;

	// =========================================================
	// INIT
	// =========================================================

	@Override
	public void init() throws ServletException {

		manufacturerDAO = new ManufacturerDAO();
	}

	// =========================================================
	// GET
	// =========================================================

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if (action == null || action.isBlank()) {

			listManufacturers(request, response);

			return;
		}

		switch (action) {

		case "add":

			showAddForm(request, response);

			break;

		case "edit":

			showEditForm(request, response);

			break;

		case "status":

			updateStatus(request, response);

			break;

		default:

			listManufacturers(request, response);

			break;
		}
	}

	// =========================================================
	// LIST MANUFACTURERS
	// =========================================================

	private void listManufacturers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Manufacturer> manufacturers = manufacturerDAO.getAllManufacturers();

		request.setAttribute("manufacturers", manufacturers);

		request.getRequestDispatcher("/manufacturers.jsp").forward(request, response);
	}

	// =========================================================
	// ADD FORM
	// =========================================================

	private void showAddForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("/manufacturer-form.jsp").forward(request, response);
	}

	// =========================================================
	// EDIT FORM
	// =========================================================

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idParameter = request.getParameter("id");

		if (idParameter == null || idParameter.isBlank()) {

			response.sendRedirect(request.getContextPath() + "/manufacturers");

			return;
		}

		try {

			int manufacturerId = Integer.parseInt(idParameter);

			Manufacturer manufacturer = manufacturerDAO.getManufacturerById(manufacturerId);

			if (manufacturer == null) {

				response.sendRedirect(request.getContextPath() + "/manufacturers");

				return;
			}

			request.setAttribute("manufacturer", manufacturer);

			request.getRequestDispatcher("/manufacturer-form.jsp").forward(request, response);

		} catch (NumberFormatException e) {

			response.sendRedirect(request.getContextPath() + "/manufacturers");
		}
	}

	// =========================================================
	// UPDATE STATUS
	// =========================================================

	private void updateStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String idParameter = request.getParameter("id");

		String status = request.getParameter("status");

		if (idParameter == null || idParameter.isBlank() || status == null || status.isBlank()) {

			response.sendRedirect(request.getContextPath() + "/manufacturers");

			return;
		}

		try {

			int manufacturerId = Integer.parseInt(idParameter);

			// Only allow ACTIVE or INACTIVE

			if (!"ACTIVE".equals(status) && !"INACTIVE".equals(status)) {

				response.sendRedirect(request.getContextPath() + "/manufacturers");

				return;
			}

			manufacturerDAO.updateStatus(manufacturerId, status);

			response.sendRedirect(request.getContextPath() + "/manufacturers");

		} catch (NumberFormatException e) {

			response.sendRedirect(request.getContextPath() + "/manufacturers");
		}
	}

	// =========================================================
	// POST
	// =========================================================

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String action = request.getParameter("action");

		if ("save".equals(action)) {

			saveManufacturer(request, response);

		} else {

			response.sendRedirect(request.getContextPath() + "/manufacturers");
		}
	}

	// =========================================================
	// SAVE MANUFACTURER
	// =========================================================

	private void saveManufacturer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String manufacturerIdParameter = request.getParameter("manufacturerId");

		String manufacturerName = request.getParameter("manufacturerName");

		String contactPerson = request.getParameter("contactPerson");

		String email = request.getParameter("email");

		String phone = request.getParameter("phone");

		String address = request.getParameter("address");

		String city = request.getParameter("city");

		String state = request.getParameter("state");

		String gstNumber = request.getParameter("gstNumber");

		String status = request.getParameter("status");

		// =====================================================
		// BASIC VALIDATION
		// =====================================================

		if (manufacturerName == null || manufacturerName.isBlank()) {

			request.setAttribute("error", "Manufacturer name is required.");

			loadFormAgain(request, response);

			return;
		}

		// Default status

		if (status == null || status.isBlank()) {

			status = "ACTIVE";
		}

		// Validate status

		if (!"ACTIVE".equals(status) && !"INACTIVE".equals(status)) {

			status = "ACTIVE";
		}

		// =====================================================
		// CREATE MANUFACTURER OBJECT
		// =====================================================

		Manufacturer manufacturer = new Manufacturer();

		manufacturer.setManufacturerName(manufacturerName.trim());

		manufacturer.setContactPerson(emptyToNull(contactPerson));

		manufacturer.setEmail(emptyToNull(email));

		manufacturer.setPhone(emptyToNull(phone));

		manufacturer.setAddress(emptyToNull(address));

		manufacturer.setCity(emptyToNull(city));

		manufacturer.setState(emptyToNull(state));

		manufacturer.setGstNumber(emptyToNull(gstNumber));

		manufacturer.setStatus(status);

		// =====================================================
		// UPDATE EXISTING MANUFACTURER
		// =====================================================

		if (manufacturerIdParameter != null && !manufacturerIdParameter.isBlank()) {

			try {

				int manufacturerId = Integer.parseInt(manufacturerIdParameter);

				manufacturer.setManufacturerId(manufacturerId);

				manufacturerDAO.updateManufacturer(manufacturer);

			} catch (NumberFormatException e) {

				request.setAttribute("error", "Invalid manufacturer ID.");

				request.setAttribute("manufacturer", manufacturer);

				loadFormAgain(request, response);

				return;
			}

		}

		// =====================================================
		// ADD NEW MANUFACTURER
		// =====================================================

		else {

			manufacturerDAO.addManufacturer(manufacturer);
		}

		// =====================================================
		// REDIRECT TO LIST
		// =====================================================

		response.sendRedirect(request.getContextPath() + "/manufacturers");
	}

	// =========================================================
	// LOAD FORM AFTER ERROR
	// =========================================================

	private void loadFormAgain(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("/manufacturer-form.jsp").forward(request, response);
	}

	// =========================================================
	// EMPTY STRING TO NULL
	// =========================================================

	private String emptyToNull(String value) {

		if (value == null || value.trim().isEmpty()) {

			return null;
		}

		return value.trim();
	}
}