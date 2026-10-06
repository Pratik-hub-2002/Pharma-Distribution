<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.ClientDocument"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Client Documents</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body>

	<div class="container mt-4">

		<!-- Header -->

		<div class="d-flex justify-content-between align-items-center mb-4">

			<h2>Client Documents</h2>

			<a href="<%=request.getContextPath()%>/clients"
				class="btn btn-secondary"> Back to Clients </a>

		</div>


		<%
		List<ClientDocument> documents = (List<ClientDocument>) request.getAttribute("documents");

		Integer clientId = (Integer) request.getAttribute("clientId");
		%>


		<!-- Client Information -->

		<div class="card mb-4">

			<div class="card-body">

				<h5 class="card-title">

					Client ID: <strong><%=clientId%></strong>

				</h5>

			</div>

		</div>


		<!-- Documents Table -->

		<div class="card">

			<div class="card-body">

				<h5 class="card-title mb-3">Documents</h5>


				<div class="table-responsive">

					<table class="table table-bordered table-hover align-middle">

						<thead class="table-dark">

							<tr>

								<th>ID</th>

								<th>Document Type</th>

								<th>Document Number</th>

								<th>Issue Date</th>

								<th>Expiry Date</th>

								<th>Status</th>

								<th>Action</th>

							</tr>

						</thead>


						<tbody>

							<%
							if (documents != null && !documents.isEmpty()) {

								for (ClientDocument document : documents) {
							%>

							<tr>

								<td><%=document.getDocumentId()%></td>


								<td><%=document.getDocumentType()%></td>


								<td><%=document.getDocumentNumber()%></td>


								<td><%=document.getIssueDate()%></td>


								<td><%=document.getExpiryDate()%></td>


								<td>
									<%
									String status = document.getDocumentStatus();

									if ("VERIFIED".equalsIgnoreCase(status)) {
									%> <span class="badge bg-success"> VERIFIED </span> <%
 } else if ("REJECTED".equalsIgnoreCase(status)) {
 %> <span class="badge bg-danger"> REJECTED </span> <%
 } else {
 %> <span class="badge bg-warning text-dark"> PENDING </span> <%
 }
 %>

								</td>


								<td>
									<%
									if ("PENDING".equalsIgnoreCase(status)) {
									%> <!-- VERIFY -->

									<form action="<%=request.getContextPath()%>/client-documents"
										method="post" style="display: inline;">

										<input type="hidden" name="action" value="verify"> <input
											type="hidden" name="documentId"
											value="<%=document.getDocumentId()%>"> <input
											type="hidden" name="clientId" value="<%=clientId%>">

										<button type="submit" class="btn btn-sm btn-success">

											Verify</button>

									</form> <!-- REJECT -->

									<form action="<%=request.getContextPath()%>/client-documents"
										method="post" style="display: inline;">

										<input type="hidden" name="action" value="reject"> <input
											type="hidden" name="documentId"
											value="<%=document.getDocumentId()%>"> <input
											type="hidden" name="clientId" value="<%=clientId%>">

										<button type="submit" class="btn btn-sm btn-danger">

											Reject</button>

									</form> <%
 } else {
 %> <span class="text-muted"> No Action </span> <%
 }
 %>

								</td>

							</tr>


							<%
							}

							} else {
							%>

							<tr>

								<td colspan="7" class="text-center text-danger">No
									documents found.</td>

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