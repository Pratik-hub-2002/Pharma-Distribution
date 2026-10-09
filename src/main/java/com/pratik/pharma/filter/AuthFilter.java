package com.pratik.pharma.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.pratik.pharma.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

	private final Map<String, Set<String>> rolePermissions = new HashMap<>();

	// =========================================================
	// INIT
	// =========================================================

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		// =====================================================
		// SUPER ADMIN
		// =====================================================

		Set<String> superAdmin = new HashSet<>();

		superAdmin.add("dashboard");
		superAdmin.add("users");
		superAdmin.add("clients");
		superAdmin.add("client-documents");
		superAdmin.add("products");
		superAdmin.add("batches");
		superAdmin.add("inventory");
		superAdmin.add("stock-transactions");
		superAdmin.add("schemes");
		superAdmin.add("orders");
		superAdmin.add("shipments");
		superAdmin.add("invoices");
		superAdmin.add("payments");
		superAdmin.add("accountant-billing");

		rolePermissions.put("SUPER_ADMIN", superAdmin);

		// =====================================================
		// INVENTORY CLERK
		// =====================================================

		Set<String> inventoryClerk = new HashSet<>();

		inventoryClerk.add("dashboard");
		inventoryClerk.add("products");
		inventoryClerk.add("batches");
		inventoryClerk.add("inventory");
		inventoryClerk.add("stock-transactions");

		rolePermissions.put("INVENTORY_CLERK", inventoryClerk);

		// =====================================================
		// SALES REPRESENTATIVE
		// =====================================================

		Set<String> salesRep = new HashSet<>();

		salesRep.add("dashboard");
		salesRep.add("clients");
		salesRep.add("client-documents");
		salesRep.add("products");
		salesRep.add("schemes");
		salesRep.add("orders");

		rolePermissions.put("SALES_REP", salesRep);

		// =====================================================
		// ACCOUNTANT
		// =====================================================

		Set<String> accountant = new HashSet<>();

		accountant.add("dashboard");
		accountant.add("clients");
		accountant.add("invoices");
		accountant.add("payments");
		accountant.add("accountant-billing");

		rolePermissions.put("ACCOUNTANT", accountant);

		// =====================================================
		// DISPATCH MANAGER
		// =====================================================

		Set<String> dispatchManager = new HashSet<>();

		dispatchManager.add("dashboard");
		dispatchManager.add("orders");
		dispatchManager.add("shipments");

		rolePermissions.put("DISPATCH_MANAGER", dispatchManager);

		// =====================================================
		// CLIENT
		// =====================================================

		Set<String> client = new HashSet<>();

		client.add("dashboard");
		client.add("orders");
		client.add("invoices");

		rolePermissions.put("CLIENT", client);
	}

	// =========================================================
	// DO FILTER
	// =========================================================

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String path = httpRequest.getRequestURI();

		String contextPath = httpRequest.getContextPath();

		String relativePath = path.substring(contextPath.length());

		// =====================================================
		// PUBLIC RESOURCES
		// =====================================================

		if (isPublicResource(relativePath)) {

			chain.doFilter(request, response);

			return;
		}

		// =====================================================
		// SESSION CHECK
		// =====================================================

		HttpSession session = httpRequest.getSession(false);

		if (session == null || session.getAttribute("loggedInUser") == null) {

			httpResponse.sendRedirect(contextPath + "/login.jsp");

			return;
		}

		// =====================================================
		// GET LOGGED-IN USER
		// =====================================================

		User loggedInUser = (User) session.getAttribute("loggedInUser");

		String roleName = loggedInUser.getRoleName();

		// =====================================================
		// DETERMINE MODULE
		// =====================================================

		String module = getModule(relativePath);

		// =====================================================
		// UNKNOWN RESOURCE
		// =====================================================

		if (module == null) {

			chain.doFilter(request, response);

			return;
		}

		// =====================================================
		// DASHBOARD
		// =====================================================

		if ("dashboard".equals(module)) {

			chain.doFilter(request, response);

			return;
		}

		// =====================================================
		// CHECK PERMISSION
		// =====================================================

		Set<String> permissions = rolePermissions.get(roleName);

		if (permissions != null && permissions.contains(module)) {

			chain.doFilter(request, response);

			return;
		}

		// =====================================================
		// ACCESS DENIED
		// =====================================================

		httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this module.");
	}

	// =========================================================
	// GET MODULE
	// =========================================================

	private String getModule(String path) {

		if (path.equals("/dashboard.jsp")) {
			return "dashboard";
		}

		if (path.equals("/users")) {
			return "users";
		}

		if (path.equals("/clients")) {
			return "clients";
		}

		if (path.equals("/client-documents")) {
			return "client-documents";
		}

		if (path.equals("/products")) {
			return "products";
		}

		if (path.equals("/batches")) {
			return "batches";
		}

		if (path.equals("/inventory")) {
			return "inventory";
		}

		if (path.equals("/stock-transactions")) {
			return "stock-transactions";
		}

		if (path.equals("/schemes")) {
			return "schemes";
		}

		if (path.equals("/orders")) {
			return "orders";
		}

		if (path.equals("/shipments")) {
			return "shipments";
		}

		if (path.equals("/invoices")) {
			return "invoices";
		}
		if (path.equals("/accountant-billing")) {
		    return "accountant-billing";
		}

		if (path.equals("/payments")) {
			return "payments";
		}
		

		return null;
	}

	// =========================================================
	// PUBLIC RESOURCE
	// =========================================================

	private boolean isPublicResource(String path) {

		if (path.equals("/login.jsp")) {
			return true;
		}

		if (path.equals("/login")) {
			return true;
		}

		if (path.equals("/logout")) {
			return true;
		}

		if (path.equals("/favicon.ico")) {
			return true;
		}

		if (path.startsWith("/css/")) {
			return true;
		}

		if (path.startsWith("/js/")) {
			return true;
		}

		if (path.startsWith("/images/")) {
			return true;
		}

		return false;
	}
}