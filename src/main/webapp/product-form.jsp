<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Product"%>
<%@ page import="com.pratik.pharma.model.Category"%>
<%@ page import="com.pratik.pharma.model.Manufacturer"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<title>
	<%
	Product product = (Product) request.getAttribute("product");

	boolean editMode = product != null;

	if (editMode) {
		out.print("Edit Product");
	} else {
		out.print("Add Product");
	}
	%>
</title>

<meta name="viewport" content="width=device-width, initial-scale=1">

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body>

	<div class="container mt-4 mb-5">

		<!-- ========================= -->
		<!-- PAGE HEADER -->
		<!-- ========================= -->

		<div class="d-flex justify-content-between align-items-center mb-4">

			<div>

				<h2 class="fw-bold">

					<%
					if (editMode) {
						out.print("Edit Product");
					} else {
						out.print("Add Product");
					}
					%>

				</h2>

				<p class="text-muted">Manage pharmaceutical product information
				</p>

			</div>

			<a href="<%=request.getContextPath()%>/products"
				class="btn btn-secondary"> Back to Products </a>

		</div>


		<!-- ========================= -->
		<!-- PRODUCT FORM -->
		<!-- ========================= -->

		<div class="card shadow-sm">

			<div class="card-header bg-primary text-white">

				<h5 class="mb-0">

					<%
					if (editMode) {
						out.print("Update Product");
					} else {
						out.print("New Product");
					}
					%>

				</h5>

			</div>


			<div class="card-body">

				<form method="post" action="<%=request.getContextPath()%>/products">


					<!-- ========================= -->
					<!-- ACTION -->
					<!-- ========================= -->

					<input type="hidden" name="action"
						value="<%=editMode ? "update" : "add"%>">


					<%
					if (editMode) {
					%>

					<input type="hidden" name="productId"
						value="<%=product.getProductId()%>">

					<%
					}
					%>


					<!-- ========================= -->
					<!-- ROW 1 -->
					<!-- ========================= -->

					<div class="row mb-3">

						<!-- PRODUCT CODE -->

						<div class="col-md-6">

							<label class="form-label"> Product Code </label> <input
								type="text" class="form-control" name="productCode" required
								value="<%=editMode ? product.getProductCode() : ""%>">

						</div>


						<!-- PRODUCT NAME -->

						<div class="col-md-6">

							<label class="form-label"> Product Name </label> <input
								type="text" class="form-control" name="productName" required
								value="<%=editMode ? product.getProductName() : ""%>">

						</div>

					</div>


					<!-- ========================= -->
					<!-- DESCRIPTION -->
					<!-- ========================= -->

					<div class="mb-3">

						<label class="form-label"> Description </label>

						<textarea class="form-control" name="description" rows="3"
							placeholder="Enter product description"><%=editMode ? product.getDescription() : ""%></textarea>

					</div>


					<!-- ========================= -->
					<!-- ROW 2 -->
					<!-- ========================= -->

					<div class="row mb-3">

						<!-- CATEGORY -->

						<div class="col-md-6">

							<label class="form-label"> Category </label> <select
								class="form-select" name="categoryId" required>

								<option value="">-- Select Category --</option>

								<%
								List<Category> categories = (List<Category>) request.getAttribute("categories");

								if (categories != null) {

									for (Category category : categories) {

										boolean selected = editMode && product.getCategoryId() == category.getCategoryId();
								%>

								<option value="<%=category.getCategoryId()%>"
									<%=selected ? "selected" : ""%>>

									<%=category.getCategoryName()%>

								</option>

								<%
								}
								}
								%>

							</select>

						</div>


						<!-- MANUFACTURER -->

						<div class="col-md-6">

							<label class="form-label"> Manufacturer </label> <select
								class="form-select" name="manufacturerId" required>

								<option value="">-- Select Manufacturer --</option>

								<%
								List<Manufacturer> manufacturers = (List<Manufacturer>) request.getAttribute("manufacturers");

								if (manufacturers != null) {

									for (Manufacturer manufacturer : manufacturers) {

										boolean selected = editMode && product.getManufacturerId() == manufacturer.getManufacturerId();
								%>

								<option value="<%=manufacturer.getManufacturerId()%>"
									<%=selected ? "selected" : ""%>>

									<%=manufacturer.getManufacturerName()%>

								</option>

								<%
								}
								}
								%>

							</select>

						</div>

					</div>


					<!-- ========================= -->
					<!-- ROW 3 -->
					<!-- ========================= -->

					<div class="row mb-3">

						<!-- DOSAGE FORM -->

						<div class="col-md-4">

							<label class="form-label"> Dosage Form </label> <input
								type="text" class="form-control" name="dosageForm"
								placeholder="Example: TABLET"
								value="<%=editMode ? product.getDosageForm() : ""%>">

						</div>


						<!-- STRENGTH -->

						<div class="col-md-4">

							<label class="form-label"> Strength </label> <input type="text"
								class="form-control" name="strength"
								placeholder="Example: 500 MG"
								value="<%=editMode ? product.getStrength() : ""%>">

						</div>


						<!-- UNIT -->

						<div class="col-md-4">

							<label class="form-label"> Unit </label> <input type="text"
								class="form-control" name="unit"
								placeholder="Example: STRIP / VIAL / BOTTLE"
								value="<%=editMode ? product.getUnit() : ""%>">

						</div>

					</div>


					<!-- ========================= -->
					<!-- ROW 4 -->
					<!-- ========================= -->

					<div class="row mb-3">

						<!-- HSN -->

						<div class="col-md-4">

							<label class="form-label"> HSN Code </label> <input type="text"
								class="form-control" name="hsnCode"
								value="<%=editMode ? product.getHsnCode() : ""%>">

						</div>


						<!-- GST -->

						<div class="col-md-4">

							<label class="form-label"> GST Rate (%) </label> <input
								type="number" step="0.01" min="0" class="form-control"
								name="gstRate" required
								value="<%=editMode ? product.getGstRate() : ""%>">

						</div>


						<!-- MRP -->

						<div class="col-md-4">

							<label class="form-label"> MRP </label> <input type="number"
								step="0.01" min="0" class="form-control" name="mrp" required
								value="<%=editMode ? product.getMrp() : ""%>">

						</div>

					</div>


					<!-- ========================= -->
					<!-- ROW 5 -->
					<!-- ========================= -->

					<div class="row mb-3">

						<!-- SELLING PRICE -->

						<div class="col-md-4">

							<label class="form-label"> Selling Price </label> <input
								type="number" step="0.01" min="0" class="form-control"
								name="sellingPrice" required
								value="<%=editMode ? product.getSellingPrice() : ""%>">

						</div>


						<!-- COLD CHAIN -->

						<div class="col-md-4">

							<label class="form-label"> Cold Chain Required? </label>

							<div class="mt-2">

								<div class="form-check form-check-inline">

									<input class="form-check-input" type="radio"
										name="coldChainRequired" value="true"
										<%=editMode && product.isColdChainRequired() ? "checked" : (!editMode ? "checked" : "")%>>

									<label class="form-check-label"> Yes </label>

								</div>


								<div class="form-check form-check-inline">

									<input class="form-check-input" type="radio"
										name="coldChainRequired" value="false"
										<%=editMode && !product.isColdChainRequired() ? "checked" : ""%>>

									<label class="form-check-label"> No </label>

								</div>

							</div>

						</div>


						<!-- STATUS -->

						<%
						if (editMode) {
						%>

						<div class="col-md-4">

							<label class="form-label"> Status </label> <select
								class="form-select" name="status">

								<option value="ACTIVE"
									<%="ACTIVE".equalsIgnoreCase(product.getStatus()) ? "selected" : ""%>>

									ACTIVE</option>

								<option value="INACTIVE"
									<%="INACTIVE".equalsIgnoreCase(product.getStatus()) ? "selected" : ""%>>

									INACTIVE</option>

							</select>

						</div>

						<%
						}
						%>

					</div>


					<!-- ========================= -->
					<!-- BUTTONS -->
					<!-- ========================= -->

					<div class="mt-4">

						<button type="submit" class="btn btn-primary">

							<%
							if (editMode) {
								out.print("Update Product");
							} else {
								out.print("Add Product");
							}
							%>

						</button>


						<a href="<%=request.getContextPath()%>/products"
							class="btn btn-secondary ms-2"> Cancel </a>

					</div>

				</form>

			</div>

		</div>

	</div>

</body>
</html>