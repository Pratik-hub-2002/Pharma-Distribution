<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Order"%>
<%@ page import="com.pratik.pharma.model.OrderItem"%>

<%
Order order = (Order) request.getAttribute("order");

List<OrderItem> items = (List<OrderItem>) request.getAttribute("items");

String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Review Order</title>

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

.summary-card {
	border: none;
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

					<h2 class="mb-1">

						📦 Review Order #<%=order != null ? order.getOrderId() : ""%>

					</h2>

					<p class="mb-0">Dispatcher verification before billing.</p>

				</div>


				<a href="dispatch-orders" class="btn btn-light"> ← Back </a>

			</div>

		</div>


		<!-- =====================================================
         ERROR
    ====================================================== -->

		<%
		if (error != null) {
		%>

		<div class="alert alert-danger">

			<%=error%>

		</div>

		<%
		}
		%>


		<%
		if (order == null) {
		%>

		<div class="alert alert-danger">Order not found.</div>

		<%
		} else {
		%>


		<!-- =====================================================
         ORDER SUMMARY
    ====================================================== -->

		<div class="card shadow-sm summary-card mb-4">

			<div class="card-body">

				<div class="row">


					<div class="col-md-4">

						<h6 class="text-muted">Order Number</h6>

						<h4>
							#<%=order.getOrderId()%>
						</h4>

					</div>


					<div class="col-md-4">

						<h6 class="text-muted">Client</h6>

						<h4>
							<%=order.getClientName()%>
						</h4>

					</div>


					<div class="col-md-4">

						<h6 class="text-muted">Order Date</h6>

						<h5>
							<%=order.getOrderDate()%>
						</h5>

					</div>

				</div>


				<hr>


				<div class="row">


					<div class="col-md-3">

						<strong> Order Status </strong> <br> <span
							class="badge bg-warning text-dark mt-1"> <%=order.getOrderStatus()%>

						</span>

					</div>


					<div class="col-md-3">

						<strong> Credit Status </strong> <br>

						<%
						if ("PAID".equalsIgnoreCase(order.getCreditStatus())) {
						%>

						<span class="badge bg-success mt-1"> PAID </span>

						<%
						} else {
						%>

						<span class="badge bg-warning text-dark mt-1"> <%=order.getCreditStatus()%>

						</span>

						<%
						}
						%>

					</div>


					<div class="col-md-3">

						<strong> Subtotal </strong> <br> ₹<%=order.getSubtotal()%>

					</div>


					<div class="col-md-3">

						<strong> Grand Total </strong> <br> <span class="fw-bold">

							₹<%=order.getTotalAmount()%>

						</span>

					</div>

				</div>

			</div>

		</div>


		<!-- =====================================================
         ORDER ITEMS
    ====================================================== -->

		<div class="card shadow-sm mb-4">

			<div class="card-header bg-dark text-white">

				<h5 class="mb-0">Ordered Products</h5>

			</div>


			<div class="card-body">


				<%
				if (items == null || items.isEmpty()) {
				%>

				<div class="alert alert-warning">No order items found.</div>

				<%
				} else {
				%>


				<div class="table-responsive">

					<table class="table table-bordered table-hover align-middle">

						<thead class="table-dark">

							<tr>

								<th>#</th>

								<th>Product</th>

								<th>Ordered Qty</th>

								<th>Free Qty</th>

								<th>Total Qty</th>

								<th>Unit Price</th>

								<th>Discount</th>

								<th>GST</th>

								<th>Line Total</th>

							</tr>

						</thead>


						<tbody>


							<%
							int serialNumber = 1;

							for (OrderItem item : items) {
							%>

							<tr>

								<td><%=serialNumber++%></td>


								<td><strong> <%=item.getProductName()%>
								</strong></td>


								<td><%=item.getOrderedQuantity()%></td>


								<td><%=item.getFreeQuantity()%></td>


								<td class="fw-bold"><%=item.getTotalQuantity()%></td>


								<td>₹<%=item.getUnitPrice()%>
								</td>


								<td>₹<%=item.getDiscountAmount()%>
								</td>


								<td>₹<%=item.getTaxAmount()%>
								</td>


								<td class="fw-bold">₹<%=item.getLineTotal()%>
								</td>

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
         DISPATCHER DECISION
    ====================================================== -->

		<%
		if ("PENDING".equalsIgnoreCase(order.getOrderStatus())) {
		%>


		<div class="card shadow-sm mb-5">

			<div class="card-header bg-primary text-white">

				<h5 class="mb-0">Dispatcher Decision</h5>

			</div>


			<div class="card-body">


				<div class="alert alert-info">Review the ordered products and
					confirm that the order can proceed to billing.</div>


				<div class="d-flex gap-2">


					<!-- APPROVE -->

					<form action="dispatch-orders" method="post">

						<input type="hidden" name="action" value="approve"> <input
							type="hidden" name="orderId" value="<%=order.getOrderId()%>">

						<button type="submit" class="btn btn-success">✓ Approve
							Order</button>

					</form>


					<!-- HOLD -->

					<form action="dispatch-orders" method="post">

						<input type="hidden" name="action" value="hold"> <input
							type="hidden" name="orderId" value="<%=order.getOrderId()%>">

						<button type="submit" class="btn btn-warning">⏸ Put On
							Hold</button>

					</form>


					<a href="dispatch-orders" class="btn btn-secondary"> Cancel </a>

				</div>

			</div>

		</div>


		<%
		}
		%>


		<%
		}
		%>


	</div>


</body>

</html>