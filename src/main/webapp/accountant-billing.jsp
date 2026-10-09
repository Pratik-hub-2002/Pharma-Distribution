<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Order"%>

<%
List<Order> orders = (List<Order>) request.getAttribute("orders");

String successMessage = (String) session.getAttribute("successMessage");

String errorMessage = (String) session.getAttribute("errorMessage");

if (successMessage != null) {
	session.removeAttribute("successMessage");
}

if (errorMessage != null) {
	session.removeAttribute("errorMessage");
}
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Accountant Billing</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<style>
body {
	background-color: #f5f7fa;
}

.page-header {
	background: #198754;
	color: white;
	padding: 22px;
	border-radius: 8px;
}

.card {
	border: none;
}

.summary-number {
	font-size: 30px;
	font-weight: 700;
}
</style>

</head>

<body>

	<div class="container mt-5 mb-5">

		<!-- =====================================================
         HEADER
    ====================================================== -->

		<div class="page-header shadow-sm mb-4">

			<div class="d-flex justify-content-between align-items-center">

				<div>

					<h2 class="mb-1">🧾 Accountant Billing</h2>

					<p class="mb-0">Create invoices for dispatcher-approved orders.
					</p>

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

			<strong>Workflow:</strong> Dispatcher Approval → Accountant Billing →
			Invoice → Payment → Shipment → Delivery

		</div>


		<!-- =====================================================
         SUMMARY
    ====================================================== -->

		<div class="row mb-4">

			<div class="col-md-4">

				<div class="card shadow-sm">

					<div class="card-body">

						<h6 class="text-muted">Orders Ready for Billing</h6>

						<div class="summary-number text-success">

							<%=orders != null ? orders.size() : 0%>

						</div>

					</div>

				</div>

			</div>

			<div class="col-md-8">

				<div class="card shadow-sm">

					<div class="card-body">

						<h6 class="text-muted">Billing Rule</h6>

						<p class="mb-0">

							Only orders with status <strong>APPROVED</strong> and without an
							existing invoice can be billed.

						</p>

					</div>

				</div>

			</div>

		</div>


		<!-- =====================================================
         APPROVED ORDERS
    ====================================================== -->

		<div class="card shadow-sm">

			<div class="card-header bg-success text-white">

				<h5 class="mb-0">✓ Orders Ready for Billing</h5>

			</div>


			<div class="card-body">

				<%
				if (orders == null || orders.isEmpty()) {
				%>

				<div class="text-center py-5">

					<div style="font-size: 50px;">📭</div>

					<h5 class="mt-3">No orders ready for billing</h5>

					<p class="text-muted">Only dispatcher-approved orders without
						an invoice appear here.</p>

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

								<th>Grand Total</th>

								<th>Credit</th>

								<th>Status</th>

								<th>Action</th>

							</tr>

						</thead>


						<tbody>

							<%
							for (Order order : orders) {
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


								<td><span class="badge bg-warning text-dark"> <%=order.getCreditStatus()%>

								</span></td>


								<td><span class="badge bg-success"> APPROVED </span></td>


								<td><a
									href="accountant-billing?action=create&orderId=<%=order.getOrderId()%>"
									class="btn btn-sm btn-success"
									onclick="return confirm('Create invoice for Order #<%=order.getOrderId()%>?');">

										🧾 Create Invoice </a></td>

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
         INFORMATION
    ====================================================== -->

		<div class="card shadow-sm mt-4">

			<div class="card-body">

				<h5>📌 Billing Rules</h5>

				<ul class="mb-0">

					<li>Only <strong>APPROVED</strong> orders are eligible for
						billing.
					</li>

					<li>An order with an existing invoice cannot receive another
						invoice.</li>

					<li>New invoices are initially created with status <strong>PENDING</strong>.
					</li>

					<li>Payment is handled separately by the Payment module.</li>

					<li>Payment does not automatically mean the order is
						completed.</li>

				</ul>

			</div>

		</div>

	</div>


	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
		
	</script>

</body>

</html>