<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Payment"%>
<%@ page import="com.pratik.pharma.model.Invoice"%>

<%
List<Payment> outstandingInvoices = (List<Payment>) request.getAttribute("outstandingInvoices");

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

				<h4 class="mb-0">💰 Record Payment</h4>

			</div>


			<div class="card-body">


				<!-- ERROR -->

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


					<!-- ============================= -->
					<!-- SELECT INVOICE -->
					<!-- ============================= -->

					<div class="mb-3">

						<label class="form-label fw-bold"> Invoice </label> <select
							name="invoiceId" id="invoiceId" class="form-select" required
							onchange="showInvoiceDetails()">


							<option value="">-- Select Outstanding Invoice --</option>


							<%
							if (outstandingInvoices != null && !outstandingInvoices.isEmpty()) {

								for (Payment payment : outstandingInvoices) {

									boolean selected = false;

									if (selectedInvoice != null && selectedInvoice.getInvoiceId() == payment.getInvoiceId()) {

								selected = true;
									}
							%>


							<option value="<%=payment.getInvoiceId()%>"
								data-number="<%=payment.getInvoiceNumber()%>"
								data-client="<%=payment.getClientName()%>"
								data-total="<%=payment.getInvoiceTotalAmount()%>"
								data-paid="<%=payment.getPaidAmount()%>"
								data-outstanding="<%=payment.getOutstandingAmount()%>"
								<%=selected ? "selected" : ""%>>

								<%=payment.getInvoiceNumber()%> -
								<%=payment.getClientName()%> - Outstanding ₹<%=payment.getOutstandingAmount()%>

							</option>


							<%
							}

							} else {
							%>


							<option value="" disabled>No outstanding invoices</option>


							<%
							}
							%>


						</select>

					</div>


					<!-- ============================= -->
					<!-- INVOICE INFORMATION -->
					<!-- ============================= -->

					<div id="invoiceDetails" class="alert alert-info"
						style="display: none;">

						<h5>Invoice Details</h5>


						<strong> Invoice: </strong> <span id="invoiceNumber"> - </span> <br>


						<strong> Client: </strong> <span id="clientName"> - </span> <br>


						<strong> Invoice Amount: </strong> ₹<span id="invoiceTotal">
							0.00 </span> <br> <strong> Already Paid: </strong> ₹<span
							id="paidAmount"> 0.00 </span> <br> <strong>
							Outstanding: </strong> <span class="text-danger fw-bold"> ₹<span
							id="outstandingAmount"> 0.00 </span>

						</span>

					</div>


					<!-- ============================= -->
					<!-- PAYMENT AMOUNT -->
					<!-- ============================= -->

					<div class="mb-3">

						<label class="form-label fw-bold"> Payment Amount </label> <input
							type="number" name="amount" id="amount" class="form-control"
							step="0.01" min="0.01" required>


						<div class="form-text">

							Maximum payment: ₹<span id="maxPayment"> 0.00 </span>

						</div>

					</div>


					<!-- ============================= -->
					<!-- PAYMENT METHOD -->
					<!-- ============================= -->

					<div class="mb-3">

						<label class="form-label fw-bold"> Payment Method </label> <select
							name="paymentMethod" class="form-select" required>

							<option value="">-- Select Payment Method --</option>

							<option value="CASH">CASH</option>

							<option value="UPI">UPI</option>

							<option value="NEFT">NEFT</option>

							<option value="RTGS">RTGS</option>

							<option value="IMPS">IMPS</option>

							<option value="CHEQUE">CHEQUE</option>

							<option value="BANK_TRANSFER">BANK TRANSFER</option>

						</select>

					</div>


					<!-- ============================= -->
					<!-- TRANSACTION REFERENCE -->
					<!-- ============================= -->

					<div class="mb-3">

						<label class="form-label fw-bold"> Transaction Reference </label>


						<input type="text" name="transactionReference"
							class="form-control"
							placeholder="UTR / Cheque No / UPI Reference">

					</div>


					<!-- ============================= -->
					<!-- BUTTONS -->
					<!-- ============================= -->

					<div class="d-flex gap-2">

						<button type="submit" class="btn btn-success">💰 Record
							Payment</button>


						<a href="payments?action=list" class="btn btn-secondary">

							Cancel </a>

					</div>


				</form>

			</div>

		</div>

	</div>


	<!-- ============================= -->
	<!-- JAVASCRIPT -->
	<!-- ============================= -->

	<script>
		function showInvoiceDetails() {

			const select = document.getElementById("invoiceId");

			const option = select.options[select.selectedIndex];

			const details = document.getElementById("invoiceDetails");

			if (!option || !option.value) {

				details.style.display = "none";

				document.getElementById("amount").removeAttribute("max");

				document.getElementById("maxPayment").innerText = "0.00";

				return;
			}

			const invoiceNumber = option.getAttribute("data-number");

			const clientName = option.getAttribute("data-client");

			const invoiceTotal = option.getAttribute("data-total");

			const paidAmount = option.getAttribute("data-paid");

			const outstandingAmount = option.getAttribute("data-outstanding");

			document.getElementById("invoiceNumber").innerText = invoiceNumber;

			document.getElementById("clientName").innerText = clientName;

			document.getElementById("invoiceTotal").innerText = parseFloat(
					invoiceTotal).toFixed(2);

			document.getElementById("paidAmount").innerText = parseFloat(
					paidAmount).toFixed(2);

			document.getElementById("outstandingAmount").innerText = parseFloat(
					outstandingAmount).toFixed(2);

			document.getElementById("maxPayment").innerText = parseFloat(
					outstandingAmount).toFixed(2);

			document.getElementById("amount").max = outstandingAmount;

			details.style.display = "block";

		}

		// If PaymentServlet already selected an invoice

		window.onload = function() {

			showInvoiceDetails();

		};
	</script>


</body>

</html>