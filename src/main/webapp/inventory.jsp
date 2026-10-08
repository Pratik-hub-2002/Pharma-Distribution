<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Inventory"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Inventory Management</title>

<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Bootstrap -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<style>
body {
	background-color: #f5f7fb;
}

.page-title {
	margin-top: 25px;
	margin-bottom: 20px;
}

.summary-card {
	border: none;
	border-radius: 12px;
	height: 100%;
}

.summary-card h6 {
	color: #6c757d;
	margin-bottom: 8px;
}

.summary-card h2 {
	font-weight: 700;
	margin: 0;
}

.alert-card {
	border-radius: 12px;
	height: 100%;
}

.table-container {
	background-color: white;
	padding: 20px;
	border-radius: 12px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}
</style>

</head>


<body>


	<div class="container-fluid">


		<!-- =====================================================
         PAGE HEADER
    ====================================================== -->

		<div
			class="d-flex justify-content-between align-items-center page-title">

			<div>

				<h2>📦 Inventory Management</h2>

				<p class="text-muted mb-0">Monitor stock, availability, damaged
					quantities and inventory alerts.</p>

			</div>


			<div>

				<a href="<%=request.getContextPath()%>/inventory?action=low-stock"
					class="btn btn-warning me-2"> ⚠ Low Stock </a> <a
					href="<%=request.getContextPath()%>/inventory?action=near-expiry"
					class="btn btn-info me-2"> ⏳ Expiring Soon </a> <a
					href="<%=request.getContextPath()%>/inventory?action=expired"
					class="btn btn-danger me-2"> ❌ Expired </a> <a
					href="<%=request.getContextPath()%>/inventory?action=add"
					class="btn btn-primary"> + Add Inventory </a>

			</div>

		</div>



		<!-- =====================================================
         INVENTORY SUMMARY
    ====================================================== -->

		<div class="row g-3 mb-4">


			<!-- TOTAL BATCHES -->

			<div class="col-md-6 col-lg-3">

				<div class="card summary-card shadow-sm">

					<div class="card-body">

						<h6>Total Batches</h6>

						<h2>
							<%=request.getAttribute("totalBatches") != null ? request.getAttribute("totalBatches") : "—"%>
						</h2>

						<small class="text-muted"> Inventory records </small>

					</div>

				</div>

			</div>


			<!-- TOTAL QUANTITY -->

			<div class="col-md-6 col-lg-3">

				<div class="card summary-card shadow-sm">

					<div class="card-body">

						<h6>Total Quantity</h6>

						<h2>
							<%=request.getAttribute("totalQuantity") != null ? request.getAttribute("totalQuantity") : "—"%>
						</h2>

						<small class="text-muted"> Physical stock </small>

					</div>

				</div>

			</div>


			<!-- AVAILABLE -->

			<div class="col-md-6 col-lg-3">

				<div class="card summary-card shadow-sm">

					<div class="card-body">

						<h6>Available Stock</h6>

						<h2>
							<%=request.getAttribute("totalAvailable") != null ? request.getAttribute("totalAvailable") : "—"%>
						</h2>

						<small class="text-muted"> Ready for sale </small>

					</div>

				</div>

			</div>


			<!-- RESERVED -->

			<div class="col-md-6 col-lg-3">

				<div class="card summary-card shadow-sm">

					<div class="card-body">

						<h6>Reserved Stock</h6>

						<h2>
							<%=request.getAttribute("totalReserved") != null ? request.getAttribute("totalReserved") : "—"%>
						</h2>

						<small class="text-muted"> Reserved quantity </small>

					</div>

				</div>

			</div>

		</div>



		<!-- =====================================================
         DAMAGED + ALERT SUMMARY
    ====================================================== -->

		<div class="row g-3 mb-4">


			<!-- DAMAGED -->

			<div class="col-md-6 col-lg-3">

				<div class="card summary-card shadow-sm">

					<div class="card-body">

						<h6>Damaged Stock</h6>

						<h2>
							<%=request.getAttribute("totalDamaged") != null ? request.getAttribute("totalDamaged") : "—"%>
						</h2>

						<small class="text-muted"> Damaged quantity </small>

					</div>

				</div>

			</div>


			<!-- LOW STOCK -->

			<div class="col-md-6 col-lg-3">

				<div class="card alert-card shadow-sm border-warning">

					<div class="card-body">

						<h6>⚠ Low Stock</h6>

						<h2>
							<%=request.getAttribute("lowStockCount") != null ? request.getAttribute("lowStockCount") : "—"%>
						</h2>

						<a href="<%=request.getContextPath()%>/inventory?action=low-stock"
							class="btn btn-warning btn-sm mt-2"> View Low Stock </a>

					</div>

				</div>

			</div>


			<!-- NEAR EXPIRY -->

			<div class="col-md-6 col-lg-3">

				<div class="card alert-card shadow-sm border-info">

					<div class="card-body">

						<h6>⏳ Expiring Soon</h6>

						<h2>
							<%=request.getAttribute("nearExpiryCount") != null ? request.getAttribute("nearExpiryCount") : "—"%>
						</h2>

						<a
							href="<%=request.getContextPath()%>/inventory?action=near-expiry"
							class="btn btn-info btn-sm mt-2"> View Expiring </a>

					</div>

				</div>

			</div>


			<!-- EXPIRED -->

			<div class="col-md-6 col-lg-3">

				<div class="card alert-card shadow-sm border-danger">

					<div class="card-body">

						<h6>❌ Expired</h6>

						<h2>
							<%=request.getAttribute("expiredCount") != null ? request.getAttribute("expiredCount") : "—"%>
						</h2>

						<a href="<%=request.getContextPath()%>/inventory?action=expired"
							class="btn btn-danger btn-sm mt-2"> View Expired </a>

					</div>

				</div>

			</div>

		</div>



		<!-- =====================================================
         INVENTORY TABLE
    ====================================================== -->

		<div class="table-container">


			<div class="d-flex justify-content-between align-items-center mb-3">

				<h4 class="mb-0">Inventory Records</h4>

				<span class="text-muted"> Current Stock </span>

			</div>


			<div class="table-responsive">

				<table class="table table-bordered table-hover align-middle">

					<thead class="table-dark">

						<tr>

							<th>ID</th>

							<th>Product</th>

							<th>Code</th>

							<th>Batch</th>

							<th>Expiry</th>

							<th>Quantity</th>

							<th>Reserved</th>

							<th>Damaged</th>

							<th>Available</th>

							<th>Reorder Level</th>

							<th>Stock Status</th>

							<th>Updated At</th>

							<th>Action</th>

						</tr>

					</thead>


					<tbody>

						<%
						List<Inventory> inventoryList = (List<Inventory>) request.getAttribute("inventoryList");

						if (inventoryList != null && !inventoryList.isEmpty()) {

							for (Inventory inventory : inventoryList) {

								int available = inventory.getAvailableQuantity();

								String stockStatus;

								if (available <= 0) {

							stockStatus = "OUT OF STOCK";

								} else if (available <= inventory.getReorderLevel()) {

							stockStatus = "LOW STOCK";

								} else {

							stockStatus = "AVAILABLE";

								}
						%>


						<tr>


							<!-- ID -->

							<td><%=inventory.getInventoryId()%></td>


							<!-- PRODUCT -->

							<td><%=inventory.getProductName()%></td>


							<!-- CODE -->

							<td><%=inventory.getProductCode()%></td>


							<!-- BATCH -->

							<td><%=inventory.getBatchNumber()%></td>


							<!-- EXPIRY -->

							<td><%=inventory.getExpiryDate()%></td>


							<!-- QUANTITY -->

							<td><%=inventory.getQuantity()%></td>


							<!-- RESERVED -->

							<td><%=inventory.getReservedQuantity()%></td>


							<!-- DAMAGED -->

							<td><%=inventory.getDamagedQuantity()%></td>


							<!-- AVAILABLE -->

							<td><strong> <%=available%>
							</strong></td>


							<!-- REORDER LEVEL -->

							<td><%=inventory.getReorderLevel()%></td>


							<!-- STOCK STATUS -->

							<td>
								<%
								if ("OUT OF STOCK".equals(stockStatus)) {
								%> <span class="badge bg-danger"> OUT OF
									STOCK </span> <%
 } else if ("LOW STOCK".equals(stockStatus)) {
 %> <span class="badge bg-warning text-dark">
									LOW STOCK </span> <%
 } else {
 %> <span class="badge bg-success"> AVAILABLE
							</span> <%
 }
 %>

							</td>


							<!-- UPDATED -->

							<td><%=inventory.getUpdatedAt()%></td>


							<!-- ACTION -->

							<td><a
								href="<%=request.getContextPath()%>/inventory?action=edit&inventoryId=<%=inventory.getInventoryId()%>"
								class="btn btn-sm btn-warning"> Edit </a></td>


						</tr>


						<%
						}

						} else {
						%>


						<tr>

							<td colspan="13" class="text-center">No inventory records
								found.</td>

						</tr>


						<%
						}
						%>

					</tbody>

				</table>

			</div>

		</div>


	</div>


	<!-- Bootstrap JS -->

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
		
	</script>


</body>

</html>