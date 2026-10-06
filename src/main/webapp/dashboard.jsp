<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="com.pratik.pharma.model.User"%>

<%
User loggedInUser = (User) session.getAttribute("loggedInUser");

if (loggedInUser == null) {
	response.sendRedirect("login.jsp");
	return;
}

String username = loggedInUser.getUsername();
String role = loggedInUser.getRoleName();

if (role == null) {
	role = "";
}
%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Pharma Distribution Dashboard</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<style>
body {
	background-color: #f5f7fb;
}

.navbar-brand {
	font-weight: 700;
}

.dashboard-title {
	margin-top: 30px;
}

.module-card {
	border: none;
	border-radius: 15px;
	transition: 0.2s;
	height: 100%;
}

.module-card:hover {
	transform: translateY(-4px);
}

.module-icon {
	font-size: 40px;
}

.role-badge {
	font-size: 13px;
}

.section-title {
	margin-top: 35px;
	margin-bottom: 20px;
	font-weight: 700;
}
</style>

</head>

<body>

	<!-- =========================================================
     NAVBAR
========================================================= -->

	<nav class="navbar navbar-dark bg-dark">

		<div class="container">

			<a class="navbar-brand" href="dashboard.jsp"> 💊 Pharma
				Distribution </a>

			<div class="d-flex align-items-center">

				<span class="text-white me-3"> Welcome, <strong><%=username%></strong>

					<span class="badge bg-primary role-badge ms-2"> <%=role%>
				</span>

				</span> <a href="logout" class="btn btn-outline-light btn-sm"> Logout </a>

			</div>

		</div>

	</nav>


	<!-- =========================================================
     MAIN
