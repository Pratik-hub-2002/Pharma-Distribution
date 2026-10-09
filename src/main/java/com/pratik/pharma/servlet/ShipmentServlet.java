package com.pratik.pharma.servlet;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.pratik.pharma.dao.OrderDAO;
import com.pratik.pharma.dao.ShipmentDAO;
import com.pratik.pharma.model.Order;
import com.pratik.pharma.model.Shipment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/shipments")
public class ShipmentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ShipmentDAO shipmentDAO;
	private OrderDAO orderDAO;

	// =========================================================
	// INIT
	// =========================================================

	@Override
	public void init() throws ServletException {

		shipmentDAO = new ShipmentDAO();

		orderDAO = new OrderDAO();
	}

	// =========================================================
	// GET
	// =========================================================

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		// -----------------------------------------------------
		// LIST
		// -----------------------------------------------------

		if (action == null || action.equals("list")) {

			listShipments(request, response);

			return;
		}

		// -----------------------------------------------------
		// ADD FORM
		// -----------------------------------------------------

		if (action.equals("add")) {

			showAddForm(request, response);

			return;
		}

		// -----------------------------------------------------
		// VIEW
		// -----------------------------------------------------

		if (action.equals("view")) {

			viewShipment(request, response);

			return;
		}

		// -----------------------------------------------------
		// DISPATCH
		// -----------------------------------------------------

		if (action.equals("dispatch")) {

			dispatchShipment(request, response);

			return;
		}

		// -----------------------------------------------------
		// DELIVER
		// -----------------------------------------------------

		if (action.equals("deliver")) {

			deliverShipment(request, response);

			return;
		}

		response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid shipment action.");
	}

	// =========================================================
	// POST
	// =========================================================

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if ("create".equals(action)) {

			createShipment(request, response);

			return;
		}

		if ("temperature".equals(action)) {

			updateTemperature(request, response);

			return;
		}

		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid shipment request.");
	}

	// =========================================================
	// LIST SHIPMENTS
	// =========================================================

	private void listShipments(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Shipment> shipments = shipmentDAO.getAllShipments();

		request.setAttribute("shipments", shipments);

		request.getRequestDispatcher("shipment-list.jsp").forward(request, response);
	}

	// =========================================================
	// SHOW ADD FORM
	// =========================================================

	private void showAddForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Order> orders = shipmentDAO.getOrdersEligibleForShipment();

		request.setAttribute("orders", orders);

		request.getRequestDispatcher("shipment-form.jsp").forward(request, response);
	}

	// =========================================================
	// CREATE SHIPMENT
	// =========================================================

	private void createShipment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("userId") == null) {

			response.sendRedirect("login.jsp");

			return;
		}

		// -----------------------------------------------------
		// ORDER ID
		// -----------------------------------------------------

		String orderIdParameter = request.getParameter("orderId");

		if (orderIdParameter == null || orderIdParameter.isBlank()) {

			request.setAttribute("error", "Order is required.");

			showAddForm(request, response);

			return;
		}

		int orderId;

		try {

			orderId = Integer.parseInt(orderIdParameter);

		} catch (NumberFormatException e) {

			request.setAttribute("error", "Invalid order ID.");

			showAddForm(request, response);

			return;
		}

		// -----------------------------------------------------
		// CHECK ORDER
		// -----------------------------------------------------

		Order order = orderDAO.getOrderById(orderId);

		if (order == null) {

			request.setAttribute("error", "Order not found.");

			showAddForm(request, response);

			return;
		}

		// -----------------------------------------------------
		// CHECK ORDER STATUS
		// -----------------------------------------------------

		if (!"APPROVED".equals(order.getOrderStatus())) {

			request.setAttribute("error", "Order #" + orderId + " is not approved for shipment.");

			showAddForm(request, response);

			return;
		}

		// -----------------------------------------------------
		// CHECK PAYMENT
		// -----------------------------------------------------

		if (!"PAID".equals(order.getCreditStatus())) {

			request.setAttribute("error", "Order #" + orderId + " has not been paid.");

			showAddForm(request, response);

			return;
		}

		// -----------------------------------------------------
		// CHECK EXISTING SHIPMENT
		// -----------------------------------------------------

		Shipment existingShipment = shipmentDAO.getShipmentByOrderId(orderId);

		if (existingShipment != null) {

			request.setAttribute("error", "A shipment already exists for Order #" + orderId);

			showAddForm(request, response);

			return;
		}

		// -----------------------------------------------------
		// FORM DATA
		// -----------------------------------------------------

		String trackingNumber = request.getParameter("trackingNumber");

		String temperatureStatus = request.getParameter("temperatureStatus");

		String coldChainParameter = request.getParameter("coldChainRequired");

		boolean coldChainRequired = "true".equalsIgnoreCase(coldChainParameter);

		// -----------------------------------------------------
		// CREATE SHIPMENT
		// -----------------------------------------------------

		Shipment shipment = new Shipment();

		shipment.setOrderId(orderId);

		shipment.setTrackingNumber(trackingNumber);

		shipment.setShipmentDate(Date.valueOf(LocalDate.now()));

		shipment.setShipmentStatus("PENDING");

		shipment.setColdChainRequired(coldChainRequired);

		shipment.setTemperatureStatus(temperatureStatus);

		shipment.setCreatedBy((Integer) session.getAttribute("userId"));

		boolean created = shipmentDAO.addShipment(shipment);

		if (created) {

			response.sendRedirect("shipments?action=list");

		} else {

			request.setAttribute("error", "Unable to create shipment.");

			showAddForm(request, response);
		}
	}

	// =========================================================
	// VIEW SHIPMENT
	// =========================================================

	private void viewShipment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idParameter = request.getParameter("id");

		if (idParameter == null || idParameter.isBlank()) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Shipment ID is required.");

			return;
		}

		int shipmentId;

		try {

			shipmentId = Integer.parseInt(idParameter);

		} catch (NumberFormatException e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid shipment ID.");

			return;
		}

		Shipment shipment = shipmentDAO.getShipmentById(shipmentId);

		if (shipment == null) {

			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Shipment not found.");

			return;
		}

		request.setAttribute("shipment", shipment);

		request.getRequestDispatcher("shipment-view.jsp").forward(request, response);
	}

	// =========================================================
	// DISPATCH SHIPMENT
	// =========================================================

	private void dispatchShipment(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String idParameter = request.getParameter("id");

		if (idParameter == null || idParameter.isBlank()) {

			response.sendRedirect("shipments?action=list");

			return;
		}

		int shipmentId;

		try {

			shipmentId = Integer.parseInt(idParameter);

		} catch (NumberFormatException e) {

			response.sendRedirect("shipments?action=list");

			return;
		}

		shipmentDAO.markDispatched(shipmentId);

		response.sendRedirect("shipments?action=view&id=" + shipmentId);
	}

	// =========================================================
	// DELIVER SHIPMENT
	// =========================================================

	private void deliverShipment(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String idParameter = request.getParameter("id");

		if (idParameter == null || idParameter.isBlank()) {

			response.sendRedirect("shipments?action=list");

			return;
		}

		int shipmentId;

		try {

			shipmentId = Integer.parseInt(idParameter);

		} catch (NumberFormatException e) {

			response.sendRedirect("shipments?action=list");

			return;
		}

		shipmentDAO.markDelivered(shipmentId);

		response.sendRedirect("shipments?action=view&id=" + shipmentId);
	}

	// =========================================================
	// UPDATE TEMPERATURE
	// =========================================================

	private void updateTemperature(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String idParameter = request.getParameter("id");

		String temperatureStatus = request.getParameter("temperatureStatus");

		if (idParameter == null || idParameter.isBlank()) {

			response.sendRedirect("shipments?action=list");

			return;
		}

		int shipmentId;

		try {

			shipmentId = Integer.parseInt(idParameter);

		} catch (NumberFormatException e) {

			response.sendRedirect("shipments?action=list");

			return;
		}

		shipmentDAO.updateTemperatureStatus(shipmentId, temperatureStatus);

		response.sendRedirect("shipments?action=view&id=" + shipmentId);
	}
}