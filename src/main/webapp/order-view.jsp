<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.pratik.pharma.model.Order"%>
<%@ page import="com.pratik.pharma.model.OrderItem"%>

<%
String contextPath = request.getContextPath();

Order order = (Order) request.getAttribute("order");
List<OrderItem> items = (List<OrderItem>) request.getAttribute("items");
%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Order Details</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<style>
body {
	background-color: #f5f7fa;
}

.card {
	border: none;
	border-radius: 10px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
	margin-bottom: 20px;
}

.card-header {
	font-weight: 600;
}

.label {
	color: #6c757d;
	font-size: 13px;
}

.value {
	font-weight: 500;
}

.total {
	font-size: 22px;
	font-weight: bold;
}

@media print {
	.no-print {
		display: none !important;
	}
	body {
		background: white;
	}
}
</style>

</head>


<body>


	<!-- =====================================================
     HEADER
     ===================================================== -->

	<div class="container mt-4">


		<div class="d-flex justify-content-between align-items-center mb-4">

			<div>

				<h2>Order Details</h2>

				<p class="text-muted mb-0">

					<%
					if (order != null) {
					%>

					Order #<%=order.getOrderId()%>

					<%
					} else {
					%>

					Order Not Found

					<%
					}
					%>

				</p>

			</div>


			<div class="no-print">

				<a href="<%=contextPath%>/orders" class="btn btn-secondary">

					Back to Orders </a>


				<%
				if (order != null) {
				%>

				<button type="button" class="btn btn-primary"
					onclick="window.print()">Print</button>

				<%
				}
				%>

			</div>

		</div>



		<%
		if (order == null) {
		%>


		<!-- =================================================
             ORDER NOT FOUND
             ================================================= -->

		<div class="alert alert-danger">

			<h5>Order Not Found</h5>

			<p class="mb-0">The requested order could not be found.</p>

		</div>


		<%
		} else {
		%>


		<!-- =================================================
             ORDER INFORMATION
             ================================================= -->

		<div class="card">

			<div class="card-header bg-primary text-white">Order
				Information</div>


			<div class="card-body">


				<div class="row g-4">


					<!-- Order ID -->

					<div class="col-md-3">

						<div class="label">Order ID</div>

						<div class="value">

							#<%=order.getOrderId()%>

						</div>

					</div>


					<!-- Client -->

					<div class="col-md-3">

						<div class="label">Client</div>

						<div class="value">

							<%
							if (order.getClientName() != null) {
							%>

							<%=order.getClientName()%>

							<%
							} else {
							%>

							Client #<%=order.getClientId()%>

							<%
							}
							%>

						</div>

					</div>


					<!-- Order Date -->

					<div class="col-md-3">

						<div class="label">Order Date</div>

						<div class="value">

							<%=order.getOrderDate() != null ? order.getOrderDate() : "-"%>

						</div>

					</div>


					<!-- Status -->

					<div class="col-md-3">

						<div class="label">Order Status</div>

						<div class="value">

							<span class="badge bg-warning text-dark"> <%=order.getOrderStatus() != null ? order.getOrderStatus() : "-"%>

							</span>

						</div>

					</div>


				</div>


				<hr>


				<div class="row g-4">


					<!-- Credit Status -->

					<div class="col-md-3">

						<div class="label">Credit Status</div>

						<div class="value">

							<%=order.getCreditStatus() != null ? order.getCreditStatus() : "-"%>

						</div>

					</div>


					<!-- Created By -->

					<div class="col-md-3">

						<div class="label">Created By</div>

						<div class="value">

							<%
							if (order.getCreatedByName() != null) {
							%>

							<%=order.getCreatedByName()%>

							<%
							} else {
							%>

							User #<%=order.getCreatedBy()%>

							<%
							}
							%>

						</div>

					</div>


					<!-- Approved By -->

					<div class="col-md-3">

						<div class="label">Approved By</div>

						<div class="value">

							<%
							if (order.getApprovedBy() != null) {
							%>

							User #<%=order.getApprovedBy()%>

							<%
							} else {
							%>

							Not Approved

							<%
							}
							%>

						</div>

					</div>


					<!-- Created At -->

					<div class="col-md-3">

						<div class="label">Created At</div>

						<div class="value">

							<%=order.getCreatedAt() != null ? order.getCreatedAt() : "-"%>

						</div>

					</div>


				</div>

			</div>

		</div>



		<!-- =================================================
             ORDER ITEMS
             ================================================= -->

		<div class="card">

			<div class="card-header">Order Items</div>


			<div class="card-body p-0">


				<%
				if (items == null || items.isEmpty()) {
				%>


				<div class="alert alert-info m-3">No items found for this
					order.</div>


				<%
				} else {
				%>


				<div class="table-responsive">

					<table class="table table-bordered table-hover mb-0">

						<thead class="table-light">

							<tr>

								<th>#</th>

								<th>Product</th>

								<th>Product ID</th>

								<th class="text-center">Ordered Qty</th>

								<th class="text-center">Free Qty</th>

								<th class="text-center">Total Qty</th>

								<th class="text-end">Unit Price</th>

								<th class="text-end">Discount</th>

								<th class="text-end">Tax</th>

								<th class="text-end">Line Total</th>

							</tr>

						</thead>


						<tbody>


							<%
							int count = 1;

							for (OrderItem item : items) {

								String productName = item.getProductName();

								if (productName == null) {
									productName = "Product #" + item.getProductId();
								}

								BigDecimal unitPrice = item.getUnitPrice();

								if (unitPrice == null) {
									unitPrice = BigDecimal.ZERO;
								}

								BigDecimal discount = item.getDiscountAmount();

								if (discount == null) {
									discount = BigDecimal.ZERO;
								}

								BigDecimal tax = item.getTaxAmount();

								if (tax == null) {
									tax = BigDecimal.ZERO;
								}

								BigDecimal lineTotal = item.getLineTotal();

								if (lineTotal == null) {
									lineTotal = BigDecimal.ZERO;
								}
							%>


							<tr>


								<td><%=count++%></td>


								<td><strong> <%=productName%>
								</strong></td>


								<td>#<%=item.getProductId()%>

								</td>


								<td class="text-center"><%=item.getOrderedQuantity()%></td>


								<td class="text-center">
									<%
									if (item.getFreeQuantity() > 0) {
									%> <span class="badge bg-success"> <%=item.getFreeQuantity()%>
										FREE

								</span> <%
 } else {
 %> 0 <%
 }
 %>

								</td>


								<td class="text-center"><strong> <%=item.getTotalQuantity()%>

								</strong></td>


								<td class="text-end">₹ <%=unitPrice%>

								</td>


								<td class="text-end">₹ <%=discount%>

								</td>


								<td class="text-end">₹ <%=tax%>

								</td>


								<td class="text-end"><strong> ₹ <%=lineTotal%>

								</strong></td>


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



		<!-- =================================================
             ORDER SUMMARY
             ================================================= -->

		<div class="row justify-content-end">


			<div class="col-md-5">


				<div class="card">


					<div class="card-header">Order Summary</div>


					<div class="card-body">


						<!-- Subtotal -->

						<div class="d-flex justify-content-between mb-3">

							<span> Subtotal </span> <strong> ₹ <%=order.getSubtotal() != null ? order.getSubtotal() : BigDecimal.ZERO%>

							</strong>

						</div>


						<!-- Discount -->

						<div class="d-flex justify-content-between mb-3">

							<span> Discount </span> <span class="text-success"> - ₹ <%=order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO%>

							</span>

						</div>


						<!-- Tax -->

						<div class="d-flex justify-content-between mb-3">

							<span> GST / Tax </span> <span> ₹ <%=order.getTaxAmount() != null ? order.getTaxAmount() : BigDecimal.ZERO%>

							</span>

						</div>


						<hr>


						<!-- Total -->

						<div class="d-flex justify-content-between">

							<span class="total"> Grand Total </span> <span class="total">

								₹ <%=order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO%>

							</span>

						</div>


					</div>

				</div>

			</div>

		</div>


		<%
		}
		%>


	</div>


	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
		
	</script>


</body>

</html>