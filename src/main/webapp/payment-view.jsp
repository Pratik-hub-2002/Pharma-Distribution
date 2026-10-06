<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="com.pratik.pharma.model.Payment"%>

<%
Payment payment = (Payment) request.getAttribute("payment");
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Payment #<%=payment.getPaymentId()%>
</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>


<body class="bg-light">


	<div class="container mt-5">


		<div class="card shadow">


			<div class="card-header bg-success text-white">

				<h4 class="mb-0">

					Payment #

					<%=payment.getPaymentId()%>

				</h4>

			</div>


			<div class="card-body">


				<div class="row">


					<div class="col-md-6 mb-3">

						<strong> Invoice: </strong> <br>

						<%=payment.getInvoiceNumber()%>

					</div>


					<div class="col-md-6 mb-3">

						<strong> Order: </strong> <br> #<%=payment.getOrderId()%>

					</div>


					<div class="col-md-6 mb-3">

						<strong> Client: </strong> <br>

						<%=payment.getClientName()%>

					</div>


					<div class="col-md-6 mb-3">

						<strong> Payment Date: </strong> <br>

						<%=payment.getPaymentDate()%>

					</div>


					<div class="col-md-6 mb-3">

						<strong> Payment Amount: </strong> <br>

						<h4>

							₹<%=payment.getAmount()%>

						</h4>

					</div>


					<div class="col-md-6 mb-3">

						<strong> Payment Method: </strong> <br>

						<%=payment.getPaymentMethod()%>

					</div>


					<div class="col-md-6 mb-3">

						<strong> Transaction Reference: </strong> <br>

						<%=payment.getTransactionReference() != null ? payment.getTransactionReference() : "N/A"%>

					</div>


					<div class="col-md-6 mb-3">

						<strong> Payment Status: </strong> <br> <span
							class="badge bg-success"> <%=payment.getPaymentStatus()%>

						</span>

					</div>

				</div>


				<hr>


				<div class="row text-center">


					<div class="col-md-4">

						<div class="card bg-light">

							<div class="card-body">

								<small> Invoice Amount </small>

								<h4>

									₹<%=payment.getInvoiceTotalAmount()%>

								</h4>

							</div>

						</div>

					</div>


					<div class="col-md-4">

						<div class="card bg-light">

							<div class="card-body">

								<small> Total Paid </small>

								<h4 class="text-success">

									₹<%=payment.getPaidAmount()%>

								</h4>

							</div>

						</div>

					</div>


					<div class="col-md-4">

						<div class="card bg-light">

							<div class="card-body">

								<small> Outstanding </small>

								<h4 class="text-danger">

									₹<%=payment.getOutstandingAmount()%>

								</h4>

							</div>

						</div>

					</div>


				</div>


				<hr>


				<p>

					<strong> Due Date: </strong>

					<%=payment.getInvoiceDueDate()%>

				</p>


				<p>

					<strong> Recorded By: </strong>

					<%=payment.getRecordedByName()%>

				</p>


				<p>

					<strong> Created At: </strong>

					<%=payment.getCreatedAt()%>

				</p>


				<div class="mt-4 d-flex gap-2">


					<a href="payments?action=list" class="btn btn-secondary"> Back
						to Payments </a> <a
						href="invoices?action=view&id=<%=payment.getInvoiceId()%>"
						class="btn btn-outline-primary"> View Invoice </a>


				</div>


			</div>

		</div>


	</div>


</body>

</html>