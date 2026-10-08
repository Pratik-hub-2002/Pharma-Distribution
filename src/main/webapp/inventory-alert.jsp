<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Inventory"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Inventory Alert</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body>

	<div class="container-fluid mt-4">

		<div class="d-flex justify-content-between align-items-center mb-4">

			<h2>
				<%=request.getAttribute("alertTitle")%>
			</h2>

			<a href="<%=request.getContextPath()%>/inventory"
				class="btn btn-secondary"> Back to Inventory </a>

		</div>


		<%
		List<Inventory> inventoryList = (List<Inventory>) request.getAttribute("inventoryList");
		%>


		<%
		if (inventoryList != null && !inventoryList.isEmpty()) {
		%>


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

					</tr>

				</thead>


				<tbody>

					<%
					for (Inventory inventory : inventoryList) {
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

						<td><strong> <%=inventory.getAvailableQuantity()%>
						</strong></td>

						<td><%=inventory.getReorderLevel()%></td>

					</tr>

					<%
					}
					%>

				</tbody>

			</table>

		</div>


		<%
		} else {
		%>

		<div class="alert alert-success">No records found for this
			alert.</div>

		<%
		}
		%>

	</div>

</body>

</html>