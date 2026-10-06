<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Product"%>

<%
List<Product> products = (List<Product>) request.getAttribute("products");
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Product Management</title>

<meta name="viewport" content="width=device-width, initial-scale=1">

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>


<body>

	<div class="container-fluid mt-4 mb-5">


		<!-- ===================================== -->
		<!-- HEADER -->
		<!-- ===================================== -->

		<div class="d-flex justify-content-between align-items-center mb-4">

			<div>

				<h2 class="fw-bold">Product Management</h2>

				<p class="text-muted mb-0">Manage pharmaceutical products</p>

			</div>


			<!-- ADD PRODUCT -->

			<a href="<%=request.getContextPath()%>/products?action=add"
				class="btn btn-success"> + Add Product </a>

		</div>



		<!-- ===================================== -->
		<!-- PRODUCT TABLE CARD -->
		<!-- ===================================== -->

		<div class="card shadow-sm">

			<div class="card-header bg-primary text-white">

				<h5 class="mb-0">Product List</h5>

			</div>


			<div class="card-body">


				<!-- ===================================== -->
				<!-- RESPONSIVE TABLE -->
				<!-- ===================================== -->

				<div class="table-responsive">

					<table class="table table-bordered table-hover align-middle">

						<!-- ===================================== -->
						<!-- TABLE HEADER -->
						<!-- ===================================== -->

						<thead class="table-dark">

							<tr>

								<th>ID</th>

								<th>Code</th>

								<th>Product Name</th>

								<th>Category</th>

								<th>Manufacturer</th>

								<th>Dosage</th>

								<th>Strength</th>

								<th>Unit</th>

								<th>HSN</th>

								<th>GST %</th>

								<th>MRP</th>

								<th>Selling Price</th>

								<th>Cold Chain</th>

								<th>Status</th>

								<th>Action</th>

							</tr>

						</thead>


						<!-- ===================================== -->
						<!-- TABLE BODY -->
						<!-- ===================================== -->

						<tbody>


							<%
							if (products != null && !products.isEmpty()) {

								for (Product product : products) {
							%>


							<tr>


								<!-- ================================= -->
								<!-- ID -->
								<!-- ================================= -->

								<td><%=product.getProductId()%></td>


								<!-- ================================= -->
								<!-- PRODUCT CODE -->
								<!-- ================================= -->

								<td><span class="fw-semibold"> <%=product.getProductCode()%>
								</span></td>


								<!-- ================================= -->
								<!-- PRODUCT NAME -->
								<!-- ================================= -->

								<td><strong> <%=product.getProductName()%>
								</strong></td>


								<!-- ================================= -->
								<!-- CATEGORY -->
								<!-- ================================= -->

								<td><%=product.getCategoryName()%></td>


								<!-- ================================= -->
								<!-- MANUFACTURER -->
								<!-- ================================= -->

								<td><%=product.getManufacturerName()%></td>


								<!-- ================================= -->
								<!-- DOSAGE FORM -->
								<!-- ================================= -->

								<td><%=product.getDosageForm()%></td>


								<!-- ================================= -->
								<!-- STRENGTH -->
								<!-- ================================= -->

								<td><%=product.getStrength()%></td>


								<!-- ================================= -->
								<!-- UNIT -->
								<!-- ================================= -->

								<td><%=product.getUnit()%></td>


								<!-- ================================= -->
								<!-- HSN -->
								<!-- ================================= -->

								<td><%=product.getHsnCode()%></td>


								<!-- ================================= -->
								<!-- GST -->
								<!-- ================================= -->

								<td><%=product.getGstRate()%>%</td>


								<!-- ================================= -->
								<!-- MRP -->
								<!-- ================================= -->

								<td>₹ <%=product.getMrp()%>

								</td>


								<!-- ================================= -->
								<!-- SELLING PRICE -->
								<!-- ================================= -->

								<td>₹ <%=product.getSellingPrice()%>

								</td>


								<!-- ================================= -->
								<!-- COLD CHAIN -->
								<!-- ================================= -->

								<td>
									<%
									if (product.isColdChainRequired()) {
									%> <span class="badge bg-warning text-dark"> YES </span> <%
 } else {
 %> <span class="badge bg-secondary"> NO </span> <%
 }
 %>

								</td>


								<!-- ================================= -->
								<!-- STATUS -->
								<!-- ================================= -->

								<td>
									<%
									if ("ACTIVE".equalsIgnoreCase(product.getStatus())) {
									%> <span class="badge bg-success"> ACTIVE </span> <%
 } else {
 %> <span class="badge bg-danger"> INACTIVE </span> <%
 }
 %>

								</td>


								<!-- ================================= -->
								<!-- ACTION -->
								<!-- ================================= -->

								<td>

									<div class="d-flex gap-1">


										<!-- ============================= -->
										<!-- EDIT -->
										<!-- ============================= -->

										<a
											href="<%=request.getContextPath()%>/products?action=edit&productId=<%=product.getProductId()%>"
											class="btn btn-sm btn-primary"> Edit </a>


										<!-- ============================= -->
										<!-- ACTIVATE / DEACTIVATE -->
										<!-- ============================= -->

										<a
											href="<%=request.getContextPath()%>/products?action=status&productId=<%=product.getProductId()%>"
											class="btn btn-sm <%="ACTIVE".equalsIgnoreCase(product.getStatus())

		? "btn-danger"
		: "btn-success"%>"
											onclick="return confirm(
											'Are you sure you want to change the product status?'
										);">

											<%="ACTIVE".equalsIgnoreCase(product.getStatus())

		? "Deactivate"
		: "Activate"%>

										</a>

									</div>

								</td>


							</tr>


							<%
							}

							} else {
							%>


							<!-- ================================= -->
							<!-- NO PRODUCTS -->
							<!-- ================================= -->

							<tr>

								<td colspan="15" class="text-center text-muted py-4">No
									products found.</td>

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


	<!-- ===================================== -->
	<!-- BOOTSTRAP JS -->
	<!-- ===================================== -->

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
		
	</script>

</body>

</html>