package com.pratik.pharma.servlet;

import java.io.IOException;
import java.util.List;

import com.pratik.pharma.dao.ClientDocumentDAO;
import com.pratik.pharma.model.ClientDocument;
import com.pratik.pharma.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/client-documents")
public class ClientDocumentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ClientDocumentDAO documentDAO;

	@Override
	public void init() {
		documentDAO = new ClientDocumentDAO();
	}

	// =========================================================
	// GET - DISPLAY CLIENT DOCUMENTS
	// =========================================================

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String clientIdParam = request.getParameter("clientId");

		if (clientIdParam == null || clientIdParam.trim().isEmpty()) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Client ID is required");

			return;
		}

		try {

			int clientId = Integer.parseInt(clientIdParam);

			List<ClientDocument> documents = documentDAO.getDocumentsByClientId(clientId);

			request.setAttribute("documents", documents);
			request.setAttribute("clientId", clientId);

			request.getRequestDispatcher("/client-documents.jsp").forward(request, response);

		} catch (NumberFormatException e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Client ID");
		}
	}

	// =========================================================
	// POST - VERIFY / REJECT DOCUMENT
	// =========================================================

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		String documentIdParam = request.getParameter("documentId");
		String clientIdParam = request.getParameter("clientId");

		if (action == null || documentIdParam == null || clientIdParam == null) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Required parameters are missing");

			return;
		}

		// Get logged-in user from session
		HttpSession session = request.getSession(false);

		if (session == null) {

			response.sendRedirect(request.getContextPath() + "/login.jsp");

			return;
		}

		User loggedInUser = (User) session.getAttribute("loggedInUser");

		if (loggedInUser == null) {

			response.sendRedirect(request.getContextPath() + "/login.jsp");

			return;
		}

		try {

			int documentId = Integer.parseInt(documentIdParam);

			int clientId = Integer.parseInt(clientIdParam);

			// Actual logged-in user ID
			int verifiedBy = loggedInUser.getUserId();

			if ("verify".equalsIgnoreCase(action)) {

				documentDAO.updateDocumentStatus(documentId, "VERIFIED", verifiedBy);

			} else if ("reject".equalsIgnoreCase(action)) {

				documentDAO.updateDocumentStatus(documentId, "REJECTED", verifiedBy);

			} else {

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");

				return;
			}

			// Return to documents page
			response.sendRedirect(request.getContextPath() + "/client-documents?clientId=" + clientId);

		} catch (NumberFormatException e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid document or client ID");
		}
	}
}