<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Invoice"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Invoices</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>


<body class="bg-light">


	<div class="container mt-5">

		<!-- HEADER -->

		<div class="d-flex justify-content-between align-items-center mb-4">

			<div>

				<h2 class="mb-1">🧾 Invoice Management</h2>

				<p class="text-muted mb-0">View and manage customer invoices</p>

			</div>


			<div>

				<a href="dashboard.jsp" class="btn btn-secondary"> ← Dashboard </a>

			</div>

		</div>


		<!-- ERROR -->

		<%
		String error = (String) request.getAttribute("error");

		if (error != null) {
		%>

		<div class="alert alert-danger">
			<%=error%>
		</div>

		<%
		}
		%>


		<!-- INVOICE TABLE -->

		<div class="card shadow">

			<div class="card-body">

				<%
				List<Invoice> invoices = (List<Invoice>) request.getAttribute("invoices");
				%>


				<%
				if (invoices == null || invoices.isEmpty()) {
				%>

				<div class="text-center py-5">

					<h5>No invoices found</h5>

					<p class="text-muted">There are currently no invoices.</p>

				</div>

				<%
				} else {
				%>


				<div class="table-responsive">

					<table class="table table-bordered table-hover align-middle">

						<thead class="table-dark">

							<tr>

								<th>Invoice #</th>

								<th>Order #</th>

								<th>Subtotal</th>

								<th>Discount</th>

								<th>GST</th>

								<th>Grand Total</th>

								<th>Invoice Date</th>

								<th>Due Date</th>

								<th>Status</th>

								<th>Action</th>

							</tr>

						</thead>


						<tbody>


							<%
							for (Invoice invoice : invoices) {
							%>

							<tr>

								<td class="fw-bold"><%=invoice.getInvoiceNumber()%></td>


								<td>#<%=invoice.getOrderId()%>

								</td>


								<td>₹<%=invoice.getSubtotal()%>

								</td>


								<td>₹<%=invoice.getDiscountAmount()%>

								</td>


								<td>₹<%=invoice.getTaxAmount()%>

								</td>


								<td class="fw-bold">₹<%=invoice.getTotalAmount()%>

								</td>


								<td><%=invoice.getInvoiceDate()%></td>


								<td><%=invoice.getDueDate() != null ? invoice.getDueDate() : "-"%></td>


								<td>
									<%
									String status = invoice.getInvoiceStatus();

									if ("PAID".equalsIgnoreCase(status)) {
									%> <span class="badge bg-success">
										PAID </span> <%
 } else if ("PARTIALLY_PAID".equalsIgnoreCase(status)) {
 %> <span
									class="badge bg-warning text-dark"> PARTIALLY PAID </span> <%
 } else {
 %> <span class="badge bg-danger">
										<%=status%>
								</span> <%
 }
 %>

								</td>


								<td><a
									href="invoices?action=view&id=<%=invoice.getInvoiceId()%>"
									class="btn btn-sm btn-primary"> View </a></td>

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