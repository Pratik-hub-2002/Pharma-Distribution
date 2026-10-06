<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Product"%>
<%@ page import="com.pratik.pharma.model.Scheme"%>

<%
Scheme scheme = (Scheme) request.getAttribute("scheme");

List<Product> products = (List<Product>) request.getAttribute("products");

boolean editMode = scheme != null;
%>


<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title><%=editMode ? "Edit Scheme" : "Add Scheme"%></title>


<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>


<body class="bg-light">


	<div class="container mt-5">


		<div class="card shadow">


			<div class="card-header bg-primary text-white">

				<h4 class="mb-0">

					<%=editMode ? "Edit Scheme" : "Add New Scheme"%>

				</h4>

			</div>


			<div class="card-body">


				<form method="post" action="<%=request.getContextPath()%>/schemes">


					<!-- ================================================= -->
					<!-- ACTION -->
					<!-- ================================================= -->

					<input type="hidden" name="action"
						value="<%=editMode ? "update" : "add"%>">


					<%
					if (editMode) {
					%>

					<input type="hidden" name="schemeId"
						value="<%=scheme.getSchemeId()%>">

					<%
					}
					%>


					<!-- ================================================= -->
					<!-- SCHEME NAME -->
					<!-- ================================================= -->

					<div class="mb-3">

						<label class="form-label"> Scheme Name </label> <input type="text"
							name="schemeName" class="form-control" required
							value="<%=editMode ? scheme.getSchemeName() : ""%>">

					</div>


					<!-- ================================================= -->
					<!-- PRODUCT -->
					<!-- ================================================= -->

					<div class="mb-3">

						<label class="form-label"> Product </label> <select
							name="productId" class="form-select" required>


							<option value="">-- Select Product --</option>


							<%
							if (products != null) {

								for (Product product : products) {

									boolean selected = editMode && scheme.getProductId() == product.getProductId();
							%>


							<option value="<%=product.getProductId()%>"
								<%=selected ? "selected" : ""%>>


								<%=product.getProductName()%> -
								<%=product.getProductCode()%>


							</option>


							<%
							}

							}
							%>


						</select>

					</div>


					<!-- ================================================= -->
					<!-- SCHEME TYPE -->
					<!-- ================================================= -->

					<div class="mb-3">

						<label class="form-label"> Scheme Type </label> <select
							name="schemeType" id="schemeType" class="form-select" required
							onchange="toggleFields()">


							<option value="">-- Select Type --</option>


							<option value="BUY_GET_FREE"
								<%=editMode && "BUY_GET_FREE".equals(scheme.getSchemeType()) ? "selected" : ""%>>

								Buy X Get Y Free</option>


							<option value="PERCENTAGE_DISCOUNT"
								<%=editMode && "PERCENTAGE_DISCOUNT".equals(scheme.getSchemeType()) ? "selected" : ""%>>

								Percentage Discount</option>


						</select>

					</div>


					<!-- ================================================= -->
					<!-- MINIMUM QUANTITY -->
					<!-- ================================================= -->

					<div class="mb-3">

						<label class="form-label"> Minimum Quantity </label> <input
							type="number" name="minimumQuantity" class="form-control" min="1"
							required
							value="<%=editMode ? scheme.getMinimumQuantity() : "1"%>">

					</div>


					<!-- ================================================= -->
					<!-- FREE QUANTITY -->
					<!-- ================================================= -->

					<div class="mb-3" id="freeQuantityBox">


						<label class="form-label"> Free Quantity </label> <input
							type="number" name="freeQuantity" id="freeQuantity"
							class="form-control" min="0"
							value="<%=editMode ? scheme.getFreeQuantity() : "0"%>">

					</div>


					<!-- ================================================= -->
					<!-- DISCOUNT -->
					<!-- ================================================= -->

					<div class="mb-3" id="discountBox">


						<label class="form-label"> Discount Percentage </label> <input
							type="number" name="discountPercentage" id="discountPercentage"
							class="form-control" min="0" max="100" step="0.01"
							value="<%=editMode ? scheme.getDiscountPercentage() : "0"%>">

					</div>


					<!-- ================================================= -->
					<!-- START DATE -->
					<!-- ================================================= -->

					<div class="mb-3">

						<label class="form-label"> Start Date </label> <input type="date"
							name="startDate" class="form-control" required
							value="<%=editMode ? scheme.getStartDate() : ""%>">

					</div>


					<!-- ================================================= -->
					<!-- END DATE -->
					<!-- ================================================= -->

					<div class="mb-3">

						<label class="form-label"> End Date </label> <input type="date"
							name="endDate" class="form-control" required
							value="<%=editMode ? scheme.getEndDate() : ""%>">

					</div>


					<!-- ================================================= -->
					<!-- BUTTONS -->
					<!-- ================================================= -->

					<div class="text-end">


						<a href="<%=request.getContextPath()%>/schemes?action=list"
							class="btn btn-secondary"> Cancel </a>


						<button type="submit" class="btn btn-primary">

							<%=editMode ? "Update Scheme" : "Save Scheme"%>

						</button>


					</div>


				</form>

			</div>

		</div>

	</div>


	<script>
		// =========================================================
		// SHOW / HIDE SCHEME FIELDS
		// =========================================================

		function toggleFields() {

			const type = document.getElementById("schemeType").value;

			const freeBox = document.getElementById("freeQuantityBox");

			const discountBox = document.getElementById("discountBox");

			const freeInput = document.getElementById("freeQuantity");

			const discountInput = document.getElementById("discountPercentage");

			if (type === "BUY_GET_FREE") {

				freeBox.style.display = "block";

				discountBox.style.display = "none";

				freeInput.required = true;

				discountInput.required = false;

				discountInput.value = "0";

			}

			else if (type === "PERCENTAGE_DISCOUNT") {

				freeBox.style.display = "none";

				discountBox.style.display = "block";

				freeInput.required = false;

				discountInput.required = true;

				freeInput.value = "0";

			}

			else {

				freeBox.style.display = "block";

				discountBox.style.display = "block";

				freeInput.required = false;

				discountInput.required = false;

			}

		}

		// Run when page opens

		toggleFields();
	</script>


</body>

</html>