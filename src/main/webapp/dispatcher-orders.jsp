<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Order"%>

<%
List<Order> pendingOrders = (List<Order>) request.getAttribute("pendingOrders");

List<Order> onHoldOrders = (List<Order>) request.getAttribute("onHoldOrders");

List<Order> approvedOrders = (List<Order>) request.getAttribute("approvedOrders");

List<Order> allOrders = (List<Order>) request.getAttribute("allOrders");

String successMessage = (String) request.getAttribute("successMessage");

String errorMessage = (String) request.getAttribute("errorMessage");
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Dispatcher Order Management</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<style>
body {
	background-color: #f5f7fa;
}

.page-header {
	background: #0d6efd;
	color: white;
	padding: 20px;
	border-radius: 8px;
}

.card {
	border: none;
}

.section-title {
	font-weight: 600;
}

.status-badge {
	font-size: 13px;
}

.summary-number {
	font-size: 28px;
	font-weight: 700;
}
</style>

</head>

<body>

	<div class="container mt-5">

		<!-- =====================================================
         HEADER
    ====================================================== -->

		<div class="page-header shadow-sm mb-4">

			<div class="d-flex justify-content-between align-items-center">

				<div>

					<h2 class="mb-1">🚚 Dispatcher Order Management</h2>

					<p class="mb-0">Review and manage orders before billing.</p>

				</div>

				<a href="dashboard.jsp" class="btn btn-light"> ← Dashboard </a>

			</div>

		</div>


		<!-- =====================================================
         SUCCESS MESSAGE
    ====================================================== -->

		<%
		if (successMessage != null) {
		%>

		<div class="alert alert-success alert-dismissible fade show">

			<strong>Success:</strong>

			<%=successMessage%>

			<button type="button" class="btn-close" data-bs-dismiss="alert">
			</button>

		</div>

		<%
		}
		%>


		<!-- =====================================================
         ERROR MESSAGE
    ====================================================== -->

		<%
		if (errorMessage != null) {
		%>

		<div class="alert alert-danger alert-dismissible fade show">

			<strong>Error:</strong>

			<%=errorMessage%>

			<button type="button" class="btn-close" data-bs-dismiss="alert">
			</button>

		</div>

		<%
		}
		%>


		<!-- =====================================================
         WORKFLOW
    ====================================================== -->

		<div class="alert alert-info">

			<strong>Workflow:</strong> New Order → Dispatcher Review → Approved /
			On Hold → Accountant Billing → Payment → Shipment → Delivery

		</div>


		<!-- =====================================================
         SUMMARY CARDS
    ====================================================== -->

		<div class="row g-3 mb-4">

			<!-- PENDING -->

			<div class="col-md-3">

				<div class="card shadow-sm">

					<div class="card-body">

						<h6 class="text-muted">Pending Review</h6>

						<div class="summary-number text-warning">

							<%=pendingOrders != null ? pendingOrders.size() : 0%>

						</div>

					</div>

				</div>

			</div>


			<!-- ON HOLD -->

			<div class="col-md-3">

				<div class="card shadow-sm">

					<div class="card-body">

						<h6 class="text-muted">On Hold</h6>

						<div class="summary-number text-danger">

							<%=onHoldOrders != null ? onHoldOrders.size() : 0%>

						</div>

					</div>

				</div>

			</div>


			<!-- APPROVED -->

			<div class="col-md-3">

				<div class="card shadow-sm">

					<div class="card-body">

						<h6 class="text-muted">Approved</h6>

						<div class="summary-number text-success">

							<%=approvedOrders != null ? approvedOrders.size() : 0%>

						</div>

					</div>

				</div>

			</div>


			<!-- TOTAL -->

			<div class="col-md-3">

				<div class="card shadow-sm">

					<div class="card-body">

						<h6 class="text-muted">Total Orders</h6>

						<div class="summary-number text-primary">

							<%=allOrders != null ? allOrders.size() : 0%>

						</div>

					</div>

				</div>

			</div>

		</div>


		<!-- =====================================================
         PENDING REVIEW
    ====================================================== -->

		<div class="card shadow-sm mb-4">

			<div class="card-header bg-warning">

				<h5 class="section-title mb-0">⏳ Orders Awaiting Dispatcher
					Review</h5>

			</div>


			<div class="card-body">

				<%
				if (pendingOrders == null || pendingOrders.isEmpty()) {
				%>

				<div class="text-center py-4">

					<div style="font-size: 40px;">✅</div>

					<h5 class="mt-2">No pending orders</h5>

					<p class="text-muted">There are no orders waiting for review.</p>

				</div>

				<%
				} else {
				%>

				<div class="table-responsive">

					<table class="table table-bordered table-hover align-middle">

						<thead class="table-dark">

							<tr>

								<th>Order #</th>

								<th>Client</th>

								<th>Order Date</th>

								<th>Subtotal</th>

								<th>Discount</th>

								<th>GST</th>

								<th>Total</th>

								<th>Credit</th>

								<th>Status</th>

								<th>Action</th>

							</tr>

						</thead>


						<tbody>

							<%
							for (Order order : pendingOrders) {
							%>

							<tr>

								<td class="fw-bold">#<%=order.getOrderId()%>

								</td>

								<td><%=order.getClientName()%></td>

								<td><%=order.getOrderDate()%></td>

								<td>₹<%=order.getSubtotal()%>
								</td>

								<td>₹<%=order.getDiscountAmount()%>
								</td>

								<td>₹<%=order.getTaxAmount()%>
								</td>

								<td class="fw-bold">₹<%=order.getTotalAmount()%>
								</td>

								<td>
									<%
									if ("PAID".equalsIgnoreCase(order.getCreditStatus())) {
									%>

									<span class="badge bg-success"> PAID </span> <%
 } else {
 %> <span
									class="badge bg-warning text-dark"> <%=order.getCreditStatus()%>

								</span> <%
 }
 %>

								</td>

								<td><span class="badge bg-warning text-dark status-badge">

										<%=order.getOrderStatus()%>

								</span></td>

								<td><a
									href="dispatch-orders?action=view&id=<%=order.getOrderId()%>"
									class="btn btn-sm btn-primary"> Review </a></td>

							</tr>

							<%
							}
							%>

						</tbody>

					</table>

				</div>

				<%
				}
				%>

			</div>

		</div>


		<!-- =====================================================
         ON HOLD
    ====================================================== -->

		<div class="card shadow-sm mb-4">

			<div class="card-header bg-danger text-white">

				<h5 class="section-title mb-0">⏸ Orders On Hold</h5>

			</div>


			<div class="card-body">

				<%
				if (onHoldOrders == null || onHoldOrders.isEmpty()) {
				%>

				<div class="text-center py-4">

					<p class="text-muted mb-0">No orders are currently on hold.</p>

				</div>

				<%
				} else {
				%>

				<div class="table-responsive">

					<table class="table table-bordered table-hover align-middle">

						<thead class="table-dark">

							<tr>

								<th>Order #</th>

								<th>Client</th>

								<th>Order Date</th>

								<th>Total</th>

								<th>Credit</th>

								<th>Status</th>

								<th>Action</th>

							</tr>

						</thead>


						<tbody>

							<%
							for (Order order : onHoldOrders) {
							%>

							<tr>

								<td class="fw-bold">#<%=order.getOrderId()%>
								</td>

								<td><%=order.getClientName()%></td>

								<td><%=order.getOrderDate()%></td>

								<td class="fw-bold">₹<%=order.getTotalAmount()%>
								</td>

								<td><%=order.getCreditStatus()%></td>

								<td><span class="badge bg-danger"> <%=order.getOrderStatus()%>

								</span></td>

								<td><a
									href="dispatch-orders?action=view&id=<%=order.getOrderId()%>"
									class="btn btn-sm btn-primary"> Review </a>

									<form action="dispatch-orders" method="post" class="d-inline">

										<input type="hidden" name="action" value="release"> <input
											type="hidden" name="orderId" value="<%=order.getOrderId()%>">

										<button type="submit" class="btn btn-sm btn-success">

											Release</button>

									</form></td>

							</tr>

							<%
							}
							%>

						</tbody>

					</table>

				</div>

				<%
				}
				%>

			</div>

		</div>


		<!-- =====================================================
         APPROVED
    ====================================================== -->

		<div class="card shadow-sm mb-4">

			<div class="card-header bg-success text-white">

				<h5 class="section-title mb-0">✓ Approved Orders</h5>

			</div>


			<div class="card-body">

				<%
				if (approvedOrders == null || approvedOrders.isEmpty()) {
				%>

				<div class="text-center py-4">

					<p class="text-muted mb-0">No approved orders yet.</p>

				</div>

				<%
				} else {
				%>

				<div class="table-responsive">

					<table class="table table-bordered table-hover align-middle">

						<thead class="table-dark">

							<tr>

								<th>Order #</th>

								<th>Client</th>

								<th>Order Date</th>

								<th>Subtotal</th>

								<th>GST</th>

								<th>Total</th>

								<th>Credit</th>

								<th>Status</th>

								<th>Action</th>

							</tr>

						</thead>


						<tbody>

							<%
							for (Order order : approvedOrders) {
							%>

							<tr>

								<td class="fw-bold">#<%=order.getOrderId()%>
								</td>

								<td><%=order.getClientName()%></td>

								<td><%=order.getOrderDate()%></td>

								<td>₹<%=order.getSubtotal()%>
								</td>

								<td>₹<%=order.getTaxAmount()%>
								</td>

								<td class="fw-bold">₹<%=order.getTotalAmount()%>
								</td>

								<td>
									<%
									if ("PAID".equalsIgnoreCase(order.getCreditStatus())) {
									%>

									<span class="badge bg-success"> PAID </span> <%
 } else {
 %> <span
									class="badge bg-warning text-dark"> <%=order.getCreditStatus()%>

								</span> <%
 }
 %>

								</td>

								<td><span class="badge bg-success"> APPROVED </span></td>

								<td><a
									href="dispatch-orders?action=view&id=<%=order.getOrderId()%>"
									class="btn btn-sm btn-outline-primary"> View </a></td>

							</tr>

							<%
							}
							%>

						</tbody>

					</table>

				</div>

				<%
				}
				%>

			</div>

		</div>


		<!-- =====================================================
         ALL ORDER HISTORY
    ====================================================== -->

		<div class="card shadow-sm mb-5">

			<div class="card-header bg-dark text-white">

				<h5 class="section-title mb-0">📋 Dispatcher Order History</h5>

			</div>


			<div class="card-body">

				<%
				if (allOrders == null || allOrders.isEmpty()) {
				%>

				<p class="text-muted">No order history available.</p>

				<%
				} else {
				%>

				<div class="table-responsive">

					<table class="table table-bordered table-hover align-middle">

						<thead class="table-dark">

							<tr>

								<th>Order #</th>

								<th>Client</th>

								<th>Order Date</th>

								<th>Total</th>

								<th>Credit</th>

								<th>Order Status</th>

								<th>Created By</th>

								<th>Action</th>

							</tr>

						</thead>


						<tbody>

							<%
							for (Order order : allOrders) {
							%>

							<tr>

								<td class="fw-bold">#<%=order.getOrderId()%>

								</td>

								<td><%=order.getClientName()%></td>

								<td><%=order.getOrderDate()%></td>

								<td class="fw-bold">₹<%=order.getTotalAmount()%>
								</td>

								<td><%=order.getCreditStatus()%></td>

								<td>
									<%
									if ("APPROVED".equalsIgnoreCase(order.getOrderStatus())) {
									%>

									<span class="badge bg-success"> APPROVED </span> <%
 } else if ("ON_HOLD".equalsIgnoreCase(order.getOrderStatus())) {
 %>

									<span class="badge bg-danger"> ON_HOLD </span> <%
 } else if ("PENDING".equalsIgnoreCase(order.getOrderStatus())) {
 %>

									<span class="badge bg-warning text-dark"> PENDING </span> <%
 } else {
 %>

									<span class="badge bg-secondary"> <%=order.getOrderStatus()%>

								</span> <%
 }
 %>

								</td>

								<td><%=order.getCreatedByName() != null ? order.getCreatedByName() : "-"%></td>

								<td><a
									href="dispatch-orders?action=view&id=<%=order.getOrderId()%>"
									class="btn btn-sm btn-outline-primary"> View </a></td>

							</tr>

							<%
							}
							%>

						</tbody>

					</table>

				</div>

				<%
				}
				%>

			</div>

		</div>

	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
		
	</script>

</body>

</html>