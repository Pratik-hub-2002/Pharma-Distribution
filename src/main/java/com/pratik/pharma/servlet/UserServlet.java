package com.pratik.pharma.servlet;

import java.io.IOException;
import java.util.List;

import com.pratik.pharma.dao.ClientDAO;
import com.pratik.pharma.dao.RoleDAO;
import com.pratik.pharma.dao.UserDAO;
import com.pratik.pharma.model.Client;
import com.pratik.pharma.model.Role;
import com.pratik.pharma.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private UserDAO userDAO;
	private RoleDAO roleDAO;
	private ClientDAO clientDAO;

	// =========================================================
	// INIT
	// =========================================================

	@Override
	public void init() throws ServletException {

		userDAO = new UserDAO();
		roleDAO = new RoleDAO();
		clientDAO = new ClientDAO();
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
			listUsers(request, response);
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
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid user action");
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

			addUser(request, response);

		} else if ("update".equals(action)) {

			updateUser(request, response);

		} else {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
		}
	}

	// =========================================================
	// LIST USERS
	// =========================================================

	private void listUsers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<User> users = userDAO.getAllUsers();

		request.setAttribute("users", users);

		request.getRequestDispatcher("/user-list.jsp").forward(request, response);
	}

	// =========================================================
	// ADD FORM
	// =========================================================

	private void showAddForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Role> roles = roleDAO.getAllRoles();

		List<Client> clients = clientDAO.getAllClients();

		request.setAttribute("roles", roles);
		request.setAttribute("clients", clients);

		request.getRequestDispatcher("/user-form.jsp").forward(request, response);
	}

	// =========================================================
	// EDIT FORM
	// =========================================================

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idParameter = request.getParameter("id");

		if (idParameter == null || idParameter.isBlank()) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");

			return;
		}

		try {

			int userId = Integer.parseInt(idParameter);

			User user = userDAO.getUserById(userId);

			if (user == null) {

				response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");

				return;
			}

			List<Role> roles = roleDAO.getAllRoles();

			List<Client> clients = clientDAO.getAllClients();

			request.setAttribute("user", user);
			request.setAttribute("roles", roles);
			request.setAttribute("clients", clients);

			request.getRequestDispatcher("/user-form.jsp").forward(request, response);

		} catch (NumberFormatException e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid User ID");
		}
	}

	// =========================================================
	// ADD USER
	// =========================================================

	private void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {

			HttpSession session = request.getSession(false);

			if (session == null || session.getAttribute("userId") == null) {

				response.sendRedirect(request.getContextPath() + "/login.jsp");

				return;
			}

			User user = readUserFromRequest(request);

			user.setStatus("ACTIVE");

			boolean success = userDAO.addUser(user);

			if (success) {

				response.sendRedirect(request.getContextPath() + "/users?action=list");

			} else {

				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to add user");
			}

		} catch (Exception e) {

			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user data: " + e.getMessage());
		}
	}

	// =========================================================
	// UPDATE USER
	// =========================================================

	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {

			// -------------------------------------------------
			// GET USER ID
			// -------------------------------------------------

			String userIdParameter = request.getParameter("userId");

			System.out.println("UPDATE USER - userId parameter = " + userIdParameter);

			if (userIdParameter == null || userIdParameter.isBlank()) {

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");

				return;
			}

			int userId = Integer.parseInt(userIdParameter);

			// -------------------------------------------------
			// READ FORM DATA
			// -------------------------------------------------

			User user = readUserFromRequest(request);

			user.setUserId(userId);

			// -------------------------------------------------
			// IMPORTANT:
			// Keep existing status if form does not send status
			// -------------------------------------------------

			String status = request.getParameter("status");

			if (status == null || status.isBlank()) {

				User existingUser = userDAO.getUserById(userId);

				if (existingUser != null) {
					user.setStatus(existingUser.getStatus());
				} else {
					user.setStatus("ACTIVE");
				}

			} else {

				user.setStatus(status);
			}

			// -------------------------------------------------
			// UPDATE
			// -------------------------------------------------

			System.out.println("UPDATE USER - ID = " + user.getUserId());

			System.out.println("UPDATE USER - Username = " + user.getUsername());

			System.out.println("UPDATE USER - First Name = " + user.getFirstName());

			System.out.println("UPDATE USER - Last Name = " + user.getLastName());

			System.out.println("UPDATE USER - Email = " + user.getEmail());

			System.out.println("UPDATE USER - Phone = " + user.getPhone());

			System.out.println("UPDATE USER - Role ID = " + user.getRoleId());

			System.out.println("UPDATE USER - Client ID = " + user.getClientId());

			System.out.println("UPDATE USER - Status = " + user.getStatus());

			boolean success = userDAO.updateUser(user);

			// -------------------------------------------------
			// SUCCESS
			// -------------------------------------------------

			if (success) {

				response.sendRedirect(request.getContextPath() + "/users?action=list");

			} else {

				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to update user");
			}

		} catch (NumberFormatException e) {

			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid User ID");

		} catch (Exception e) {

			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Unable to update user: " + e.getMessage());
		}
	}

	// =========================================================
	// ACTIVATE / DEACTIVATE
	// =========================================================

	private void updateStatus(HttpServletRequest request, HttpServletResponse response, String status)
			throws IOException {

		try {

			String idParameter = request.getParameter("id");

			if (idParameter == null || idParameter.isBlank()) {

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");

				return;
			}

			int userId = Integer.parseInt(idParameter);

			boolean success = userDAO.updateStatus(userId, status);

			if (success) {

				response.sendRedirect(request.getContextPath() + "/users?action=list");

			} else {

				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to update user status");
			}

		} catch (NumberFormatException e) {

			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid User ID");

		} catch (Exception e) {

			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to update user status");
		}
	}

	// =========================================================
	// READ FORM DATA
	// =========================================================

	private User readUserFromRequest(HttpServletRequest request) {

		User user = new User();

		// -----------------------------------------------------
		// USERNAME
		// -----------------------------------------------------

		user.setUsername(request.getParameter("username"));

		// -----------------------------------------------------
		// PASSWORD
		// -----------------------------------------------------

		user.setPasswordHash(request.getParameter("passwordHash"));

		// -----------------------------------------------------
		// FIRST NAME
		// -----------------------------------------------------

		user.setFirstName(request.getParameter("firstName"));

		// -----------------------------------------------------
		// LAST NAME
		// -----------------------------------------------------

		user.setLastName(request.getParameter("lastName"));

		// -----------------------------------------------------
		// EMAIL
		// -----------------------------------------------------

		user.setEmail(request.getParameter("email"));

		// -----------------------------------------------------
		// PHONE
		// -----------------------------------------------------

		user.setPhone(request.getParameter("phone"));

		// -----------------------------------------------------
		// ROLE
		// -----------------------------------------------------

		String roleId = request.getParameter("roleId");

		if (roleId != null && !roleId.isBlank()) {

			user.setRoleId(Integer.parseInt(roleId));
		}

		// -----------------------------------------------------
		// CLIENT
		// -----------------------------------------------------

		String clientId = request.getParameter("clientId");

		if (clientId != null && !clientId.isBlank()) {

			user.setClientId(Integer.parseInt(clientId));

		} else {

			user.setClientId(null);
		}

		return user;
	}
}