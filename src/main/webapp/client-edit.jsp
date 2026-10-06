<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="com.pratik.pharma.model.Client" %>

<%
    Client client =
            (Client) request.getAttribute("client");
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Edit Client - Pharma Distribution</title>

    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">

</head>

<body class="bg-light">

<div class="container mt-4">

    <div class="d-flex justify-content-between align-items-center mb-4">

        <h2>Edit Client</h2>

        <a href="<%= request.getContextPath() %>/clients"
           class="btn btn-secondary">
            Back to Clients
        </a>

    </div>


    <div class="card shadow">

        <div class="card-header bg-warning">

            <h5 class="mb-0">
                Edit Client Information
            </h5>

        </div>


        <div class="card-body">

            <% if (request.getAttribute("error") != null) { %>

                <div class="alert alert-danger">
                    <%= request.getAttribute("error") %>
                </div>

            <% } %>


            <form
                action="<%= request.getContextPath() %>/clients"
                method="post">

                <!-- Tells Servlet this is UPDATE -->
                <input
                    type="hidden"
                    name="action"
                    value="update">

                <!-- Client ID -->
                <input
                    type="hidden"
                    name="clientId"
                    value="<%= client.getClientId() %>">


                <!-- Client Name / Type -->

                <div class="row mb-3">

                    <div class="col-md-6">

                        <label class="form-label">
                            Client Name
                        </label>

                        <input
                            type="text"
                            name="clientName"
                            class="form-control"
                            value="<%= client.getClientName() %>"
                            required>

                    </div>


                    <div class="col-md-6">

                        <label class="form-label">
                            Client Type
                        </label>

                        <select
                            name="clientType"
                            class="form-select"
                            required>

                            <option value="MEDICAL_STORE"
                                <%= "MEDICAL_STORE".equals(client.getClientType()) ? "selected" : "" %>>
                                Medical Store
                            </option>

                            <option value="HOSPITAL"
                                <%= "HOSPITAL".equals(client.getClientType()) ? "selected" : "" %>>
                                Hospital
                            </option>

                            <option value="IVF_CLINIC"
                                <%= "IVF_CLINIC".equals(client.getClientType()) ? "selected" : "" %>>
                                IVF Clinic
                            </option>

                            <option value="CLINIC"
                                <%= "CLINIC".equals(client.getClientType()) ? "selected" : "" %>>
                                Clinic
                            </option>

                            <option value="DISTRIBUTOR"
                                <%= "DISTRIBUTOR".equals(client.getClientType()) ? "selected" : "" %>>
                                Distributor
                            </option>

                        </select>

                    </div>

                </div>


                <!-- Owner / Contact -->

                <div class="row mb-3">

                    <div class="col-md-6">

                        <label class="form-label">
                            Owner Name
                        </label>

                        <input
                            type="text"
                            name="ownerName"
                            class="form-control"
                            value="<%= client.getOwnerName() %>"
                            required>

                    </div>


                    <div class="col-md-6">

                        <label class="form-label">
                            Contact Person
                        </label>

                        <input
                            type="text"
                            name="contactPerson"
                            class="form-control"
                            value="<%= client.getContactPerson() != null
                                    ? client.getContactPerson()
                                    : "" %>">

                    </div>

                </div>


                <!-- Email / Phone -->

                <div class="row mb-3">

                    <div class="col-md-6">

                        <label class="form-label">
                            Email
                        </label>

                        <input
                            type="email"
                            name="email"
                            class="form-control"
                            value="<%= client.getEmail() != null
                                    ? client.getEmail()
                                    : "" %>">

                    </div>


                    <div class="col-md-6">

                        <label class="form-label">
                            Phone
                        </label>

                        <input
                            type="text"
                            name="phone"
                            class="form-control"
                            value="<%= client.getPhone() != null
                                    ? client.getPhone()
                                    : "" %>">

                    </div>

                </div>


                <!-- Address -->

                <div class="mb-3">

                    <label class="form-label">
                        Address
                    </label>

                    <textarea
                        name="address"
                        class="form-control"
                        rows="3"><%= client.getAddress() != null
                                ? client.getAddress()
                                : "" %></textarea>

                </div>


                <!-- Location -->

                <div class="row mb-3">

                    <div class="col-md-4">

                        <label class="form-label">
                            City
                        </label>

                        <input
                            type="text"
                            name="city"
                            class="form-control"
                            value="<%= client.getCity() != null
                                    ? client.getCity()
                                    : "" %>">

                    </div>


                    <div class="col-md-4">

                        <label class="form-label">
                            State
                        </label>

                        <input
                            type="text"
                            name="state"
                            class="form-control"
                            value="<%= client.getState() != null
                                    ? client.getState()
                                    : "" %>">

                    </div>


                    <div class="col-md-4">

                        <label class="form-label">
                            Pincode
                        </label>

                        <input
                            type="text"
                            name="pincode"
                            class="form-control"
                            value="<%= client.getPincode() != null
                                    ? client.getPincode()
                                    : "" %>">

                    </div>

                </div>


                <!-- GST -->

                <div class="mb-3">

                    <label class="form-label">
                        GST Number
                    </label>

                    <input
                        type="text"
                        name="gstNumber"
                        class="form-control"
                        value="<%= client.getGstNumber() != null
                                ? client.getGstNumber()
                                : "" %>">

                </div>


                <!-- Credit -->

                <div class="row mb-3">

                    <div class="col-md-6">

                        <label class="form-label">
                            Credit Limit
                        </label>

                        <input
                            type="number"
                            name="creditLimit"
                            class="form-control"
                            min="0"
                            step="0.01"
                            value="<%= client.getCreditLimit() %>">

                    </div>


                    <div class="col-md-6">

                        <label class="form-label">
                            Credit Period (Days)
                        </label>

                        <input
                            type="number"
                            name="creditPeriodDays"
                            class="form-control"
                            min="0"
                            value="<%= client.getCreditPeriodDays() %>">

                    </div>

                </div>


                <!-- Status -->

                <div class="row mb-4">

                    <div class="col-md-6">

                        <label class="form-label">
                            Status
                        </label>

                        <select
                            name="status"
                            class="form-select">

                            <option value="ACTIVE"
                                <%= "ACTIVE".equals(client.getStatus())
                                    ? "selected" : "" %>>
                                ACTIVE
                            </option>

                            <option value="INACTIVE"
                                <%= "INACTIVE".equals(client.getStatus())
                                    ? "selected" : "" %>>
                                INACTIVE
                            </option>

                        </select>

                    </div>


                    <div class="col-md-6">

                        <label class="form-label">
                            Verification Status
                        </label>

                        <select
                            name="verificationStatus"
                            class="form-select">

                            <option value="PENDING"
                                <%= "PENDING".equals(
                                        client.getVerificationStatus())
                                    ? "selected" : "" %>>
                                PENDING
                            </option>

                            <option value="VERIFIED"
                                <%= "VERIFIED".equals(
                                        client.getVerificationStatus())
                                    ? "selected" : "" %>>
                                VERIFIED
                            </option>

                            <option value="REJECTED"
                                <%= "REJECTED".equals(
                                        client.getVerificationStatus())
                                    ? "selected" : "" %>>
                                REJECTED
                            </option>

                        </select>

                    </div>

                </div>


                <!-- Buttons -->

                <div class="text-end">

                    <a
                        href="<%= request.getContextPath() %>/clients"
                        class="btn btn-secondary me-2">

                        Cancel

                    </a>


                    <button
                        type="submit"
                        class="btn btn-warning">

                        Update Client

                    </button>

                </div>

            </form>

        </div>

    </div>

</div>

</body>
</html>