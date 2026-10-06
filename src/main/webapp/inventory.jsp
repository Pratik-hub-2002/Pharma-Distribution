<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Inventory"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Inventory Management</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body>

	<div class="container-fluid mt-4">

		<div class="d-flex justify-content-between mb-3">

			<h2>Inventory Management</h2>

			<a href="<%=request.getContextPath()%>/inventory?action=add"
				class="btn btn-primary"> Add Inventory </a>

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

						<td><%=inventory.getInventoryId()%></td>

						<td><%=inventory.getProductName()%></td>

						<td><%=inventory.getProductCode()%></td>

						<td><%=inventory.getBatchNumber()%></td>

						<td><%=inventory.getExpiryDate()%></td>

						<td><%=inventory.getQuantity()%></td>

						<td><%=inventory.getReservedQuantity()%></td>

						<td><%=inventory.getDamagedQuantity()%></td>

						<td><strong> <%=available%>
						</strong></td>

						<td><%=inventory.getReorderLevel()%></td>

						<td>
							<%
							if ("OUT OF STOCK".equals(stockStatus)) {
							%> <span class="badge bg-danger"> OUT OF STOCK </span> <%
 } else if ("LOW STOCK".equals(stockStatus)) {
 %> <span class="badge bg-warning text-dark"> LOW STOCK </span> <%
 } else {
 %> <span class="badge bg-success"> AVAILABLE </span> <%
 }
 %>

						</td>

						<td><%=inventory.getUpdatedAt()%></td>

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

</body>

</html>