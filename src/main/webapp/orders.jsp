<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Order"%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Orders - Pharma Distribution</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body class="bg-light">

	<div class="container-fluid mt-4">

		<!-- HEADER -->

		<div class="d-flex justify-content-between align-items-center mb-4">

			<div>
				<h2>Orders</h2>
				<p class="text-muted mb-0">Manage customer orders</p>
			</div>

			<div>

				<a href="dashboard.jsp" class="btn btn-secondary"> Dashboard </a> <a
					href="orders?action=add" class="btn btn-primary"> + Create
					Order </a>

			</div>

		</div>


		<!-- ORDER TABLE -->

		<div class="card shadow">

			<div class="card-header bg-primary text-white">

				<h5 class="mb-0">Order List</h5>

			</div>


			<div class="card-body">

				<%
				List<Order> orders = (List<Order>) request.getAttribute("orders");
				%>


				<%
				if (orders == null || orders.isEmpty()) {
				%>

				<div class="alert alert-info">

					No orders found.

					<div class="mt-2">

						<a href="orders?action=add" class="btn btn-primary btn-sm">

							Create First Order </a>

					</div>

				</div>

				<%
				} else {
				%>


				<div class="table-responsive">

					<table class="table table-bordered table-hover">

						<thead class="table-dark">

							<tr>

								<th>ID</th>

								<th>Client</th>

								<th>Order Date</th>

								<th>Status</th>

								<th>Subtotal</th>

								<th>Discount</th>

								<th>Tax</th>

								<th>Total</th>

								<th>Credit Status</th>

								<th>Created By</th>

								<th>Action</th>

							</tr>

						</thead>


						<tbody>

							<%
							for (Order order : orders) {
							%>

							<tr>

								<td><%=order.getOrderId()%></td>

								<td><%=order.getClientName()%></td>

								<td><%=order.getOrderDate()%></td>

								<td><span class="badge bg-warning text-dark"> <%=order.getOrderStatus()%>

								</span></td>

								<td>₹ <%=order.getSubtotal()%>
								</td>

								<td>₹ <%=order.getDiscountAmount()%>
								</td>

								<td>₹ <%=order.getTaxAmount()%>
								</td>

								<td><strong> ₹ <%=order.getTotalAmount()%>
								</strong></td>

								<td><%=order.getCreditStatus()%></td>

								<td><%=order.getCreatedByName()%></td>

								<td><a href="orders?action=view&id=<%=order.getOrderId()%>"
									class="btn btn-sm btn-info"> View </a></td>

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

</body>

</html>