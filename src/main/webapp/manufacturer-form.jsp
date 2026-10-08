<%@ page import="com.pratik.pharma.model.Manufacturer"%>

<%
Manufacturer manufacturer = (Manufacturer) request.getAttribute("manufacturer");

boolean editMode = manufacturer != null;
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title><%=editMode ? "Edit Manufacturer" : "Add Manufacturer"%>
</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body>

	<div class="container mt-4">

		<div class="card shadow-sm">

			<div class="card-header">

				<h4 class="mb-0">

					<%=editMode ? "Edit Manufacturer" : "Add Manufacturer"%>

				</h4>

			</div>

			<div class="card-body">

				<%
				if (request.getAttribute("error") != null) {
				%>

				<div class="alert alert-danger">

					<%=request.getAttribute("error")%>

				</div>

				<%
				}
				%>


				<form method="post"
					action="<%=request.getContextPath()%>/manufacturers">

					<input type="hidden" name="action" value="save">


					<%
					if (editMode) {
					%>

					<input type="hidden" name="manufacturerId"
						value="<%=manufacturer.getManufacturerId()%>">

					<%
					}
					%>


					<!-- ================================================= -->
					<!-- NAME / CONTACT -->
					<!-- ================================================= -->

					<div class="row mb-3">

						<div class="col-md-6">

							<label class="form-label"> Manufacturer Name * </label> <input
								type="text" name="manufacturerName" class="form-control"
								maxlength="150" required
								value="<%=editMode && manufacturer.getManufacturerName() != null ? manufacturer.getManufacturerName() : ""%>">

						</div>


						<div class="col-md-6">

							<label class="form-label"> Contact Person </label> <input
								type="text" name="contactPerson" class="form-control"
								maxlength="100"
								value="<%=editMode && manufacturer.getContactPerson() != null ? manufacturer.getContactPerson() : ""%>">

						</div>

					</div>


					<!-- ================================================= -->
					<!-- EMAIL / PHONE -->
					<!-- ================================================= -->

					<div class="row mb-3">

						<div class="col-md-6">

							<label class="form-label"> Email </label> <input type="email"
								name="email" class="form-control" maxlength="100"
								value="<%=editMode && manufacturer.getEmail() != null ? manufacturer.getEmail() : ""%>">

						</div>


						<div class="col-md-6">

							<label class="form-label"> Phone </label> <input type="text"
								name="phone" class="form-control" maxlength="15"
								value="<%=editMode && manufacturer.getPhone() != null ? manufacturer.getPhone() : ""%>">

						</div>

					</div>


					<!-- ================================================= -->
					<!-- ADDRESS -->
					<!-- ================================================= -->

					<div class="mb-3">

						<label class="form-label"> Address </label>

						<textarea name="address" class="form-control" rows="3"
							maxlength="255"><%=editMode && manufacturer.getAddress() != null ? manufacturer.getAddress() : ""%></textarea>

					</div>


					<!-- ================================================= -->
					<!-- CITY / STATE -->
					<!-- ================================================= -->

					<div class="row mb-3">

						<div class="col-md-6">

							<label class="form-label"> City </label> <input type="text"
								name="city" class="form-control" maxlength="50"
								value="<%=editMode && manufacturer.getCity() != null ? manufacturer.getCity() : ""%>">

						</div>


						<div class="col-md-6">

							<label class="form-label"> State </label> <input type="text"
								name="state" class="form-control" maxlength="50"
								value="<%=editMode && manufacturer.getState() != null ? manufacturer.getState() : ""%>">

						</div>

					</div>


					<!-- ================================================= -->
					<!-- GST / STATUS -->
					<!-- ================================================= -->

					<div class="row mb-4">

						<div class="col-md-6">

							<label class="form-label"> GST Number </label> <input type="text"
								name="gstNumber" class="form-control" maxlength="20"
								value="<%=editMode && manufacturer.getGstNumber() != null ? manufacturer.getGstNumber() : ""%>">

						</div>


						<div class="col-md-6">

							<label class="form-label"> Status </label> <select name="status"
								class="form-select">

								<option value="ACTIVE"
									<%=editMode && "ACTIVE".equals(manufacturer.getStatus()) ? "selected" : ""%>>

									ACTIVE</option>

								<option value="INACTIVE"
									<%=editMode && "INACTIVE".equals(manufacturer.getStatus()) ? "selected" : ""%>>

									INACTIVE</option>

							</select>

						</div>

					</div>


					<!-- ================================================= -->
					<!-- BUTTONS -->
					<!-- ================================================= -->

					<div class="text-end">

						<a href="<%=request.getContextPath()%>/manufacturers"
							class="btn btn-secondary me-2"> Cancel </a>

						<button type="submit" class="btn btn-primary">

							<%=editMode ? "Update Manufacturer" : "Save Manufacturer"%>

						</button>

					</div>

				</form>

			</div>

		</div>

	</div>

</body>

</html>