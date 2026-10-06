<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.User"%>
<%@ page import="com.pratik.pharma.model.Role"%>
<%@ page import="com.pratik.pharma.model.Client"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<title>User Form - Pharma Distribution</title>

<meta name="viewport" content="width=device-width, initial-scale=1">

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body class="bg-light">

	<div class="container py-5">

		<%
		User user = (User) request.getAttribute("user");

		boolean editMode = (user != null);

		List<Role> roles = (List<Role>) request.getAttribute("roles");

		List<Client> clients = (List<Client>) request.getAttribute("clients");
		%>


		<!-- =========================================================
         HEADER
         ========================================================= -->

		<div class="d-flex justify-content-between align-items-center mb-4">

			<div>

				<h2>
					<%=editMode ? "Edit User" : "Add New User"%>
				</h2>

				<p class="text-muted mb-0">
					<%=editMode ? "Update user account details" : "Create a new system user"%>
				</p>

			</div>

			<a href="users?action=list" class="btn btn-secondary"> Back to
				Users </a>

		</div>


		<!-- =========================================================
         FORM
         ========================================================= -->

		<div class="card shadow-sm">

			<div class="card-header bg-dark text-white">

				<h5 class="mb-0">

					<%=editMode ? "Update User" : "Create User"%>

				</h5>

			</div>


			<div class="card-body">

				<form action="users" method="post">

					<!-- ACTION -->

					<input type="hidden" name="action"
						value="<%=editMode ? "update" : "add"%>">


					<!-- USER ID FOR EDIT -->

					<%
					if (editMode) {
					%>

					<input type="hidden" name="userId" value="<%=user.getUserId()%>">

					<%
					}
					%>


					<div class="row g-3">


						<!-- USERNAME -->

						<div class="col-md-6">

							<label class="form-label"> Username </label> <input type="text"
								name="username" class="form-control" required
								value="<%=editMode ? user.getUsername() : ""%>">

						</div>


						<!-- PASSWORD -->

						<div class="col-md-6">

							<label class="form-label"> Password </label> <input
								type="password" name="passwordHash" class="form-control"
								<%=!editMode ? "required" : ""%>
								placeholder="<%=editMode ? "Leave blank to keep existing password" : "Enter password"%>">

							<%
							if (editMode) {
							%>

							<small class="text-muted"> Leave blank if you do not want
								to change the password. </small>

							<%
							} else {
							%>

							<small class="text-muted"> Enter the password for the new
								user. </small>

							<%
							}
							%>

						</div>


						<!-- FIRST NAME -->

						<div class="col-md-6">

							<label class="form-label"> First Name </label> <input type="text"
								name="firstName" class="form-control" required
								value="<%=editMode ? user.getFirstName() : ""%>">

						</div>


						<!-- LAST NAME -->

						<div class="col-md-6">

							<label class="form-label"> Last Name </label> <input type="text"
								name="lastName" class="form-control" required
								value="<%=editMode ? user.getLastName() : ""%>">

						</div>


						<!-- EMAIL -->

						<div class="col-md-6">

							<label class="form-label"> Email </label> <input type="email"
								name="email" class="form-control"
								value="<%=editMode && user.getEmail() != null ? user.getEmail() : ""%>">

						</div>


						<!-- PHONE -->

						<div class="col-md-6">

							<label class="form-label"> Phone </label> <input type="text"
								name="phone" class="form-control" maxlength="15"
								value="<%=editMode && user.getPhone() != null ? user.getPhone() : ""%>">

						</div>


						<!-- ROLE -->

						<div class="col-md-6">

							<label class="form-label"> Role </label> <select name="roleId"
								class="form-select" required>

								<option value="">-- Select Role --</option>

								<%
								if (roles != null) {

									for (Role role : roles) {
								%>

								<option value="<%=role.getRoleId()%>"
									<%=editMode && user.getRoleId() == role.getRoleId() ? "selected" : ""%>>

									<%=role.getRoleName()%>

								</option>

								<%
								}
								}
								%>

							</select>

						</div>


						<!-- CLIENT -->

						<div class="col-md-6">

							<label class="form-label"> Client </label> <select
								name="clientId" class="form-select">

								<option value="">-- No Client / Internal User --</option>

								<%
								if (clients != null) {

									for (Client client : clients) {
								%>

								<option value="<%=client.getClientId()%>"
									<%=editMode && user.getClientId() != null && user.getClientId() == client.getClientId() ? "selected" : ""%>>

									<%=client.getClientName()%>

								</option>

								<%
								}
								}
								%>

							</select> <small class="text-muted"> Select a client only when
								this user belongs to a client account. </small>

						</div>


					</div>


					<!-- =================================================
                     BUTTONS
                     ================================================= -->

					<div class="mt-4">

						<button type="submit" class="btn btn-primary">

							<%=editMode ? "Update User" : "Create User"%>

						</button>


						<a href="users?action=list" class="btn btn-secondary"> Cancel

						</a>

					</div>

				</form>

			</div>

		</div>

	</div>


	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
		
	</script>

</body>
</html>