========================================================= -->

	<div class="container">

		<div class="dashboard-title">

			<h1>Pharma Distribution Dashboard</h1>

			<p class="text-muted">Manage clients, products, inventory,
				orders, shipments, invoices and payments.</p>

		</div>


		<!-- =====================================================
         SUPER ADMIN
    ====================================================== -->

		<%
		if ("SUPER_ADMIN".equals(role)) {
		%>

		<h3 class="section-title">Master Management</h3>

		<div class="row g-4">

			<!-- =================================================
             USER MANAGEMENT
        ================================================== -->

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">👤</div>

						<h5>User Management</h5>

						<p class="text-muted">Manage system users, roles and access
							permissions.</p>

						<a href="users?action=list" class="btn btn-primary"> Manage
							Users </a>

					</div>

				</div>

			</div>


			<!-- =================================================
             CLIENTS
        ================================================== -->

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">👥</div>

						<h5>Clients</h5>

						<p class="text-muted">Manage medical stores, hospitals and
							registered clients.</p>

						<a href="clients?action=list" class="btn btn-primary"> Manage
							Clients </a>

					</div>

				</div>

			</div>


			<!-- =================================================
             PRODUCTS
        ================================================== -->

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">💊</div>

						<h5>Products</h5>

						<p class="text-muted">Manage medicines, HSN codes, GST rates
							and selling prices.</p>

						<a href="products?action=list" class="btn btn-primary"> Manage
							Products </a>

					</div>

				</div>

			</div>


			<!-- =================================================
             BATCHES
        ================================================== -->

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">📦</div>

						<h5>Batches</h5>

						<p class="text-muted">Manage medicine batches, manufacturing
							dates and expiry dates.</p>

						<a href="batches?action=list" class="btn btn-primary"> Manage
							Batches </a>

					</div>

				</div>

			</div>


			<!-- =================================================
             INVENTORY
        ================================================== -->

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🏬</div>

						<h5>Inventory</h5>

						<p class="text-muted">Monitor available stock, reserved and
							damaged quantities.</p>

						<a href="inventory?action=list" class="btn btn-primary"> View
							Inventory </a>

					</div>

				</div>

			</div>


			<!-- =================================================
             STOCK TRANSACTIONS
        ================================================== -->

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🔄</div>

						<h5>Stock Transactions</h5>

						<p class="text-muted">Track stock IN, OUT and manual inventory
							movements.</p>

						<a href="stock-transactions?action=list" class="btn btn-primary">
							View Transactions </a>

					</div>

				</div>

			</div>


			<!-- =================================================
             SCHEMES
        ================================================== -->

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🏷️</div>

						<h5>Schemes</h5>

						<p class="text-muted">Manage promotional schemes for
							medicines.</p>

						<a href="schemes?action=list" class="btn btn-primary"> Manage
							Schemes </a>

					</div>

				</div>

			</div>

		</div>


		<!-- =====================================================
         SALES & ORDERS
    ====================================================== -->

		<h3 class="section-title">Sales &amp; Orders</h3>

		<div class="row g-4">


			<!-- ORDERS -->

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🛒</div>

						<h5>Customer Orders</h5>

						<p class="text-muted">Create and manage customer orders.</p>

						<a href="orders?action=list" class="btn btn-success"> Manage
							Orders </a>

					</div>

				</div>

			</div>


			<!-- DOCUMENTS -->

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">📄</div>

						<h5>Client Documents</h5>

						<p class="text-muted">Verify GST, drug licenses and purchase
							permits.</p>

						<a href="clients?action=list" class="btn btn-success"> Client
							Documents </a>

					</div>

				</div>

			</div>


			<!-- SHIPMENTS -->

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🚚</div>

						<h5>Shipments</h5>

						<p class="text-muted">Manage dispatch and delivery.</p>

						<a href="shipments?action=list" class="btn btn-success">
							Shipments </a>

					</div>

				</div>

			</div>


			<!-- INVOICES -->

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🧾</div>

						<h5>Invoices</h5>

						<p class="text-muted">Generate and view customer tax invoices.
						</p>

						<a href="invoices?action=list" class="btn btn-success">
							Invoices </a>

					</div>

				</div>

			</div>


			<!-- PAYMENTS -->

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">💰</div>

						<h5>Payments</h5>

						<p class="text-muted">Record payments and track outstanding
							invoice amounts.</p>

						<a href="payments?action=list" class="btn btn-success">
							Payments </a>

					</div>

				</div>

			</div>

		</div>


		<!-- =====================================================
         QUICK ACTIONS
    ====================================================== -->

		<h3 class="section-title">Quick Actions</h3>

		<div class="mb-5">

			<a href="orders?action=add" class="btn btn-success me-2"> 🛒
				Create Order </a> <a href="payments?action=add&invoiceId=1"
				class="btn btn-warning me-2"> 💰 Record Payment </a> <a
				href="shipments?action=list" class="btn btn-info me-2"> 🚚
				Shipments </a> <a href="clients?action=list" class="btn btn-secondary">
				👥 Clients </a>

		</div>

		<%
		}
		%>


		<!-- =====================================================
         INVENTORY CLERK
    ====================================================== -->

		<%
		if ("INVENTORY_CLERK".equals(role)) {
		%>

		<h3 class="section-title">Inventory Management</h3>

		<div class="row g-4">

			<div class="col-md-6 col-lg-3">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">💊</div>

						<h5>Products</h5>

						<p class="text-muted">View medicine information.</p>

						<a href="products?action=list" class="btn btn-primary">
							Products </a>

					</div>

				</div>

			</div>


			<div class="col-md-6 col-lg-3">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">📦</div>

						<h5>Batches</h5>

						<p class="text-muted">Manage batches and expiry.</p>

						<a href="batches?action=list" class="btn btn-primary"> Batches
						</a>

					</div>

				</div>

			</div>


			<div class="col-md-6 col-lg-3">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🏬</div>

						<h5>Inventory</h5>

						<p class="text-muted">Monitor available stock.</p>

						<a href="inventory?action=list" class="btn btn-primary">
							Inventory </a>

					</div>

				</div>

			</div>


			<div class="col-md-6 col-lg-3">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🔄</div>

						<h5>Stock Transactions</h5>

						<p class="text-muted">Track stock movements.</p>

						<a href="stock-transactions?action=list" class="btn btn-primary">
							Transactions </a>

					</div>

				</div>

			</div>

		</div>

		<%
		}
		%>


		<!-- =====================================================
         SALES REPRESENTATIVE
    ====================================================== -->

		<%
		if ("SALES_REP".equals(role)) {
		%>

		<h3 class="section-title">Sales Management</h3>

		<div class="row g-4">

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">👥</div>

						<h5>Clients</h5>

						<p class="text-muted">Manage registered clients.</p>

						<a href="clients?action=list" class="btn btn-primary"> Clients
						</a>

					</div>

				</div>

			</div>


			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">💊</div>

						<h5>Products</h5>

						<p class="text-muted">View medicines and prices.</p>

						<a href="products?action=list" class="btn btn-primary">
							Products </a>

					</div>

				</div>

			</div>


			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">📄</div>

						<h5>Client Documents</h5>

						<p class="text-muted">Verify client documents.</p>

						<a href="clients?action=list" class="btn btn-primary">
							Documents </a>

					</div>

				</div>

			</div>


			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🏷️</div>

						<h5>Schemes</h5>

						<p class="text-muted">Manage promotional schemes.</p>

						<a href="schemes?action=list" class="btn btn-primary"> Schemes
						</a>

					</div>

				</div>

			</div>


			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🛒</div>

						<h5>Orders</h5>

						<p class="text-muted">Create and manage customer orders.</p>

						<a href="orders?action=list" class="btn btn-success"> Orders </a>

					</div>

				</div>

			</div>

		</div>

		<%
		}
		%>


		<!-- =====================================================
         ACCOUNTANT
    ====================================================== -->

		<%
		if ("ACCOUNTANT".equals(role)) {
		%>

		<h3 class="section-title">Finance Management</h3>

		<div class="row g-4">

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">👥</div>

						<h5>Clients</h5>

						<p class="text-muted">View registered clients.</p>

						<a href="clients?action=list" class="btn btn-primary"> Clients
						</a>

					</div>

				</div>

			</div>


			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🧾</div>

						<h5>Invoices</h5>

						<p class="text-muted">Manage customer invoices.</p>

						<a href="invoices?action=list" class="btn btn-success">
							Invoices </a>

					</div>

				</div>

			</div>


			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">💰</div>

						<h5>Payments</h5>

						<p class="text-muted">Record payments and balances.</p>

						<a href="payments?action=list" class="btn btn-success">
							Payments </a>

					</div>

				</div>

			</div>

		</div>

		<%
		}
		%>


		<!-- =====================================================
         DISPATCH MANAGER
    ====================================================== -->

		<%
		if ("DISPATCH_MANAGER".equals(role)) {
		%>

		<h3 class="section-title">Dispatch Management</h3>

		<div class="row g-4">

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🛒</div>

						<h5>Orders</h5>

						<p class="text-muted">View orders ready for dispatch.</p>

						<a href="orders?action=list" class="btn btn-primary"> Orders </a>

					</div>

				</div>

			</div>


			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🚚</div>

						<h5>Shipments</h5>

						<p class="text-muted">Manage dispatch and delivery.</p>

						<a href="shipments?action=list" class="btn btn-success">
							Shipments </a>

					</div>

				</div>

			</div>

		</div>

		<%
		}
		%>


		<!-- =====================================================
         CLIENT
    ====================================================== -->

		<%
		if ("CLIENT".equals(role)) {
		%>

		<h3 class="section-title">Client Portal</h3>

		<div class="row g-4">

			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🛒</div>

						<h5>My Orders</h5>

						<p class="text-muted">View your orders.</p>

						<a href="orders?action=list" class="btn btn-primary"> My
							Orders </a>

					</div>

				</div>

			</div>


			<div class="col-md-6 col-lg-4">

				<div class="card module-card shadow-sm">

					<div class="card-body">

						<div class="module-icon">🧾</div>

						<h5>My Invoices</h5>

						<p class="text-muted">View your invoices.</p>

						<a href="invoices?action=list" class="btn btn-success"> My
							Invoices </a>

					</div>

				</div>

			</div>

		</div>

		<%
		}
		%>

	</div>


	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
		
	</script>

</body>

</html>