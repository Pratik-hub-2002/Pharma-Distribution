<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.StockTransaction"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Stock Transactions</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body>

	<div class="container-fluid mt-4">

		<div class="d-flex justify-content-between mb-3">

			<h2>Stock Transactions</h2>

			<a href="<%=request.getContextPath()%>/stock-transactions?action=add"
				class="btn btn-primary"> Add Transaction </a>

		</div>


		<div class="table-responsive">

			<table class="table table-bordered table-hover">

				<thead class="table-dark">

					<tr>
						<th>ID</th>
						<th>Product</th>
						<th>Code</th>
						<th>Batch</th>
						<th>Type</th>
						<th>Quantity</th>
						<th>Reference</th>
						<th>Reference ID</th>
						<th>Remarks</th>
						<th>Created By</th>
						<th>Created At</th>
					</tr>

				</thead>

				<tbody>

					<%
					List<StockTransaction> transactions = (List<StockTransaction>) request.getAttribute("transactions");

					if (transactions != null && !transactions.isEmpty()) {

						for (StockTransaction transaction : transactions) {
					%>

					<tr>

						<td><%=transaction.getTransactionId()%></td>

						<td><%=transaction.getProductName()%></td>

						<td><%=transaction.getProductCode()%></td>

						<td><%=transaction.getBatchNumber()%></td>

						<td><%=transaction.getTransactionType()%></td>

						<td><%=transaction.getQuantity()%></td>

						<td><%=transaction.getReferenceType() != null ? transaction.getReferenceType() : "-"%></td>

						<td><%=transaction.getReferenceId() != null ? transaction.getReferenceId() : "-"%></td>

						<td><%=transaction.getRemarks() != null ? transaction.getRemarks() : "-"%></td>

						<td><%=transaction.getCreatedByName()%></td>

						<td><%=transaction.getCreatedAt()%></td>

					</tr>

					<%
					}

					} else {
					%>

					<tr>

						<td colspan="11" class="text-center">No stock transactions
							found.</td>

					</tr>

					<%
					}
					%>

				</tbody>

			</table>

		</div>

	</div>

</body>

</html>