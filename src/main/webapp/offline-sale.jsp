<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Client"%>
<%@ page import="com.pratik.pharma.model.Product"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Offline Sale</title>

<meta name="viewport"
	content="width=device-width,
                   initial-scale=1">

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body class="bg-light">

	<div class="container-fluid p-4">

		<div
			class="d-flex
                justify-content-between
                align-items-center
                mb-4">

			<div>

				<h2>Offline Sale</h2>

				<p class="text-muted mb-0">Create a counter / offline sale with
					automatic FEFO, inventory deduction and invoice.</p>

			</div>

			<a href="${pageContext.request.contextPath}/orders"
				class="btn btn-secondary"> Back to Orders </a>

		</div>


		<!-- ===================================================== -->
		<!-- SUCCESS / ERROR -->
		<!-- ===================================================== -->

		<%
		String success = (String) session.getAttribute("offlineSaleSuccess");

		String error = (String) session.getAttribute("offlineSaleError");

		if (success != null) {
		%>

		<div class="alert alert-success">
			<%=success%>
		</div>

		<%
		session.removeAttribute("offlineSaleSuccess");
		}

		if (error != null) {
		%>

		<div class="alert alert-danger">
			<%=error%>
		</div>

		<%
		session.removeAttribute("offlineSaleError");
		}
		%>


		<!-- ===================================================== -->
		<!-- FORM -->
		<!-- ===================================================== -->

		<form method="post"
			action="${pageContext.request.contextPath}/offline-sale"
			id="offlineSaleForm">


			<!-- ================================================= -->
			<!-- CLIENT -->
			<!-- ================================================= -->

			<div class="card shadow-sm mb-4">

				<div class="card-header">

					<h5 class="mb-0">Customer</h5>

				</div>

				<div class="card-body">

					<label class="form-label"> Select Customer </label> <select
						name="clientId" class="form-select" required>

						<option value="">-- Select Customer --</option>

						<%
						List<Client> clients = (List<Client>) request.getAttribute("clients");

						if (clients != null) {

							for (Client client : clients) {

								if ("ACTIVE".equalsIgnoreCase(client.getStatus())) {
						%>

						<option value="<%=client.getClientId()%>">

							<%=client.getClientName()%> -
							<%=client.getCity()%> -
							<%=client.getGstNumber()%>

						</option>

						<%
						}
						}
						}
						%>

					</select>

					<div class="form-text">Customer must be ACTIVE and VERIFIED.

					</div>

				</div>

			</div>


			<!-- ================================================= -->
			<!-- PRODUCTS -->
			<!-- ================================================= -->

			<div class="card shadow-sm mb-4">

				<div
					class="card-header
                        d-flex
                        justify-content-between
                        align-items-center">

					<h5 class="mb-0">Products</h5>

					<button type="button" class="btn btn-primary btn-sm"
						onclick="addProductRow()">+ Add Product</button>

				</div>

				<div class="card-body">

					<div id="productContainer">

						<div
							class="product-row
                                row
                                g-3
                                mb-3">

							<div class="col-md-7">

								<label class="form-label"> Product </label> <select
									name="productId" class="form-select" required>

									<option value="">-- Select Product --</option>

									<%
									List<Product> products = (List<Product>) request.getAttribute("products");

									if (products != null) {

										for (Product product : products) {

											if ("ACTIVE".equalsIgnoreCase(product.getStatus())) {
									%>

									<option value="<%=product.getProductId()%>">

										<%=product.getProductName()%> -
										<%=product.getProductCode()%> - ₹<%=product.getSellingPrice()%>

									</option>

									<%
									}
									}
									}
									%>

								</select>

							</div>


							<div class="col-md-3">

								<label class="form-label"> Quantity </label> <input
									type="number" name="quantity" class="form-control" min="1"
									required>

							</div>


							<div
								class="col-md-2
                                    d-flex
                                    align-items-end">

								<button type="button" class="btn btn-danger w-100"
									onclick="removeProductRow(this)">Remove</button>

							</div>

						</div>

					</div>

				</div>

			</div>


			<!-- ================================================= -->
			<!-- PAYMENT -->
			<!-- ================================================= -->

			<div class="card shadow-sm mb-4">

				<div class="card-header">

					<h5 class="mb-0">Payment</h5>

				</div>

				<div class="card-body">

					<div class="row g-3">

						<div class="col-md-4">

							<label class="form-label"> Payment Method </label> <select
								name="paymentMethod" id="paymentMethod" class="form-select"
								required onchange="toggleReference()">

								<option value="">-- Select --</option>

								<option value="CASH">Cash</option>

								<option value="UPI">UPI</option>

								<option value="CARD">Card</option>

								<option value="NEFT">NEFT</option>

								<option value="RTGS">RTGS</option>

								<option value="CREDIT">Credit</option>

							</select>

						</div>


						<div class="col-md-8">

							<label class="form-label"> Transaction Reference </label> <input
								type="text" name="transactionReference"
								id="transactionReference" class="form-control"
								placeholder="UPI ID / UTR / Card reference">

							<div class="form-text">Required for digital payments. Not
								required for Cash/Credit.</div>

						</div>

					</div>

				</div>

			</div>


			<!-- ================================================= -->
			<!-- SUBMIT -->
			<!-- ================================================= -->

			<div
				class="d-flex
                    justify-content-end
                    gap-2">

				<a href="${pageContext.request.contextPath}/orders"
					class="btn btn-secondary"> Cancel </a>

				<button type="submit" class="btn btn-success">Create
					Offline Sale</button>

			</div>

		</form>

	</div>


	<script>
		function addProductRow() {

			const container = document.getElementById("productContainer");

			const firstRow = document.querySelector(".product-row");

			const newRow = firstRow.cloneNode(true);

			newRow.querySelector("select").value = "";

			newRow.querySelector("input").value = "";

			container.appendChild(newRow);
		}

		function removeProductRow(button) {

			const rows = document.querySelectorAll(".product-row");

			if (rows.length <= 1) {

				alert("At least one product is required.");

				return;
			}

			button.closest(".product-row").remove();
		}

		function toggleReference() {

			const method = document.getElementById("paymentMethod").value;

			const reference = document.getElementById("transactionReference");

			if (method === "CASH" || method === "CREDIT") {

				reference.required = false;

				reference.value = "";

			} else {

				reference.required = true;
			}
		}
	</script>

</body>

</html>