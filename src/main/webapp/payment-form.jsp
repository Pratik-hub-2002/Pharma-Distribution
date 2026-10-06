<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="com.pratik.pharma.model.Invoice"%>

<%
Invoice selectedInvoice = (Invoice) request.getAttribute("selectedInvoice");

String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Record Payment</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>


<body class="bg-light">


	<div class="container mt-5">


		<div class="card shadow">


			<div class="card-header bg-primary text-white">

				<h4 class="mb-0">Record Payment</h4>

			</div>


			<div class="card-body">


				<%
				if (error != null) {
				%>

				<div class="alert alert-danger">

					<%=error%>

				</div>

				<%
				}
				%>


				<form action="payments" method="post">


					<input type="hidden" name="action" value="create">


					<!-- INVOICE -->

					<div class="mb-3">

						<label class="form-label"> Invoice ID </label> <input
							type="number" name="invoiceId" class="form-control"
							value="<%=selectedInvoice != null ? selectedInvoice.getInvoiceId() : ""%>"
							required>

					</div>


					<%
					if (selectedInvoice != null) {
					%>


					<div class="alert alert-info">


						<strong> Invoice: </strong>

						<%=selectedInvoice.getInvoiceNumber()%>


						<br> <strong> Invoice Amount: </strong> ₹<%=selectedInvoice.getTotalAmount()%>


						<br> <strong> Due Date: </strong>

						<%=selectedInvoice.getDueDate()%>


					</div>


					<%
					}
					%>


					<!-- AMOUNT -->

					<div class="mb-3">

						<label class="form-label"> Payment Amount </label> <input
							type="number" name="amount" class="form-control" step="0.01"
							min="0.01" required>

					</div>


					<!-- METHOD -->

					<div class="mb-3">

						<label class="form-label"> Payment Method </label> <select
							name="paymentMethod" class="form-select" required>


							<option value="CASH">CASH</option>


							<option value="UPI">UPI</option>


							<option value="NEFT">NEFT</option>


							<option value="RTGS">RTGS</option>


							<option value="IMPS">IMPS</option>


							<option value="CHEQUE">CHEQUE</option>


							<option value="BANK_TRANSFER">BANK TRANSFER</option>


						</select>

					</div>


					<!-- REFERENCE -->

					<div class="mb-3">

						<label class="form-label"> Transaction Reference </label> <input
							type="text" name="transactionReference" class="form-control"
							placeholder="UTR / Cheque No / UPI Reference">

					</div>


					<div class="d-flex gap-2">


						<button type="submit" class="btn btn-success">Record
							Payment</button>


						<a href="payments?action=list" class="btn btn-secondary">

							Cancel </a>


					</div>


				</form>


			</div>

		</div>


	</div>


</body>

</html>