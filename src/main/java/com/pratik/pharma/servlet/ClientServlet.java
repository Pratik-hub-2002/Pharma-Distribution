package com.pratik.pharma.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.pratik.pharma.dao.ClientDAO;
import com.pratik.pharma.model.Client;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/clients")
public class ClientServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ClientDAO clientDAO;

	@Override
	public void init() {
		clientDAO = new ClientDAO();
	}

	// =========================
	// GET
	// =========================
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		// =========================
		// EDIT CLIENT
		// =========================
		if ("edit".equals(action)) {

			String id = request.getParameter("id");

			try {

				int clientId = Integer.parseInt(id);

				Client client = clientDAO.getClientById(clientId);

				if (client == null) {

					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Client not found");

					return;
				}

				request.setAttribute("client", client);

				request.getRequestDispatcher("client-edit.jsp").forward(request, response);

				return;

			} catch (NumberFormatException e) {

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client ID");

				return;
			}
		}

		// =========================
		// ACTIVATE / DEACTIVATE
		// =========================
		if ("status".equals(action)) {

			String id = request.getParameter("id");

			try {

				int clientId = Integer.parseInt(id);

				Client client = clientDAO.getClientById(clientId);

				if (client == null) {

					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Client not found");

					return;
				}

				String newStatus;

				if ("ACTIVE".equalsIgnoreCase(client.getStatus())) {

					newStatus = "INACTIVE";

				} else {

					newStatus = "ACTIVE";
				}

				boolean success = clientDAO.updateClientStatus(clientId, newStatus);

				if (success) {

					response.sendRedirect(request.getContextPath() + "/clients");

				} else {

					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to update client status");
				}

				return;

			} catch (NumberFormatException e) {

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client ID");

				return;
			}
		}

		// =========================
		// DISPLAY ALL CLIENTS
		// =========================

		List<Client> clients = clientDAO.getAllClients();

		request.setAttribute("clients", clients);

		request.getRequestDispatcher("clients.jsp").forward(request, response);
	}

	// =========================
	// POST
	// =========================
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String action = request.getParameter("action");

		Client client = new Client();

		// =========================
		// CLIENT INFORMATION
		// =========================

		client.setClientName(request.getParameter("clientName"));

		client.setClientType(request.getParameter("clientType"));

		client.setOwnerName(request.getParameter("ownerName"));

		client.setContactPerson(request.getParameter("contactPerson"));

		client.setEmail(request.getParameter("email"));

		client.setPhone(request.getParameter("phone"));

		client.setAddress(request.getParameter("address"));

		client.setCity(request.getParameter("city"));

		client.setState(request.getParameter("state"));

		client.setPincode(request.getParameter("pincode"));

		client.setGstNumber(request.getParameter("gstNumber"));

		// =========================
		// CREDIT LIMIT
		// =========================

		String creditLimit = request.getParameter("creditLimit");

		if (creditLimit == null || creditLimit.trim().isEmpty()) {

			client.setCreditLimit(BigDecimal.ZERO);

		} else {

			client.setCreditLimit(new BigDecimal(creditLimit));
		}

		// =========================
		// CREDIT PERIOD
		// =========================

		String creditPeriodDays = request.getParameter("creditPeriodDays");

		if (creditPeriodDays == null || creditPeriodDays.trim().isEmpty()) {

			client.setCreditPeriodDays(0);

		} else {

			client.setCreditPeriodDays(Integer.parseInt(creditPeriodDays));
		}

		// =========================
		// STATUS
		// =========================

		client.setStatus(request.getParameter("status"));

		// =========================
		// VERIFICATION STATUS
		// =========================

		client.setVerificationStatus(request.getParameter("verificationStatus"));

		// =========================
		// UPDATE CLIENT
		// =========================

		if ("update".equals(action)) {

			String clientId = request.getParameter("clientId");

			try {

				int id = Integer.parseInt(clientId);

				client.setClientId(id);

				boolean success = clientDAO.updateClient(client);

				if (success) {

					response.sendRedirect(request.getContextPath() + "/clients");

				} else {

					request.setAttribute("error", "Unable to update client.");

					request.setAttribute("client", client);

					request.getRequestDispatcher("client-edit.jsp").forward(request, response);
				}

				return;

			} catch (NumberFormatException e) {

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client ID");

				return;
			}
		}

		// =========================
		// ADD CLIENT
		// =========================

		boolean success = clientDAO.addClient(client);

		if (success) {

			response.sendRedirect(request.getContextPath() + "/clients");

		} else {

			request.setAttribute("error", "Unable to add client.");

			request.setAttribute("client", client);

			request.getRequestDispatcher("client-form.jsp").forward(request, response);
		}
	}
}