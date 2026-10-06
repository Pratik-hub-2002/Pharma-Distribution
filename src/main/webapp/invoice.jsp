<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>

<%@ page import="com.pratik.pharma.model.Invoice"%>
<%@ page import="com.pratik.pharma.model.Order"%>
<%@ page import="com.pratik.pharma.model.Client"%>
<%@ page import="com.pratik.pharma.model.CompanyProfile"%>
<%@ page import="com.pratik.pharma.model.InvoiceItem"%>


<%
Invoice invoice = (Invoice) request.getAttribute("invoice");

Order order = (Order) request.getAttribute("order");

Client client = (Client) request.getAttribute("client");

CompanyProfile companyProfile = (CompanyProfile) request.getAttribute("companyProfile");

List<InvoiceItem> invoiceItems = (List<InvoiceItem>) request.getAttribute("invoiceItems");
%>


<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title><%=invoice.getInvoiceNumber()%></title>


<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">


<style>
body {
	background: #f4f6f9;
	font-family: Arial, sans-serif;
}

.invoice-container {
	max-width: 1200px;
	margin: 30px auto;
	background: white;
	padding: 35px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.10);
}

.company-name {
	font-size: 28px;
	font-weight: bold;
}

.invoice-title {
	font-size: 26px;
	font-weight: bold;
}

.section-title {
	background: #f1f3f5;
	padding: 8px 12px;
	font-weight: bold;
	margin-bottom: 10px;
}

.summary-table {
	max-width: 400px;
	margin-left: auto;
}

.grand-total {
	font-size: 20px;
	font-weight: bold;
}

.signature {
	margin-top: 70px;
	text-align: right;
}

.invoice-items th {
	font-size: 13px;
	white-space: nowrap;
}

.invoice-items td {
	font-size: 13px;
	vertical-align: middle;
}

@media print {
	body {
		background: white;
	}
	.invoice-container {
		margin: 0;
		max-width: 100%;
		box-shadow: none;
	}
	.no-print {
		display: none !important;
	}
}
</style>

</head>


