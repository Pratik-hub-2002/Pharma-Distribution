<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Inventory"%>
<%@ page import="com.pratik.pharma.model.Batch"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Inventory Form</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body>

	<div class="container mt-4">

		<%
		Inventory inventory = (Inventory) request.getAttribute("inventory");

		List<Batch> batches = (List<Batch>) request.getAttribute("batches");

		boolean editMode = inventory != null;
		%>

		<h2 class="mb-4">
			<%=editMode ? "Update Inventory" : "Add Inventory"%>
		</h2>

		<form method="post" action="<%=request.getContextPath()%>/inventory">

			<input type="hidden" name="action"
				value="<%=editMode ? "update" : "add"%>">

			<%
			if (editMode) {
			%>

			<input type="hidden" name="inventoryId"
				value="<%=inventory.getInventoryId()%>">

			<%
			}
			%>


			<!-- Batch -->

			<div class="mb-3">

				<label class="form-label"> Batch </label> <select name="batchId"
					class="form-select" required>

					<option value="">Select Batch</option>

					<%
					for (Batch batch : batches) {

						boolean selected = editMode && batch.getBatchId() == inventory.getBatchId();
					%>

					<option value="<%=batch.getBatchId()%>"
						<%=selected ? "selected" : ""%>>

						<%=batch.getBatchNumber()%> -
						<%=batch.getProductName()%> - Exp:
						<%=batch.getExpiryDate()%>

					</option>

					<%
					}
					%>

				</select>

			</div>


			<!-- Quantity -->

			<div class="mb-3">

				<label class="form-label"> Quantity </label> <input type="number"
					name="quantity" class="form-control" min="0" required
					value="<%=editMode ? inventory.getQuantity() : 0%>">

			</div>


			<!-- Reserved -->

			<div class="mb-3">

				<label class="form-label"> Reserved Quantity </label> <input
					type="number" name="reservedQuantity" class="form-control" min="0"
					required
					value="<%=editMode ? inventory.getReservedQuantity() : 0%>">

			</div>


			<!-- Damaged -->

			<div class="mb-3">

				<label class="form-label"> Damaged Quantity </label> <input
					type="number" name="damagedQuantity" class="form-control" min="0"
					required value="<%=editMode ? inventory.getDamagedQuantity() : 0%>">

			</div>


			<!-- Reorder -->

			<div class="mb-3">

				<label class="form-label"> Reorder Level </label> <input
					type="number" name="reorderLevel" class="form-control" min="0"
					required value="<%=editMode ? inventory.getReorderLevel() : 20%>">

			</div>


			<button type="submit" class="btn btn-primary">

				<%=editMode ? "Update Inventory" : "Add Inventory"%>

			</button>

			<a href="<%=request.getContextPath()%>/inventory"
				class="btn btn-secondary"> Cancel </a>

		</form>

	</div>

</body>
</html>