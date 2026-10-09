<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="com.pratik.pharma.model.Order" %>

<%
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Create Shipment</title>

    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">
</head>

<body class="bg-light">

<div class="container mt-5 mb-5">

    <div class="card shadow">

        <div class="card-header bg-primary text-white">
            <h4 class="mb-0">Create Shipment</h4>
        </div>

        <div class="card-body">

            <% if (error != null) { %>
                <div class="alert alert-danger" role="alert">
                    <%= error %>
                </div>
            <% } %>

            <div class="alert alert-info">
                <strong>Credit order:</strong>
                An approved order with an invoice can be shipped before payment.
                Payment can be collected within the customer's approved credit period.
            </div>

            <% if (orders != null && !orders.isEmpty()) { %>

                <form action="shipments" method="post">

                    <input type="hidden" name="action" value="create">

                    <!-- ORDER -->
                    <div class="mb-3">
                        <label for="orderId" class="form-label">
                            Order
                        </label>

                        <select id="orderId" name="orderId"
                                class="form-select" required>

                            <option value="">-- Select Order --</option>

                            <% for (Order order : orders) { %>
                                <option value="<%= order.getOrderId() %>">
                                    Order #<%= order.getOrderId() %>
                                    - <%= order.getClientName() %>
                                    - ₹<%= order.getTotalAmount() %>
                                    - <%= order.getOrderStatus() %>
                                    - Payment: <%= order.getCreditStatus() %>
                                </option>
                            <% } %>

                        </select>
                    </div>

                    <!-- TRACKING NUMBER -->
                    <div class="mb-3">
                        <label for="trackingNumber" class="form-label">
                            Tracking Number
                        </label>

                        <input type="text"
                               id="trackingNumber"
                               name="trackingNumber"
                               class="form-control"
                               maxlength="100"
                               placeholder="Enter courier / tracking number">
                    </div>

                    <!-- COLD CHAIN -->
                    <div class="mb-3">
                        <label for="coldChainRequired" class="form-label">
                            Cold Chain Required
                        </label>

                        <select id="coldChainRequired"
                                name="coldChainRequired"
                                class="form-select" required>

                            <option value="false">No</option>
                            <option value="true">Yes</option>

                        </select>
                    </div>

                    <!-- TEMPERATURE -->
                    <div class="mb-3">
                        <label for="temperatureStatus" class="form-label">
                            Temperature Status
                        </label>

                        <select id="temperatureStatus"
                                name="temperatureStatus"
                                class="form-select" required>

                            <option value="NOT_REQUIRED">NOT_REQUIRED</option>
                            <option value="NORMAL">NORMAL</option>
                            <option value="WITHIN_RANGE">WITHIN_RANGE</option>
                            <option value="OUT_OF_RANGE">OUT_OF_RANGE</option>

                        </select>

                        <div class="form-text">
                            For cold-chain products, record the actual temperature
                            compliance status. Do not select a compliant status
                            unless it has been verified.
                        </div>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-primary">
                            Create Shipment
                        </button>

                        <a href="shipments?action=list"
                           class="btn btn-secondary">
                            Cancel
                        </a>
                    </div>

                </form>

            <% } else { %>

                <div class="alert alert-warning mb-3">
                    No eligible orders are available for shipment.
                    Confirm that the order is approved, an invoice exists,
                    and a shipment has not already been created.
                </div>

                <a href="dispatch-orders" class="btn btn-outline-primary">
                    View Dispatcher Orders
                </a>

                <a href="shipments?action=list" class="btn btn-secondary">
                    Back to Shipments
                </a>

            <% } %>

        </div>
    </div>
</div>

</body>
</html>
