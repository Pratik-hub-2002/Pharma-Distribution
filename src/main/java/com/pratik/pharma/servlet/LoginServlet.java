package com.pratik.pharma.servlet;

import java.io.IOException;

import com.pratik.pharma.dao.UserDAO;
import com.pratik.pharma.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private UserDAO userDAO;

	@Override
	public void init() throws ServletException {
		userDAO = new UserDAO();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		System.out.println("================================");
		System.out.println("LoginServlet received POST request");
		System.out.println("Username: [" + username + "]");
		System.out.println("Password received: " + (password != null ? "YES, length = " + password.length() : "NULL"));
		System.out.println("================================");

		User user = userDAO.login(username, password);

		if (user != null) {

			System.out.println("Login successful");

			HttpSession session = request.getSession();

			// Store complete user object
			session.setAttribute("loggedInUser", user);

			// Store user information separately
			session.setAttribute("userId", user.getUserId());
			session.setAttribute("username", user.getUsername());

			response.sendRedirect("dashboard.jsp");

		} else {

			System.out.println("Login failed");

			request.setAttribute("error", "Invalid username or password");

			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
}