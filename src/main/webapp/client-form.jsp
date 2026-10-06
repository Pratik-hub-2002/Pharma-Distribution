<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Add Client - Pharma Distribution</title>

    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">

</head>

<body class="bg-light">

<div class="container mt-4">

    <!-- Page Header -->

    <div class="d-flex justify-content-between align-items-center mb-4">

        <h2>Add New Client</h2>

        <a href="<%= request.getContextPath() %>/clients"
           class="btn btn-secondary">
            Back to Clients
        </a>

    </div>


    <!-- Client Form -->

    <div class="card shadow">

        <div class="card-header bg-primary text-white">

            <h5 class="mb-0">
                Client Information
            </h5>

        </div>


        <div class="card-body">

            <form action="<%= request.getContextPath() %>/clients"
                  method="post">


                <!-- Client Name / Client Type -->

                <div class="row mb-3">

                    <div class="col-md-6">

                        <label class="form-label">
                            Client Name <span class="text-danger">*</span>
                        </label>

                        <input
                            type="text"
                            name="clientName"
                            class="form-control"
                            maxlength="100"
                            required>

                    </div>


                    <div class="col-md-6">

                        <label class="form-label">
                            Client Type <span class="text-danger">*</span>
                        </label>

                        <select
                            name="clientType"
                            class="form-select"
                            required>

                            <option value="">
                                Select Client Type
                            </option>

                            <option value="MEDICAL_STORE">
                                Medical Store
                            </option>

                            <option value="HOSPITAL">
                                Hospital
                            </option>

                            <option value="IVF_CLINIC">
                                IVF Clinic
                            </option>

                            <option value="CLINIC">
                                Clinic
                            </option>

                            <option value="DISTRIBUTOR">
                                Distributor
                            </option>

                        </select>

                    </div>

                </div>


                <!-- Owner / Contact Person -->

                <div class="row mb-3">

                    <div class="col-md-6">

                        <label class="form-label">
                            Owner Name <span class="text-danger">*</span>
                        </label>

                        <input
                            type="text"
                            name="ownerName"
                            class="form-control"
                            maxlength="100"
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
                            maxlength="100">

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
                            maxlength="100">

                    </div>


                    <div class="col-md-6">

                        <label class="form-label">
                            Phone
                        </label>

                        <input
                            type="text"
                            name="phone"
                            class="form-control"
                            maxlength="15">

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
                        rows="3"
                        maxlength="255"></textarea>

                </div>


                <!-- City / State / Pincode -->

                <div class="row mb-3">

                    <div class="col-md-4">

                        <label class="form-label">
                            City
                        </label>

                        <input
                            type="text"
                            name="city"
                            class="form-control"
                            maxlength="50">

                    </div>


                    <div class="col-md-4">

                        <label class="form-label">
                            State
                        </label>

                        <input
                            type="text"
                            name="state"
                            class="form-control"
                            maxlength="50">

                    </div>


                    <div class="col-md-4">

                        <label class="form-label">
                            Pincode
                        </label>

                        <input
                            type="text"
                            name="pincode"
                            class="form-control"
                            maxlength="10">

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
                        maxlength="20">

                </div>


                <!-- Credit Information -->

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
                            value="0">

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
                            value="0">

                    </div>

                </div>


                <!-- Status / Verification -->

                <div class="row mb-4">

                    <div class="col-md-6">

                        <label class="form-label">
                            Status
                        </label>

                        <select
                            name="status"
                            class="form-select">

                            <option value="ACTIVE" selected>
                                ACTIVE
                            </option>

                            <option value="INACTIVE">
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

                            <option value="PENDING" selected>
                                PENDING
                            </option>

                            <option value="VERIFIED">
                                VERIFIED
                            </option>

                            <option value="REJECTED">
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
                        class="btn btn-primary">

                        Save Client

                    </button>

                </div>

            </form>

        </div>

    </div>

</div>

</body>
</html>