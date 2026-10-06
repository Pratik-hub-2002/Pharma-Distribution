<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Shipment"%>

<%
List<Shipment> shipments = (List<Shipment>) request.getAttribute("shipments");
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Shipment Management</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>


<body class="bg-light">


	<div class="container-fluid mt-4">


		<div class="d-flex justify-content-between align-items-center mb-4">

			<h2>Shipment / Dispatch Management</h2>

			<a href="shipments?action=add" class="btn btn-primary"> + Create
				Shipment </a>

		</div>


		<%
		String error = (String) request.getAttribute("error");

		if (error != null) {
		%>

		<div class="alert alert-danger">
			<%=error%>
		</div>

		<%
		}
		%>


		<div class="card shadow-sm">


			<div class="card-body">


				<div class="table-responsive">


					<table class="table table-bordered table-hover align-middle">


						<thead class="table-dark">

							<tr>

								<th>ID</th>

								<th>Order</th>

								<th>Client</th>

								<th>Tracking Number</th>

								<th>Shipment Date</th>

								<th>Dispatch Date</th>

								<th>Delivery Date</th>

								<th>Status</th>

								<th>Cold Chain</th>

								<th>Temperature</th>

								<th>Created By</th>

								<th>Action</th>

							</tr>

						</thead>


						<tbody>


							<%
							if (shipments != null && !shipments.isEmpty()) {

								for (Shipment shipment : shipments) {
							%>


							<tr>


								<td><%=shipment.getShipmentId()%></td>


								<td><a
									href="orders?action=view&id=<%=shipment.getOrderId()%>"> #<%=shipment.getOrderId()%>

								</a></td>


								<td><%=shipment.getClientName()%></td>


								<td><%=shipment.getTrackingNumber() != null ? shipment.getTrackingNumber() : "N/A"%></td>


								<td><%=shipment.getShipmentDate() != null ? shipment.getShipmentDate() : "N/A"%></td>


								<td><%=shipment.getDispatchDate() != null ? shipment.getDispatchDate() : "N/A"%></td>


								<td><%=shipment.getDeliveryDate() != null ? shipment.getDeliveryDate() : "N/A"%></td>


								<td>
									<%
									String status = shipment.getShipmentStatus();

									String badgeClass = "bg-secondary";

									if ("PENDING".equals(status)) {
										badgeClass = "bg-warning text-dark";
									} else if ("PACKED".equals(status)) {
										badgeClass = "bg-info text-dark";
									} else if ("DISPATCHED".equals(status)) {
										badgeClass = "bg-primary";
									} else if ("IN_TRANSIT".equals(status)) {
										badgeClass = "bg-dark";
									} else if ("DELIVERED".equals(status)) {
										badgeClass = "bg-success";
									}
									%> <span class="badge <%=badgeClass%>"> <%=status%>

								</span>


								</td>


								<td>
									<%
									if (shipment.isColdChainRequired()) {
									%> <span class="badge bg-danger"> REQUIRED </span> <%
 } else {
 %> <span class="badge bg-success"> NO </span> <%
 }
 %>

								</td>


								<td><%=shipment.getTemperatureStatus() != null ? shipment.getTemperatureStatus() : "N/A"%></td>


								<td><%=shipment.getCreatedByName()%></td>


								<td><a
									href="shipments?action=view&id=<%=shipment.getShipmentId()%>"
									class="btn btn-sm btn-outline-primary"> View </a></td>


							</tr>


							<%
							}

							} else {
							%>


							<tr>

								<td colspan="12" class="text-center text-muted py-4">No
									shipments found.</td>

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