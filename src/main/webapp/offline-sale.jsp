<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Client"%>
<%@ page import="com.pratik.pharma.model.Product"%>

<%
List<Client> clients = (List<Client>) request.getAttribute("clients");

List<Product> products = (List<Product>) request.getAttribute("products");

String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Offline Sale</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body>

	<div class="container mt-5">

		<div class="card shadow">

			<div class="card-header bg-success text-white">

				<h4 class="mb-0">Offline Sale</h4>

			</div>

			<div class="card-body">

				<p class="text-muted">Create a direct counter sale for a client.
				</p>


				<%
				if (error != null) {
				%>

				<div class="alert alert-danger">
					<%=error%>
				</div>

				<%
				}
				%>


				<form action="offline-sale" method="post">


					<!-- CLIENT -->

					<div class="mb-3">

						<label class="form-label"> Client </label> <select name="clientId"
							class="form-select" required>

							<option value="">Select Client</option>

							<%
							if (clients != null) {
								for (Client client : clients) {

									if ("ACTIVE".equalsIgnoreCase(client.getStatus())
									&& "VERIFIED".equalsIgnoreCase(client.getVerificationStatus())) {
							%>

							<option value="<%=client.getClientId()%>">

								<%=client.getClientName()%>

							</option>

							<%
							}
							}
							}
							%>

						</select>

					</div>


					<!-- PRODUCT -->

					<div class="mb-3">

						<label class="form-label"> Product </label> <select
							name="productId" class="form-select" required>

							<option value="">Select Product</option>

							<%
							if (products != null) {
								for (Product product : products) {

									if ("ACTIVE".equalsIgnoreCase(product.getStatus())) {
							%>

							<option value="<%=product.getProductId()%>">

								<%=product.getProductName()%> - ₹<%=product.getSellingPrice()%>

							</option>

							<%
							}
							}
							}
							%>

						</select>

					</div>


					<!-- QUANTITY -->

					<div class="mb-3">

						<label class="form-label"> Quantity </label> <input type="number"
							name="quantity" class="form-control" min="1" required>

					</div>


					<!-- PAYMENT METHOD -->

					<div class="mb-3">

						<label class="form-label"> Payment Method </label> <select
							name="paymentMethod" class="form-select" required>

							<option value="">Select Payment Method</option>

							<option value="CASH">Cash</option>

							<option value="UPI">UPI</option>

							<option value="CARD">Card</option>

							<option value="CREDIT">Credit</option>

						</select>

					</div>


					<!-- TRANSACTION REFERENCE -->

					<div class="mb-3">

						<label class="form-label"> Transaction Reference </label> <input
							type="text" name="transactionReference" class="form-control"
							placeholder="Optional">

					</div>


					<!-- BUTTONS -->

					<button type="submit" class="btn btn-success">Create
						Offline Sale</button>


					<a href="dashboard.jsp" class="btn btn-secondary"> Back </a>

				</form>

			</div>

		</div>

	</div>

</body>

</html>