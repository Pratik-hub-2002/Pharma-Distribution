<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.pratik.pharma.model.Order"%>

<%
List<Order> orders = (List<Order>) request.getAttribute("orders");

String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Create Shipment</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>


<body class="bg-light">


	<div class="container mt-5">


		<div class="card shadow">


			<div class="card-header bg-primary text-white">

				<h4 class="mb-0">Create Shipment</h4>

			</div>


			<div class="card-body">


				<%
				if (error != null) {
				%>

				<div class="alert alert-danger">

					<%=error%>

				</div>

				<%
				}
				%>


				<form action="shipments" method="post">


					<input type="hidden" name="action" value="create">


					<!-- ORDER -->

					<div class="mb-3">

						<label class="form-label"> Order </label> <select name="orderId"
							class="form-select" required>


							<option value="">-- Select Order --</option>


							<%
							if (orders != null) {

								for (Order order : orders) {
							%>


							<option value="<%=order.getOrderId()%>">Order #<%=order.getOrderId()%>

								-
								<%=order.getClientName()%> - ₹<%=order.getTotalAmount()%> -
								<%=order.getOrderStatus()%>

							</option>


							<%
							}
							}
							%>


						</select>

					</div>


					<!-- TRACKING NUMBER -->

					<div class="mb-3">

						<label class="form-label"> Tracking Number </label> <input
							type="text" name="trackingNumber" class="form-control"
							placeholder="Enter courier / tracking number">

					</div>


					<!-- COLD CHAIN -->

					<div class="mb-3">

						<label class="form-label"> Cold Chain Required </label> <select
							name="coldChainRequired" class="form-select">


							<option value="false">No</option>


							<option value="true">Yes</option>


						</select>

					</div>


					<!-- TEMPERATURE -->

					<div class="mb-3">

						<label class="form-label"> Temperature Status </label> <select
							name="temperatureStatus" class="form-select">


							<option value="NORMAL">NORMAL</option>


							<option value="WITHIN_RANGE">WITHIN_RANGE</option>


							<option value="OUT_OF_RANGE">OUT_OF_RANGE</option>


							<option value="NOT_REQUIRED">NOT_REQUIRED</option>


						</select>

					</div>


					<div class="d-flex gap-2">


						<button type="submit" class="btn btn-primary">Create
							Shipment</button>


						<a href="shipments?action=list" class="btn btn-secondary">

							Cancel </a>


					</div>


				</form>


			</div>


		</div>


	</div>


</body>

</html>