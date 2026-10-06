<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Scheme"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Scheme Management</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>


<body class="bg-light">


	<div class="container-fluid mt-4">


		<!-- ================================================= -->
		<!-- HEADER -->
		<!-- ================================================= -->

		<div class="d-flex justify-content-between align-items-center mb-4">

			<div>

				<h2>🏷️ Scheme Management</h2>

				<p class="text-muted">Manage promotional schemes for medicines.
				</p>

			</div>


			<div>

				<a href="<%=request.getContextPath()%>/dashboard.jsp"
					class="btn btn-secondary"> Dashboard </a> <a
					href="<%=request.getContextPath()%>/schemes?action=add"
					class="btn btn-primary"> + Add Scheme </a>

			</div>

		</div>


		<!-- ================================================= -->
		<!-- TABLE -->
		<!-- ================================================= -->

		<div class="card shadow-sm">

			<div class="card-body">


				<div class="table-responsive">

					<table class="table table-bordered table-hover align-middle">


						<thead class="table-dark">

							<tr>

								<th>ID</th>

								<th>Scheme Name</th>

								<th>Product</th>

								<th>Type</th>

								<th>Minimum Qty</th>

								<th>Free Qty</th>

								<th>Discount %</th>

								<th>Start Date</th>

								<th>End Date</th>

								<th>Status</th>

								<th>Action</th>

							</tr>

						</thead>


						<tbody>


							<%
							List<Scheme> schemes = (List<Scheme>) request.getAttribute("schemes");

							if (schemes != null && !schemes.isEmpty()) {

								for (Scheme scheme : schemes) {
							%>


							<tr>


								<td><%=scheme.getSchemeId()%></td>


								<td><strong> <%=scheme.getSchemeName()%>
								</strong></td>


								<td><%=scheme.getProductName()%></td>


								<td>
									<%
									if ("BUY_GET_FREE".equals(scheme.getSchemeType())) {
									%> <span class="badge bg-info text-dark">

										Buy X Get Y Free </span> <%
 } else {
 %> <span
									class="badge bg-warning text-dark"> Percentage Discount

								</span> <%
 }
 %>

								</td>


								<td><%=scheme.getMinimumQuantity()%></td>


								<td><%=scheme.getFreeQuantity()%></td>


								<td><%=scheme.getDiscountPercentage()%>%</td>


								<td><%=scheme.getStartDate()%></td>


								<td><%=scheme.getEndDate()%></td>


								<td>
									<%
									if ("ACTIVE".equalsIgnoreCase(scheme.getStatus())) {
									%> <span class="badge bg-success">
										ACTIVE </span> <%
 } else {
 %> <span class="badge bg-secondary">
										INACTIVE </span> <%
 }
 %>


								</td>


								<td><a
									href="<%=request.getContextPath()%>/schemes?action=edit&id=<%=scheme.getSchemeId()%>"
									class="btn btn-sm btn-primary"> Edit </a> <%
 if ("ACTIVE".equalsIgnoreCase(scheme.getStatus())) {
 %> <a
									href="<%=request.getContextPath()%>/schemes?action=deactivate&id=<%=scheme.getSchemeId()%>"
									class="btn btn-sm btn-danger"
									onclick="return confirm('Deactivate this scheme?');">

										Deactivate </a> <%
 } else {
 %> <a
									href="<%=request.getContextPath()%>/schemes?action=activate&id=<%=scheme.getSchemeId()%>"
									class="btn btn-sm btn-success"
									onclick="return confirm('Activate this scheme?');">

										Activate </a> <%
 }
 %></td>

							</tr>


							<%
							}

							} else {
							%>


							<tr>

								<td colspan="11" class="text-center text-muted">No schemes
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


	</div>


</body>

</html>