<body>


	<div class="invoice-container">


		<!-- =====================================================
         HEADER
    ====================================================== -->

		<div class="row">


			<div class="col-md-7">

				<div class="company-name">

					<%=companyProfile.getCompanyName()%>

				</div>


				<strong> <%=companyProfile.getLegalName()%>

				</strong> <br>


				<%=companyProfile.getAddressLine1()%>


				<br>


				<%
				if (companyProfile.getAddressLine2() != null && !companyProfile.getAddressLine2().isBlank()) {
				%>

				<%=companyProfile.getAddressLine2()%>

				<br>

				<%
				}
				%>


				<%=companyProfile.getCity()%>,

				<%=companyProfile.getState()%>

				-

				<%=companyProfile.getPostalCode()%>


				<br> Phone:

				<%=companyProfile.getPhone()%>


				<br> Email:

				<%=companyProfile.getEmail()%>

			</div>


			<div class="col-md-5 text-end">

				<div class="invoice-title">TAX INVOICE</div>


				<br> <strong> Invoice No: </strong>

				<%=invoice.getInvoiceNumber()%>


				<br> <strong> Invoice Date: </strong>

				<%=invoice.getInvoiceDate()%>


				<br> <strong> Order No: </strong> #<%=order.getOrderId()%>


				<br> <strong> Status: </strong>

				<%=invoice.getInvoiceStatus()%>

			</div>

		</div>


		<hr>


		<!-- =====================================================
         SELLER DETAILS
    ====================================================== -->

		<div class="section-title">Seller Details</div>


		<div class="row">


			<div class="col-md-6">

				<strong>GSTIN:</strong>

				<%=companyProfile.getGstin()%>


				<br> <strong>PAN:</strong>

				<%=companyProfile.getPanNumber()%>

			</div>


			<div class="col-md-6">

				<strong>Drug License:</strong>

				<%=companyProfile.getDrugLicenseNumber()%>


				<br> <strong>Wholesale License:</strong>

				<%=companyProfile.getWholesaleLicenseNumber()%>

			</div>

		</div>


		<br>


		<!-- =====================================================
         BUYER DETAILS
    ====================================================== -->

		<div class="section-title">Bill To</div>


		<div class="row">


			<div class="col-md-6">

				<strong> <%=client.getClientName()%>

				</strong> <br> Owner:

				<%=client.getOwnerName()%>


				<br>


				<%=client.getAddress()%>


				<br>


				<%=client.getCity()%>,

				<%=client.getState()%>

				-

				<%=client.getPincode()%>


				<br> Phone:

				<%=client.getPhone()%>


				<br> Email:

				<%=client.getEmail()%>

			</div>


			<div class="col-md-6">

				<strong>GSTIN:</strong>

				<%=client.getGstNumber()%>


				<br> <strong>PAN:</strong>

				<%=client.getPanNumber() != null ? client.getPanNumber() : "N/A"%>


				<br> <strong>Drug License:</strong>

				<%=client.getDrugLicenseNumber() != null ? client.getDrugLicenseNumber() : "N/A"%>


				<br> <strong>Wholesale License:</strong>

				<%=client.getWholesaleLicenseNumber() != null ? client.getWholesaleLicenseNumber() : "N/A"%>


				<br> <strong>Purchase Permit:</strong>

				<%=client.getPurchasePermitNumber() != null ? client.getPurchasePermitNumber() : "N/A"%>

			</div>

		</div>


		<br>


		<!-- =====================================================
         PRODUCT / BATCH DETAILS
    ====================================================== -->

		<div class="section-title">Order Details</div>


		<div class="table-responsive">


			<table class="table table-bordered table-sm invoice-items">


				<thead class="table-dark">


					<tr>

						<th>#</th>

						<th>Product</th>

						<th>Batch</th>

						<th>Mfg</th>

						<th>Exp</th>

						<th>HSN</th>

						<th>Qty</th>

						<th>Free</th>

						<th>Rate</th>

						<th>GST</th>

						<th>Total</th>

					</tr>


				</thead>


				<tbody>


					<%
					int serialNumber = 1;

					if (invoiceItems != null && !invoiceItems.isEmpty()) {

						for (InvoiceItem item : invoiceItems) {
					%>


					<tr>


						<td><%=serialNumber++%></td>


						<td><strong> <%=item.getProductName()%>

						</strong> <br> <small> <%=item.getProductCode()%>

						</small></td>


						<td><%=item.getBatchNumber()%></td>


						<td><%=item.getManufacturingDate()%></td>


						<td><%=item.getExpiryDate()%></td>


						<td><%=item.getHsnCode()%></td>


						<td><%=item.getOrderedQuantity()%></td>


						<td><%=item.getFreeQuantity()%></td>


						<td>₹<%=item.getUnitPrice()%>

						</td>


						<td><%=item.getGstRate()%>%</td>


						<td>₹<%=item.getLineTotal()%>

						</td>


					</tr>


					<%
					}

					} else {
					%>


					<tr>

						<td colspan="11" class="text-center text-danger">No invoice
							items found.</td>

					</tr>


					<%
					}
					%>


				</tbody>


			</table>


		</div>


		<!-- =====================================================
         SUMMARY
    ====================================================== -->

		<table class="table table-bordered summary-table">


			<tr>

				<th>Subtotal</th>

				<td class="text-end">₹<%=invoice.getSubtotal()%>

				</td>

			</tr>


			<tr>

				<th>Discount</th>

				<td class="text-end">₹<%=invoice.getDiscountAmount()%>

				</td>

			</tr>


			<tr>

				<th>GST / Tax</th>

				<td class="text-end">₹<%=invoice.getTaxAmount()%>

				</td>

			</tr>


			<tr>

				<th class="grand-total">Grand Total</th>

				<td class="text-end grand-total">₹<%=invoice.getTotalAmount()%>

				</td>

			</tr>


			<tr>

				<th>Due Date</th>

				<td class="text-end"><%=invoice.getDueDate()%></td>

			</tr>


		</table>


		<!-- =====================================================
         BANK DETAILS
    ====================================================== -->

		<div class="section-title">Bank Details</div>


		<div>

			<strong>Bank:</strong>

			<%=companyProfile.getBankName()%>


			<br> <strong>Account Name:</strong>

			<%=companyProfile.getBankAccountName()%>


			<br> <strong>Account Number:</strong>

			<%=companyProfile.getBankAccountNumber()%>


			<br> <strong>IFSC:</strong>

			<%=companyProfile.getIfscCode()%>

		</div>


		<!-- =====================================================
         TERMS
    ====================================================== -->

		<div class="section-title mt-4">Terms & Conditions</div>


		<ol>

			<li>Goods once sold are subject to applicable business terms and
				conditions.</li>


			<li>Payment should be made within the agreed credit period.</li>


			<li>All disputes are subject to the applicable jurisdiction.</li>

		</ol>


		<!-- =====================================================
         SIGNATURE
    ====================================================== -->

		<div class="signature">

			For <strong> <%=companyProfile.getCompanyName()%>

			</strong> <br> <br> <br> Authorized Signatory

		</div>


		<!-- =====================================================
         BUTTONS
    ====================================================== -->

		<div class="text-center mt-4 no-print">


			<button onclick="window.print()" class="btn btn-primary">

				Print Invoice</button>


			<a href="orders?action=view&id=<%=order.getOrderId()%>"
				class="btn btn-secondary"> Back to Order </a>


		</div>


	</div>


</body>

</html>