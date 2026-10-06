<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.User"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<title>User Management</title>

<meta name="viewport" content="width=device-width, initial-scale=1">

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body class="bg-light">

	<div class="container-fluid py-4">

		<!-- HEADER -->

		<div class="d-flex justify-content-between align-items-center mb-4">

			<div>
				<h2>User Management</h2>

				<p class="text-muted mb-0">Manage system users and their roles</p>
			</div>

			<div>

				<a href="users?action=add" class="btn btn-primary"> + Add User </a>

				<a href="dashboard.jsp" class="btn btn-secondary"> Dashboard </a>

			</div>

		</div>


		<!-- USER TABLE -->

		<div class="card shadow-sm">

			<div class="card-header bg-dark text-white">

				<h5 class="mb-0">System Users</h5>

			</div>

			<div class="card-body">

				<%
				List<User> users = (List<User>) request.getAttribute("users");
				%>


				<%
				if (users == null || users.isEmpty()) {
				%>

				<div class="alert alert-info">No users found.</div>

				<%
				} else {
				%>

				<div class="table-responsive">

					<table class="table table-bordered table-hover align-middle">

						<thead class="table-dark">

							<tr>

								<th>ID</th>
								<th>Username</th>
								<th>Name</th>
								<th>Email</th>
								<th>Phone</th>
								<th>Role</th>
								<th>Client ID</th>
								<th>Status</th>
								<th>Created At</th>
								<th>Actions</th>

							</tr>

						</thead>

						<tbody>

							<%
							for (User user : users) {
							%>

							<tr>

								<td><%=user.getUserId()%></td>

								<td><strong> <%=user.getUsername()%>
								</strong></td>

								<td><%=user.getFirstName() != null ? user.getFirstName() : ""%> <%=user.getLastName() != null ? user.getLastName() : ""%></td>

								<td><%=user.getEmail() != null ? user.getEmail() : "-"%></td>

								<td><%=user.getPhone() != null ? user.getPhone() : "-"%></td>

								<td><span class="badge bg-primary"> <%=user.getRoleName() != null ? user.getRoleName() : "-"%>

								</span></td>

								<td><%=user.getClientId() != null ? user.getClientId() : "-"%></td>

								<td>
									<%
									if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
									%> <span
									class="badge bg-success"> ACTIVE </span> <%
 } else {
 %> <span
									class="badge bg-danger"> INACTIVE </span> <%
 }
 %>

								</td>

								<td><%=user.getCreatedAt() != null ? user.getCreatedAt() : "-"%></td>

								<td>

									<div class="d-flex gap-1 flex-wrap">

										<!-- EDIT -->

										<a href="users?action=edit&id=<%=user.getUserId()%>"
											class="btn btn-sm btn-warning"> Edit </a>


										<!-- ACTIVATE -->

										<%
										if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
										%>

										<a href="users?action=activate&id=<%=user.getUserId()%>"
											class="btn btn-sm btn-success"
											onclick="return confirm('Activate this user?');">

											Activate </a>

										<%
										}
										%>


										<!-- DEACTIVATE -->

										<%
										if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
										%>

										<a href="users?action=deactivate&id=<%=user.getUserId()%>"
											class="btn btn-sm btn-danger"
											onclick="return confirm('Deactivate this user?');">

											Deactivate </a>

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


	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
		
	</script>

</body>
</html>