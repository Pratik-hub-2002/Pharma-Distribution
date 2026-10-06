<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Batch"%>

<%
List<Batch> batches = (List<Batch>) request.getAttribute("batches");
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Batch Management</title>

<meta name="viewport" content="width=device-width, initial-scale=1">

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body>

	<div class="container-fluid mt-4 mb-5">


		<!-- HEADER -->

		<div class="d-flex justify-content-between
		align-items-center mb-4">

			<div>

				<h2 class="fw-bold">Batch Management</h2>

				<p class="text-muted mb-0">Manage product batches and expiry
					information</p>

			</div>


			<a href="<%=request.getContextPath()%>/batches?action=add"
				class="btn btn-success"> + Add Batch </a>

		</div>


		<!-- TABLE -->

		<div class="card shadow-sm">

			<div class="card-header bg-primary text-white">

				<h5 class="mb-0">Batch List</h5>

			</div>


			<div class="card-body">

				<div class="table-responsive">

					<table class="table table-bordered
					table-hover align-middle">

						<thead class="table-dark">

							<tr>

								<th>ID</th>

								<th>Product</th>

								<th>Batch Number</th>

								<th>Manufacturing Date</th>

								<th>Expiry Date</th>

								<th>Purchase Price</th>

								<th>Status</th>

								<th>Action</th>

							</tr>

						</thead>


						<tbody>

							<%
							if (batches != null && !batches.isEmpty()) {

								for (Batch batch : batches) {
							%>

							<tr>


								<!-- ID -->

								<td><%=batch.getBatchId()%></td>


								<!-- PRODUCT -->

								<td><strong> <%=batch.getProductCode()%>
								</strong> <br> <small class="text-muted"> <%=batch.getProductName()%>
								</small></td>


								<!-- BATCH -->

								<td><%=batch.getBatchNumber()%></td>


								<!-- MANUFACTURING DATE -->

								<td><%=batch.getManufacturingDate()%></td>


								<!-- EXPIRY DATE -->

								<td><%=batch.getExpiryDate()%></td>


								<!-- PURCHASE PRICE -->

								<td>₹ <%=batch.getPurchasePrice()%>
								</td>


								<!-- STATUS -->

								<td>
									<%
									if ("ACTIVE".equalsIgnoreCase(batch.getStatus())) {
									%> <span class="badge bg-success"> ACTIVE </span> <%
 } else {
 %> <span class="badge bg-danger"> INACTIVE </span> <%
 }
 %>

								</td>


								<!-- ACTION -->

								<td>

									<div class="d-flex gap-1">

										<!-- EDIT -->

										<a
											href="<%=request.getContextPath()%>/batches?action=edit&batchId=<%=batch.getBatchId()%>"
											class="btn btn-sm btn-primary"> Edit </a>


										<!-- STATUS -->

										<a
											href="<%=request.getContextPath()%>/batches?action=status&batchId=<%=batch.getBatchId()%>"
											class="btn btn-sm <%="ACTIVE".equalsIgnoreCase(batch.getStatus())

		? "btn-danger"
		: "btn-success"%>"
											onclick="return confirm(
											'Are you sure you want to change the batch status?'
										);">

											<%="ACTIVE".equalsIgnoreCase(batch.getStatus())

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

							<tr>

								<td colspan="8" class="text-center text-muted py-4">No
									batches found.</td>

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

</body>

</html>