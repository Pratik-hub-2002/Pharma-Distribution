<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="com.pratik.pharma.model.Shipment"%>

<%
Shipment shipment = (Shipment) request.getAttribute("shipment");
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Shipment #<%=shipment.getShipmentId()%>
</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>


<body class="bg-light">


	<div class="container mt-5">


		<div class="card shadow">


			<div class="card-header bg-dark text-white">

				<h4 class="mb-0">

					Shipment #

					<%=shipment.getShipmentId()%>

				</h4>

			</div>


			<div class="card-body">


				<div class="row">


					<!-- ORDER -->

					<div class="col-md-6 mb-3">

						<strong> Order: </strong> <br> <a
							href="orders?action=view&id=<%=shipment.getOrderId()%>">

							Order #<%=shipment.getOrderId()%>

						</a>

					</div>


					<!-- CLIENT -->

					<div class="col-md-6 mb-3">

						<strong> Client: </strong> <br>

						<%=shipment.getClientName()%>

					</div>


					<!-- TRACKING -->

					<div class="col-md-6 mb-3">

						<strong> Tracking Number: </strong> <br>

						<%=shipment.getTrackingNumber() != null ? shipment.getTrackingNumber() : "N/A"%>

					</div>


					<!-- STATUS -->

					<div class="col-md-6 mb-3">

						<strong> Status: </strong> <br> <span
							class="badge bg-primary"> <%=shipment.getShipmentStatus()%>

						</span>

					</div>


					<!-- SHIPMENT DATE -->

					<div class="col-md-4 mb-3">

						<strong> Shipment Date: </strong> <br>

						<%=shipment.getShipmentDate() != null ? shipment.getShipmentDate() : "N/A"%>

					</div>


					<!-- DISPATCH DATE -->

					<div class="col-md-4 mb-3">

						<strong> Dispatch Date: </strong> <br>

						<%=shipment.getDispatchDate() != null ? shipment.getDispatchDate() : "N/A"%>

					</div>


					<!-- DELIVERY DATE -->

					<div class="col-md-4 mb-3">

						<strong> Delivery Date: </strong> <br>

						<%=shipment.getDeliveryDate() != null ? shipment.getDeliveryDate() : "N/A"%>

					</div>


					<!-- COLD CHAIN -->

					<div class="col-md-6 mb-3">

						<strong> Cold Chain: </strong> <br>


						<%
						if (shipment.isColdChainRequired()) {
						%>

						<span class="badge bg-danger"> REQUIRED </span>

						<%
						} else {
						%>

						<span class="badge bg-success"> NOT REQUIRED </span>

						<%
						}
						%>

					</div>


					<!-- TEMPERATURE -->

					<div class="col-md-6 mb-3">

						<strong> Temperature Status: </strong> <br>

						<%=shipment.getTemperatureStatus() != null ? shipment.getTemperatureStatus() : "N/A"%>

					</div>


					<!-- CREATED BY -->

					<div class="col-md-6 mb-3">

						<strong> Created By: </strong> <br>

						<%=shipment.getCreatedByName()%>

					</div>


					<!-- CREATED AT -->

					<div class="col-md-6 mb-3">

						<strong> Created At: </strong> <br>

						<%=shipment.getCreatedAt()%>

					</div>


				</div>


				<hr>


				<!-- ACTIONS -->

				<div class="d-flex gap-2 flex-wrap">


					<%
					String currentStatus = shipment.getShipmentStatus();
					%>


					<%
					if ("PENDING".equals(currentStatus) || "PACKED".equals(currentStatus)) {
					%>


					<a
						href="shipments?action=dispatch&id=<%=shipment.getShipmentId()%>"
						class="btn btn-primary"> Mark Dispatched </a>


					<%
					}
					%>


					<%
					if ("DISPATCHED".equals(currentStatus) || "IN_TRANSIT".equals(currentStatus)) {
					%>


					<a
						href="shipments?action=deliver&id=<%=shipment.getShipmentId()%>"
						class="btn btn-success"> Mark Delivered </a>


					<%
					}
					%>


					<a href="shipments?action=list" class="btn btn-secondary"> Back
						to Shipments </a> <a
						href="orders?action=view&id=<%=shipment.getOrderId()%>"
						class="btn btn-outline-primary"> View Order </a>


				</div>


				<!-- TEMPERATURE UPDATE -->

				<div class="card mt-4">


					<div class="card-header">Update Temperature Status</div>


					<div class="card-body">


						<form action="shipments" method="post" class="row g-2">


							<input type="hidden" name="action" value="temperature"> <input
								type="hidden" name="id" value="<%=shipment.getShipmentId()%>">


							<div class="col-md-8">


								<select name="temperatureStatus" class="form-select">


									<option value="NORMAL">NORMAL</option>

									<option value="WITHIN_RANGE">WITHIN_RANGE</option>

									<option value="OUT_OF_RANGE">OUT_OF_RANGE</option>

									<option value="NOT_REQUIRED">NOT_REQUIRED</option>


								</select>


							</div>


							<div class="col-md-4">


								<button type="submit" class="btn btn-warning w-100">

									Update Temperature</button>


							</div>


						</form>


					</div>


				</div>


			</div>

		</div>


	</div>


</body>

</html>