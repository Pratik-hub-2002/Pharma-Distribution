<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Batch"%>
<%@ page import="com.pratik.pharma.model.Product"%>

<%
Batch batch = (Batch) request.getAttribute("batch");

boolean editMode = batch != null;

List<Product> products = (List<Product>) request.getAttribute("products");
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title><%=editMode ? "Edit Batch" : "Add Batch"%></title>

<meta name="viewport" content="width=device-width, initial-scale=1">

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body>

	<div class="container mt-4 mb-5">

		<!-- HEADER -->

		<div class="d-flex justify-content-between
		align-items-center mb-4">

			<div>

				<h2 class="fw-bold">
					<%=editMode ? "Edit Batch" : "Add Batch"%>
				</h2>

				<p class="text-muted mb-0">Manage pharmaceutical product batches
				</p>

			</div>

			<a href="<%=request.getContextPath()%>/batches"
				class="btn btn-secondary"> Back to Batches </a>

		</div>


		<!-- FORM -->

		<div class="card shadow-sm">

			<div class="card-header bg-primary text-white">

				<h5 class="mb-0">

					<%=editMode ? "Update Batch" : "New Batch"%>

				</h5>

			</div>


			<div class="card-body">

				<form method="post" action="<%=request.getContextPath()%>/batches">


					<!-- ACTION -->

					<input type="hidden" name="action"
						value="<%=editMode ? "update" : "add"%>">


					<%
					if (editMode) {
					%>

					<input type="hidden" name="batchId" value="<%=batch.getBatchId()%>">

					<%
					}
					%>


					<!-- PRODUCT -->

					<div class="mb-3">

						<label class="form-label"> Product </label> <select
							name="productId" class="form-select" required>

							<option value="">-- Select Product --</option>

							<%
							if (products != null) {

								for (Product product : products) {

									if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {
								continue;
									}

									boolean selected = editMode && batch.getProductId() == product.getProductId();
							%>

							<option value="<%=product.getProductId()%>"
								<%=selected ? "selected" : ""%>>

								<%=product.getProductCode()%> -
								<%=product.getProductName()%>

							</option>

							<%
							}
							}
							%>

						</select>

					</div>


					<!-- BATCH NUMBER -->

					<div class="mb-3">

						<label class="form-label"> Batch Number </label> <input
							type="text" name="batchNumber" class="form-control"
							placeholder="Example: AMX250-B001" required
							value="<%=editMode ? batch.getBatchNumber() : ""%>">

					</div>


					<!-- DATES -->

					<div class="row mb-3">

						<div class="col-md-6">

							<label class="form-label"> Manufacturing Date </label> <input
								type="date" name="manufacturingDate" class="form-control"
								required
								value="<%=editMode ? batch.getManufacturingDate() : ""%>">

						</div>


						<div class="col-md-6">

							<label class="form-label"> Expiry Date </label> <input
								type="date" name="expiryDate" class="form-control" required
								value="<%=editMode ? batch.getExpiryDate() : ""%>">

						</div>

					</div>


					<!-- PURCHASE PRICE -->

					<div class="mb-3">

						<label class="form-label"> Purchase Price </label> <input
							type="number" name="purchasePrice" class="form-control"
							step="0.01" min="0" required
							value="<%=editMode ? batch.getPurchasePrice() : ""%>">

					</div>


					<!-- STATUS -->

					<%
					if (editMode) {
					%>

					<div class="mb-3">

						<label class="form-label"> Status </label> <select name="status"
							class="form-select">

							<option value="ACTIVE"
								<%="ACTIVE".equalsIgnoreCase(batch.getStatus()) ? "selected" : ""%>>

								ACTIVE</option>

							<option value="INACTIVE"
								<%="INACTIVE".equalsIgnoreCase(batch.getStatus()) ? "selected" : ""%>>

								INACTIVE</option>

						</select>

					</div>

					<%
					}
					%>


					<!-- BUTTONS -->

					<button type="submit" class="btn btn-primary">

						<%=editMode ? "Update Batch" : "Add Batch"%>

					</button>


					<a href="<%=request.getContextPath()%>/batches"
						class="btn btn-secondary ms-2"> Cancel </a>

				</form>

			</div>

		</div>

	</div>

</body>

</html>