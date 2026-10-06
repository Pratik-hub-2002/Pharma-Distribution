<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Invoice"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Invoice Management</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<style>
body {
	background-color: #f5f7fa;
}

.page-header {
	background: #212529;
	color: white;
	padding: 20px;
	border-radius: 10px;
}

.invoice-card {
	background: white;
	border-radius: 10px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.table th {
	white-space: nowrap;
}

.table td {
	vertical-align: middle;
}

.badge-pending {
	background-color: #ffc107;
	color: #000;
}

.badge-paid {
	background-color: #198754;
}

.badge-partial {
	background-color: #0d6efd;
}

.empty-box {
	padding: 50px;
	text-align: center;
}
</style>

</head>

<body>

	<div class="container-fluid px-4 py-4">

		<!-- =====================================================
         HEADER
         ===================================================== -->

		<div class="page-header mb-4">

			<div class="d-flex justify-content-between align-items-center">

				<div>

					<h2 class="mb-1">Invoice Management</h2>

					<p class="mb-0">View and manage generated invoices</p>

				</div>

				<a href="dashboard.jsp" class="btn btn-light"> Back to Dashboard

				</a>

			</div>

		</div>


		<!-- =====================================================
         ERROR MESSAGE
         ===================================================== -->

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


		<!-- =====================================================
         INVOICE LIST
         ===================================================== -->

		<div class="invoice-card p-4">

			<div class="d-flex justify-content-between align-items-center mb-3">

				<h4 class="mb-0">Invoice List</h4>

				<span class="badge bg-dark"> <%
 List<Invoice> invoices = (List<Invoice>) request.getAttribute("invoices");

 int invoiceCount = invoices == null ? 0 : invoices.size();
 %> Total Invoices: <%=invoiceCount%>

				</span>

			</div>


			<%
			if (invoices == null || invoices.isEmpty()) {
			%>

			<div class="empty-box">

				<h5>No invoices found</h5>

				<p class="text-muted">No invoices have been generated yet.</p>

				<a href="dashboard.jsp" class="btn btn-secondary"> Back to
					Dashboard </a>

			</div>

			<%
			} else {
			%>


			<div class="table-responsive">

				<table class="table table-bordered table-hover align-middle">

					<thead class="table-dark">

						<tr>

							<th>#</th>

							<th>Invoice Number</th>

							<th>Invoice Date</th>

							<th>Order</th>

							<th>Subtotal</th>

							<th>Discount</th>

							<th>GST / Tax</th>

							<th>Grand Total</th>

							<th>Due Date</th>

							<th>Status</th>

							<th>Action</th>

						</tr>

					</thead>


					<tbody>

						<%
						int serialNumber = 1;

						for (Invoice invoice : invoices) {
						%>

						<tr>

							<td><%=serialNumber++%></td>

							<td><strong> <%=invoice.getInvoiceNumber()%>
							</strong></td>

							<td><%=invoice.getInvoiceDate()%></td>

							<td><a
								href="orders?action=view&id=<%=invoice.getOrderId()%>"
								class="text-decoration-none"> #<%=invoice.getOrderId()%>

							</a></td>

							<td>₹<%=invoice.getSubtotal()%>

							</td>

							<td>₹<%=invoice.getDiscountAmount()%>

							</td>

							<td>₹<%=invoice.getTaxAmount()%>

							</td>

							<td><strong> ₹<%=invoice.getTotalAmount()%>

							</strong></td>

							<td><%=invoice.getDueDate() != null ? invoice.getDueDate() : "-"%></td>

							<td>
								<%
								String status = invoice.getInvoiceStatus();

								if ("PAID".equalsIgnoreCase(status)) {
								%> <span class="badge badge-paid">
									PAID </span> <%
 } else if ("PARTIALLY_PAID".equalsIgnoreCase(status)) {
 %> <span class="badge badge-partial">
									PARTIALLY PAID </span> <%
 } else {
 %> <span class="badge badge-pending">
									<%=status%>
							</span> <%
 }
 %>

							</td>

							<td><a
								href="invoices?action=view&id=<%=invoice.getInvoiceId()%>"
								class="btn btn-primary btn-sm"> View </a></td>

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


	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
		
	</script>

</body>

</html>