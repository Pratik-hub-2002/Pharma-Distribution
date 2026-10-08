<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Manufacturer"%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<title>Manufacturer Management - Pharma Distribution</title>

<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Bootstrap -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body>

	<!-- ===================================================== -->
	<!-- HEADER -->
	<!-- ===================================================== -->

	<nav class="navbar navbar-dark bg-dark">

		<div class="container-fluid">

			<a class="navbar-brand" href="dashboard.jsp"> 💊 Pharma
				Distribution </a>

			<div class="text-white">

				Welcome, <strong> <%=session.getAttribute("username")%>
				</strong> <span class="ms-2"> <%=session.getAttribute("roleName") != null ? session.getAttribute("roleName") : "SUPER_ADMIN"%>

				</span>

			</div>

		</div>

	</nav>


	<!-- ===================================================== -->
	<!-- MAIN CONTAINER -->
	<!-- ===================================================== -->

	<div class="container-fluid mt-4">

		<!-- ================================================= -->
		<!-- PAGE HEADER -->
		<!-- ================================================= -->

		<div class="d-flex justify-content-between align-items-center mb-4">

			<div>

				<h2 class="mb-1">Manufacturer Management</h2>

				<p class="text-muted mb-0">Manage pharmaceutical manufacturers
					and their details.</p>

			</div>


			<!-- ADD MANUFACTURER BUTTON -->

			<div>

				<a href="<%=request.getContextPath()%>/manufacturers?action=add"
					class="btn btn-primary"> ➕ Add Manufacturer </a> <a
					href="<%=request.getContextPath()%>/dashboard.jsp"
					class="btn btn-secondary ms-2"> ← Dashboard </a>

			</div>

		</div>


		<!-- ================================================= -->
		<!-- SUCCESS / ERROR MESSAGE -->
		<!-- ================================================= -->

		<%
		String success = request.getParameter("success");
		String error = request.getParameter("error");
		%>

		<%
		if ("added".equals(success)) {
		%>

		<div class="alert alert-success">Manufacturer added
			successfully.</div>

		<%
		}
		%>


		<%
		if ("updated".equals(success)) {
		%>

		<div class="alert alert-success">Manufacturer updated
			successfully.</div>

		<%
		}
		%>


		<%
		if ("status".equals(success)) {
		%>

		<div class="alert alert-success">Manufacturer status updated
			successfully.</div>

		<%
		}
		%>


		<%
		if (error != null) {
		%>

		<div class="alert alert-danger">

			<%=error%>

		</div>

		<%
		}
		%>


		<!-- ================================================= -->
		<!-- MANUFACTURER TABLE -->
		<!-- ================================================= -->

		<div class="card shadow-sm">

			<div class="card-header bg-primary text-white">

				<strong> Manufacturer List </strong>

			</div>


			<div class="card-body">

				<%
				List<Manufacturer> manufacturers = (List<Manufacturer>) request.getAttribute("manufacturers");
				%>


				<%
				if (manufacturers == null || manufacturers.isEmpty()) {
				%>

				<div class="alert alert-info mb-0">No manufacturers found.</div>

				<%
				} else {
				%>


				<div class="table-responsive">

					<table
						class="table table-bordered
                                      table-hover align-middle">

						<thead class="table-dark">

							<tr>

								<th>ID</th>

								<th>Manufacturer Name</th>

								<th>Contact Person</th>

								<th>Email</th>

								<th>Phone</th>

								<th>Address</th>

								<th>City</th>

								<th>State</th>

								<th>GST Number</th>

								<th>Status</th>

								<th>Actions</th>

							</tr>

						</thead>


						<tbody>

							<%
							for (Manufacturer manufacturer : manufacturers) {
							%>

							<tr>

								<!-- ID -->

								<td><%=manufacturer.getManufacturerId()%></td>


								<!-- NAME -->

								<td><strong> <%=manufacturer.getManufacturerName()%>

								</strong></td>


								<!-- CONTACT PERSON -->

								<td><%=manufacturer.getContactPerson() != null ? manufacturer.getContactPerson() : "-"%></td>


								<!-- EMAIL -->

								<td><%=manufacturer.getEmail() != null ? manufacturer.getEmail() : "-"%></td>


								<!-- PHONE -->

								<td><%=manufacturer.getPhone() != null ? manufacturer.getPhone() : "-"%></td>


								<!-- ADDRESS -->

								<td><%=manufacturer.getAddress() != null ? manufacturer.getAddress() : "-"%></td>


								<!-- CITY -->

								<td><%=manufacturer.getCity() != null ? manufacturer.getCity() : "-"%></td>


								<!-- STATE -->

								<td><%=manufacturer.getState() != null ? manufacturer.getState() : "-"%></td>


								<!-- GST -->

								<td><%=manufacturer.getGstNumber() != null ? manufacturer.getGstNumber() : "-"%></td>


								<!-- STATUS -->

								<td>
									<%
									if ("ACTIVE".equals(manufacturer.getStatus())) {
									%>

									<span class="badge bg-success"> ACTIVE </span> <%
 } else {
 %> <span
									class="badge bg-secondary"> INACTIVE </span> <%
 }
 %>

								</td>


								<!-- ACTIONS -->

								<td>

									<div class="d-flex gap-2">

										<!-- EDIT -->

										<a
											href="<%=request.getContextPath()%>/manufacturers?action=edit&id=<%=manufacturer.getManufacturerId()%>"
											class="btn btn-sm btn-warning"> ✏️ Edit </a>


										<!-- DEACTIVATE -->

										<%
										if ("ACTIVE".equals(manufacturer.getStatus())) {
										%>

										<a
											href="<%=request.getContextPath()%>/manufacturers?action=status&id=<%=manufacturer.getManufacturerId()%>&status=INACTIVE"
											class="btn btn-sm btn-danger"
											onclick="return confirm('Are you sure you want to deactivate this manufacturer?');">

											Deactivate </a>

										<%
										} else {
										%>


										<!-- ACTIVATE -->

										<a
											href="<%=request.getContextPath()%>/manufacturers?action=status&id=<%=manufacturer.getManufacturerId()%>&status=ACTIVE"
											class="btn btn-sm btn-success"
											onclick="return confirm('Are you sure you want to activate this manufacturer?');">

											Activate </a>

										<%
										}
										%>

									</div>

								</td>

							</tr>

							<%
							}
							%>

						</tbody>

					</table>

				</div>

				<%
				}
				%>

			</div>

		</div>

	</div>


	<!-- Bootstrap JS -->

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
		
	</script>

</body>

</html>