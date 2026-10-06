<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Client"%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Clients - Pharma Distribution</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body class="bg-light">

	<div class="container-fluid mt-4">

		<div class="d-flex justify-content-between align-items-center mb-3">

			<h2>Client Management</h2>

			<a href="<%=request.getContextPath()%>/client-form.jsp"
				class="btn btn-primary"> + Add Client </a> <a
				href="<%=request.getContextPath()%>/dashboard.jsp"
				class="btn btn-secondary"> Dashboard </a>

		</div>


		<div class="card shadow">

			<div class="card-header bg-primary text-white">

				<h5 class="mb-0">Registered Clients</h5>

			</div>


			<div class="card-body">

				<div class="table-responsive">

					<table class="table table-bordered table-striped table-hover">

						<thead class="table-dark">

							<tr>
								<th>ID</th>
								<th>Client Name</th>
								<th>Type</th>
								<th>Owner</th>
								<th>Contact Person</th>
								<th>Phone</th>
								<th>City</th>
								<th>State</th>
								<th>GST</th>
								<th>Credit Limit</th>
								<th>Credit Days</th>
								<th>Status</th>
								<th>Verification</th>
								<th>Action</th>
							</tr>

						</thead>


						<tbody>

							<%
							List<Client> clients = (List<Client>) request.getAttribute("clients");

							if (clients != null && !clients.isEmpty()) {

								for (Client client : clients) {
							%>

							<tr>

								<td><%=client.getClientId()%></td>

								<td><%=client.getClientName()%></td>

								<td><%=client.getClientType()%></td>

								<td><%=client.getOwnerName()%></td>

								<td><%=client.getContactPerson()%></td>

								<td><%=client.getPhone()%></td>

								<td><%=client.getCity()%></td>

								<td><%=client.getState()%></td>

								<td><%=client.getGstNumber()%></td>

								<td>₹ <%=client.getCreditLimit()%>
								</td>

								<td><%=client.getCreditPeriodDays()%></td>

								<td><%=client.getStatus()%></td>

								<td><%=client.getVerificationStatus()%></td>
								<td><a
									href="<%=request.getContextPath()%>/clients?action=edit&id=<%=client.getClientId()%>"
									class="btn btn-sm btn-warning"> Edit </a> <%
 if ("ACTIVE".equalsIgnoreCase(client.getStatus())) {
 %> <a
									href="<%=request.getContextPath()%>/clients?action=status&id=<%=client.getClientId()%>"
									class="btn btn-sm btn-danger"> Deactivate </a> <%
 } else {
 %> <a
									href="<%=request.getContextPath()%>/clients?action=status&id=<%=client.getClientId()%>"
									class="btn btn-sm btn-success"> Activate </a> <%
 }
 %> <a
									href="<%=request.getContextPath()%>/client-documents?clientId=<%=client.getClientId()%>"
									class="btn btn-sm btn-info"> Documents </a></td>

							</tr>

							<%
							}

							} else {
							%>

							<tr>

								<td colspan="14" class="text-center">No clients found.</td>

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