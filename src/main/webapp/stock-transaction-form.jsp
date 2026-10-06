<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Batch"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Add Stock Transaction</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body>

	<!-- =====================================================
         ERROR MESSAGE
         ===================================================== -->

	<%
	String stockError = (String) session.getAttribute("stockError");

	if (stockError != null) {
	%>

	<div class="container mt-3">

		<div class="alert alert-danger alert-dismissible fade show"
			role="alert">

			<strong>⚠️ Stock Error:</strong>

			<%=stockError%>

			<button type="button" class="btn-close" data-bs-dismiss="alert"
				aria-label="Close"></button>

		</div>

	</div>

	<%
	session.removeAttribute("stockError");
	}
	%>


	<!-- =====================================================
         PAGE CONTAINER
         ===================================================== -->

	<div class="container mt-4">

		<h2 class="mb-4">Add Stock Transaction</h2>


		<!-- =================================================
             STOCK TRANSACTION FORM
             ================================================= -->

		<form method="post"
			action="<%=request.getContextPath()%>/stock-transactions">


			<!-- =============================================
                 BATCH
                 ============================================= -->

			<div class="mb-3">

				<label class="form-label"> Batch </label> <select name="batchId"
					class="form-select" required>

					<option value="">Select Batch</option>

					<%
					List<Batch> batches = (List<Batch>) request.getAttribute("batches");

					if (batches != null) {

						for (Batch batch : batches) {
					%>

					<option value="<%=batch.getBatchId()%>">

						<%=batch.getBatchNumber()%> -
						<%=batch.getProductName()%> - Exp:
						<%=batch.getExpiryDate()%>

					</option>

					<%
					}
					}
					%>

				</select>

			</div>


			<!-- =============================================
                 TRANSACTION TYPE
                 ============================================= -->

			<div class="mb-3">

				<label class="form-label"> Transaction Type </label> <select
					name="transactionType" class="form-select" required>

					<option value="">Select Transaction Type</option>

					<option value="IN">IN</option>

					<option value="OUT">OUT</option>

					<option value="DAMAGE">DAMAGE</option>

					<option value="ADJUSTMENT">ADJUSTMENT</option>

				</select>

			</div>


			<!-- =============================================
                 QUANTITY
                 ============================================= -->

			<div class="mb-3">

				<label class="form-label"> Quantity </label> <input type="number"
					name="quantity" class="form-control" min="1" required>

			</div>


			<!-- =============================================
                 REFERENCE TYPE
                 ============================================= -->

			<div class="mb-3">

				<label class="form-label"> Reference Type </label> <select
					name="referenceType" class="form-select">

					<option value="">Select Reference Type</option>

					<option value="PURCHASE">PURCHASE</option>

					<option value="ORDER">ORDER</option>

					<option value="DAMAGE">DAMAGE</option>

					<option value="MANUAL">MANUAL</option>

					<option value="ADJUSTMENT">ADJUSTMENT</option>

				</select>

			</div>


			<!-- =============================================
                 REFERENCE ID
                 ============================================= -->

			<div class="mb-3">

				<label class="form-label"> Reference ID </label> <input
					type="number" name="referenceId" class="form-control" min="1">

			</div>


			<!-- =============================================
                 REMARKS
                 ============================================= -->

			<div class="mb-3">

				<label class="form-label"> Remarks </label>

				<textarea name="remarks" class="form-control" rows="3"
					maxlength="255"></textarea>

			</div>


			<!-- =============================================
                 BUTTONS
                 ============================================= -->

			<button type="submit" class="btn btn-primary">Save
				Transaction</button>


			<a href="<%=request.getContextPath()%>/stock-transactions"
				class="btn btn-secondary"> Cancel </a>

		</form>

	</div>


	<!-- =====================================================
         BOOTSTRAP JAVASCRIPT
         Required for dismissible alert
         ===================================================== -->

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
		
	</script>

</body>

</html>