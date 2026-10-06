<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Payment"%>

<%
List<Payment> payments = (List<Payment>) request.getAttribute("payments");
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Payment Management</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>


<body class="bg-light">


	<div class="container-fluid mt-4">


		<div class="d-flex justify-content-between align-items-center mb-4">

			<h2>Payment & Accounts</h2>

			<a href="payments?action=add" class="btn btn-primary"> + Record
				Payment </a>

		</div>


		<div class="card shadow-sm">


			<div class="card-body">


				<div class="table-responsive">


					<table class="table table-bordered table-hover align-middle">


						<thead class="table-dark">

							<tr>

								<th>ID</th>

								<th>Invoice</th>

								<th>Order</th>

								<th>Client</th>

								<th>Payment Date</th>

								<th>Amount</th>

								<th>Method</th>

								<th>Reference</th>

								<th>Status</th>

								<th>Paid</th>

								<th>Outstanding</th>

								<th>Recorded By</th>

								<th>Action</th>

							</tr>

						</thead>


						<tbody>


							<%
							if (payments != null && !payments.isEmpty()) {

								for (Payment payment : payments) {
							%>


							<tr>

								<td><%=payment.getPaymentId()%></td>


								<td><%=payment.getInvoiceNumber()%></td>


								<td>#<%=payment.getOrderId()%>

								</td>


								<td><%=payment.getClientName()%></td>


								<td><%=payment.getPaymentDate()%></td>


								<td>₹<%=payment.getAmount()%>

								</td>


								<td><%=payment.getPaymentMethod()%></td>


								<td><%=payment.getTransactionReference() != null ? payment.getTransactionReference() : "N/A"%></td>


								<td><span class="badge bg-success"> <%=payment.getPaymentStatus()%>

								</span></td>


								<td>₹<%=payment.getPaidAmount()%>

								</td>


								<td>₹<%=payment.getOutstandingAmount()%>

								</td>


								<td><%=payment.getRecordedByName()%></td>


								<td><a
									href="payments?action=view&id=<%=payment.getPaymentId()%>"
									class="btn btn-sm btn-outline-primary"> View </a></td>

							</tr>


							<%
							}

							} else {
							%>


							<tr>

								<td colspan="13" class="text-center text-muted py-4">No
									payments found.</td>

							</tr>


							<%
							}
							%>


						</tbody>


					</table>


				</div>


			</div>

		</div>


	</div>


</body>

</html>