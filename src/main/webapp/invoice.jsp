<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%@ page import="com.pratik.pharma.model.Invoice"%>
<%@ page import="com.pratik.pharma.model.InvoiceItem"%>
<%@ page import="com.pratik.pharma.model.Order"%>
<%@ page import="com.pratik.pharma.model.Client"%>
<%@ page import="com.pratik.pharma.model.CompanyProfile"%>


<%
Invoice invoice = (Invoice) request.getAttribute("invoice");
Order order = (Order) request.getAttribute("order");
Client client = (Client) request.getAttribute("client");
CompanyProfile company = (CompanyProfile) request.getAttribute("companyProfile");

List<InvoiceItem> invoiceItems = (List<InvoiceItem>) request.getAttribute("invoiceItems");

SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

if (invoice == null) {
%>

<div style="padding: 30px; font-family: Arial;">

	<h2>Invoice Not Found</h2>

	<a href="<%=request.getContextPath()%>/invoices?action=list"> Back
		to Invoice List </a>

</div>

<%
return;
}
%>


<!DOCTYPE html>

<html lang="en">

<head>

<meta charset="UTF-8">

<title>Tax Invoice - <%=invoice.getInvoiceNumber()%>
</title>

<meta name="viewport" content="width=device-width, initial-scale=1">


<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">


<style>

/* =========================================================
   GENERAL
========================================================= */
body {
	background: #f2f4f7;
	font-family: Arial, Helvetica, sans-serif;
	margin: 0;
	padding: 0;
}

/* =========================================================
   INVOICE CONTAINER
========================================================= */
.invoice-container {
	max-width: 1100px;
	margin: 30px auto;
	background: white;
	padding: 35px;
	box-shadow: 0 0 15px rgba(0, 0, 0, 0.12);
}

/* =========================================================
   COMPANY
========================================================= */
.company-name {
	font-size: 28px;
	font-weight: bold;
	color: #17365d;
	margin-bottom: 4px;
}

/* =========================================================
   INVOICE TITLE
========================================================= */
.invoice-title {
	font-size: 30px;
	font-weight: bold;
	letter-spacing: 1px;
	color: #17365d;
}

/* =========================================================
   SECTION TITLE
========================================================= */
.section-title {
	background: #17365d;
	color: white;
	padding: 8px 12px;
	font-weight: bold;
	margin-top: 20px;
	margin-bottom: 0;
}

/* =========================================================
   DETAILS BOX
========================================================= */
.details-box {
	border: 1px solid #ddd;
	padding: 15px;
	min-height: 170px;
}

/* =========================================================
   INVOICE TABLE
========================================================= */
.invoice-table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

.invoice-table th {
	background: #17365d;
	color: white;
	padding: 8px;
	border: 1px solid #ccc;
	font-size: 12px;
	text-align: center;
}

.invoice-table td {
	padding: 7px;
	border: 1px solid #ccc;
	font-size: 12px;
}

.text-right {
	text-align: right;
}

.text-center {
	text-align: center;
}

/* =========================================================
   SUMMARY
========================================================= */
.summary-table {
	width: 380px;
	margin-left: auto;
	margin-top: 20px;
	border-collapse: collapse;
}

.summary-table td {
	padding: 8px;
	border-bottom: 1px solid #ddd;
}

.grand-total {
	font-size: 18px;
	font-weight: bold;
	background: #eaf2f8;
}

/* =========================================================
   BANK DETAILS
========================================================= */
.bank-box {
	border: 1px solid #ddd;
	padding: 15px;
	margin-top: 25px;
}

/* =========================================================
   TERMS
========================================================= */
.terms {
	margin-top: 30px;
	font-size: 12px;
	color: #555;
}

/* =========================================================
   SIGNATURE
========================================================= */
.signature {
	margin-top: 60px;
	text-align: right;
}

/* =========================================================
   ACTION BUTTONS
========================================================= */
.action-buttons {
	max-width: 1100px;
	margin: 20px auto;
}

/* =========================================================
   PRINT REPEATING HEADER
========================================================= */

/*
   This header is hidden on normal screen.

   During printing it becomes fixed and therefore
   appears at the top of every printed page.
*/
.print-repeat-header {
	display: none;
}

/* =========================================================
   PRINT
========================================================= */
@media print {
	@page {
		size: A4 landscape;
		margin: 8mm;
	}
	body {
		background: white !important;
		margin: 0 !important;
		padding: 0 !important;
	}

	/* =====================================================
	   HIDE PRINT BUTTONS
	===================================================== */
	.action-buttons {
		display: none !important;
	}

	/* =====================================================
	   HIDE NORMAL SCREEN HEADER
	===================================================== */
	.screen-company-header {
		display: none !important;
	}
	.screen-seller-details {
		display: none !important;
	}
	.screen-bill-details {
		display: none !important;
	}

	/* =====================================================
	   REPEATING HEADER

	   IMPORTANT:
	   Header is deliberately given enough height so
	   Bill To never gets cut between pages.
	===================================================== */
	.print-repeat-header {
		display: block !important;
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		width: 100%;
		height: 440px;
		background: white;
		z-index: 9999;
		padding: 0 5px 10px 5px;
		box-sizing: border-box;
		overflow: hidden;
	}

	/* =====================================================
	   CONTENT STARTS BELOW REPEATING HEADER
	===================================================== */
	.print-content {
		margin-top: 455px;
	}
	.invoice-container {
		margin: 0 !important;
		padding: 0 !important;
		max-width: 100% !important;
		background: white;
		box-shadow: none !important;
	}

	/* =====================================================
	   INVOICE ITEMS TABLE
	===================================================== */
	.invoice-table {
		width: 100%;
		margin-top: 8px;
		border-collapse: collapse;
	}

	/* Repeat table heading on every page */
	.invoice-table thead {
		display: table-header-group;
	}
	.invoice-table tfoot {
		display: table-footer-group;
	}
	.invoice-table tr {
		page-break-inside: avoid !important;
		break-inside: avoid !important;
	}
	.invoice-table th {
		background: #17365d !important;
		color: white !important;
		font-size: 9px !important;
		padding: 5px !important;
	}
	.invoice-table td {
		font-size: 9px !important;
		padding: 5px !important;
	}

	/* =====================================================
	   KEEP TOTALS TOGETHER
	===================================================== */
	.summary-table {
		page-break-inside: avoid !important;
		break-inside: avoid !important;
	}

	/* =====================================================
	   KEEP BANK DETAILS TOGETHER
	===================================================== */
	.bank-box {
		page-break-inside: avoid !important;
		break-inside: avoid !important;
	}

	/* =====================================================
	   KEEP TERMS TOGETHER
	===================================================== */
	.terms {
		page-break-inside: avoid !important;
		break-inside: avoid !important;
	}

	/* =====================================================
	   KEEP SIGNATURE TOGETHER
	===================================================== */
	.signature {
		page-break-inside: avoid !important;
		break-inside: avoid !important;
	}

	/* =====================================================
	   SECTION TITLE
	===================================================== */
	.section-title {
		page-break-after: avoid !important;
		break-after: avoid !important;
	}

	/* =====================================================
	   REMOVE EXTRA SPACING
	===================================================== */
	.bank-box {
		margin-top: 15px !important;
	}
	.terms {
		margin-top: 15px !important;
	}
	.signature {
		margin-top: 30px !important;
	}
}

/* =====================================================
	   REPEATING HEADER
	===================================================== */
.print-repeat-header {
	display: block;
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	width: 100%;
	height: 350px;
	background: white;
	z-index: 9999;
	padding: 5px 10px 10px 10px;
}

/* =====================================================
	   PRINT CONTENT
	===================================================== */
.print-content {
	margin-top: 365px;
}

.invoice-container {
	margin: 0;
	padding: 0;
	max-width: 100%;
	box-shadow: none;
}

/* =====================================================
	   REPEAT TABLE HEADER
	===================================================== */
.invoice-table {
	margin-top: 10px;
}

.invoice-table thead {
	display: table-header-group;
}

.invoice-table tfoot {
	display: table-footer-group;
}

.invoice-table tr {
	page-break-inside: avoid;
	break-inside: avoid;
}

.invoice-table td, .invoice-table th {
	font-size: 10px;
	padding: 5px;
}

/* =====================================================
	   AVOID BAD PAGE BREAKS
	===================================================== */
.summary-table {
	page-break-inside: avoid;
	break-inside: avoid;
}

.bank-box {
	page-break-inside: avoid;
	break-inside: avoid;
}

.terms {
	page-break-inside: avoid;
	break-inside: avoid;
}

.signature {
	page-break-inside: avoid;
	break-inside: avoid;
}

.section-title {
	page-break-after: avoid;
	break-after: avoid;
}

}

/* =========================================================
   REPEATING HEADER INTERNAL STYLES
========================================================= */
.print-company-name {
	font-size: 23px;
	font-weight: bold;
	color: #17365d;
	margin-bottom: 2px;
}

.print-legal-name {
	font-size: 13px;
	font-weight: bold;
}

.print-small {
	font-size: 11px;
	line-height: 1.35;
}

.print-invoice-title {
	font-size: 24px;
	font-weight: bold;
	color: #17365d;
	letter-spacing: 1px;
	margin-bottom: 8px;
}

.print-section-title {
	background: #17365d;
	color: white;
	padding: 4px 8px;
	font-size: 12px;
	font-weight: bold;
	margin-top: 5px;
}

.print-box {
	border: 1px solid #bbb;
	padding: 7px;
	font-size: 10px;
	line-height: 1.35;
}

.print-details-table {
	width: 100%;
	border-collapse: collapse;
}

.print-details-table td {
	vertical-align: top;
	padding: 3px 5px;
}

.print-divider {
	border-top: 1px solid #999;
	margin-top: 5px;
	margin-bottom: 5px;
}
</style>

</head>


<body>


	<!-- =========================================================
     ACTION BUTTONS
========================================================= -->

	<div class="action-buttons">

		<a href="<%=request.getContextPath()%>/invoices?action=list"
			class="btn btn-secondary"> ← Back to Invoice List </a>


		<button onclick="window.print()" class="btn btn-primary">🖨
			Print Invoice</button>

	</div>



	<!-- =========================================================
     INVOICE
========================================================= -->

	<div class="invoice-container">


		<!-- =====================================================
	     PRINT REPEATING HEADER

	     THIS WILL APPEAR ON EVERY PRINTED PAGE
	====================================================== -->

		<div class="print-repeat-header">


			<!-- COMPANY + TAX INVOICE -->

			<table class="print-details-table">

				<tr>

					<td style="width: 60%;">

						<div class="print-company-name">

							<%=company != null && company.getCompanyName() != null ? company.getCompanyName() : "AVNI PHARMA"%>

						</div>


						<div class="print-legal-name">

							<%=company != null && company.getLegalName() != null ? company.getLegalName() : ""%>

						</div> <%
 if (company != null) {
 %>

						<div class="print-small">

							<%=company.getAddressLine1() != null ? company.getAddressLine1() : ""%>

							<br>

							<%=company.getAddressLine2() != null ? company.getAddressLine2() : ""%>

							<br>

							<%=company.getCity() != null ? company.getCity() : ""%>,

							<%=company.getState() != null ? company.getState() : ""%>

							-

							<%=company.getPostalCode() != null ? company.getPostalCode() : ""%>

							<br> Phone:

							<%=company.getPhone() != null ? company.getPhone() : "N/A"%>

							<br> Email:

							<%=company.getEmail() != null ? company.getEmail() : "N/A"%>

						</div> <%
 }
 %>

					</td>


					<td style="width: 40%; text-align: right;">

						<div class="print-invoice-title">TAX INVOICE</div>


						<div class="print-small">

							<strong>Invoice No:</strong>

							<%=invoice.getInvoiceNumber()%>

							<br> <strong>Invoice Date:</strong>

							<%=invoice.getInvoiceDate() != null ? dateFormat.format(invoice.getInvoiceDate()) : ""%>

							<br> <strong>Order No:</strong> #<%=invoice.getOrderId()%>

							<br> <strong>Status:</strong>

							<%=invoice.getInvoiceStatus()%>

						</div>

					</td>

				</tr>

			</table>



			<!-- SELLER + BILL TO -->

			<table class="print-details-table">

				<tr>


					<!-- SELLER -->

					<td style="width: 50%;">

						<div class="print-section-title">Seller Details</div>


						<div class="print-box">

							<%
							if (company != null) {
							%>

							<strong>GSTIN:</strong>

							<%=company.getGstin() != null ? company.getGstin() : "N/A"%>

							<br> <strong>PAN:</strong>

							<%=company.getPanNumber() != null ? company.getPanNumber() : "N/A"%>

							<br> <strong>Drug License:</strong>

							<%=company.getDrugLicenseNumber() != null ? company.getDrugLicenseNumber() : "N/A"%>

							<br> <strong>Wholesale License:</strong>

							<%=company.getWholesaleLicenseNumber() != null ? company.getWholesaleLicenseNumber() : "N/A"%>

							<%
							}
							%>

						</div>

					</td>



					<!-- BILL TO -->

					<td style="width: 50%;">

						<div class="print-section-title">Bill To</div>


						<div class="print-box">

							<%
							if (client != null) {
							%>

							<strong> <%=client.getClientName() != null ? client.getClientName() : ""%>
							</strong> <br> Owner:

							<%=client.getOwnerName() != null ? client.getOwnerName() : "N/A"%>

							<br> Contact Person:

							<%=client.getContactPerson() != null ? client.getContactPerson() : "N/A"%>

							<br>


							<%=client.getAddress() != null ? client.getAddress() : ""%>

							<br>


							<%=client.getCity() != null ? client.getCity() : ""%>,

							<%=client.getState() != null ? client.getState() : ""%>

							-

							<%=client.getPincode() != null ? client.getPincode() : ""%>

							<br> Phone:

							<%=client.getPhone() != null ? client.getPhone() : "N/A"%>

							<br> Email:

							<%=client.getEmail() != null ? client.getEmail() : "N/A"%>

							<hr style="margin: 4px 0;">


							<strong>GSTIN:</strong>

							<%=client.getGstNumber() != null ? client.getGstNumber() : "N/A"%>

							<br> <strong>PAN:</strong>

							<%=client.getPanNumber() != null ? client.getPanNumber() : "N/A"%>

							<br> <strong>Drug License:</strong>

							<%=client.getDrugLicenseNumber() != null ? client.getDrugLicenseNumber() : "N/A"%>

							<br> <strong>Wholesale License:</strong>

							<%=client.getWholesaleLicenseNumber() != null ? client.getWholesaleLicenseNumber() : "N/A"%>

							<br> <strong>Purchase Permit:</strong>

							<%=client.getPurchasePermitNumber() != null ? client.getPurchasePermitNumber() : "N/A"%>

							<%
							}
							%>

						</div>

					</td>

				</tr>

			</table>


		</div>



		<!-- =====================================================
	     NORMAL SCREEN COMPANY HEADER
	====================================================== -->

		<div class="screen-company-header">


			<div class="row">


				<div class="col-md-7">


					<div class="company-name">

						<%=company != null && company.getCompanyName() != null ? company.getCompanyName() : "AVNI PHARMA"%>

					</div>


					<strong> <%=company != null && company.getLegalName() != null ? company.getLegalName() : ""%>

					</strong> <br>


					<%
					if (company != null) {
					%>


					<%=company.getAddressLine1() != null ? company.getAddressLine1() : ""%>

					<br>


					<%=company.getAddressLine2() != null ? company.getAddressLine2() : ""%>

					<br>


					<%=company.getCity() != null ? company.getCity() : ""%>,

					<%=company.getState() != null ? company.getState() : ""%>

					-

					<%=company.getPostalCode() != null ? company.getPostalCode() : ""%>

					<br> Phone:

					<%=company.getPhone() != null ? company.getPhone() : "N/A"%>

					<br> Email:

					<%=company.getEmail() != null ? company.getEmail() : "N/A"%>


					<%
					}
					%>

				</div>



				<div class="col-md-5 text-end">


					<div class="invoice-title">TAX INVOICE</div>


					<br> <strong>Invoice No:</strong>

					<%=invoice.getInvoiceNumber()%>


					<br> <strong>Invoice Date:</strong>

					<%=invoice.getInvoiceDate() != null ? dateFormat.format(invoice.getInvoiceDate()) : ""%>


					<br> <strong>Order No:</strong> #<%=invoice.getOrderId()%>


					<br> <strong>Status:</strong> <span
						class="badge bg-warning text-dark"> <%=invoice.getInvoiceStatus()%>

					</span>


				</div>


			</div>


		</div>



		<!-- =====================================================
	     SCREEN SELLER DETAILS
	====================================================== -->

		<div class="screen-seller-details">


			<div class="section-title">Seller Details</div>


			<div class="details-box">


				<%
				if (company != null) {
				%>


				<strong>GSTIN:</strong>

				<%=company.getGstin() != null ? company.getGstin() : "N/A"%>


				<br> <strong>PAN:</strong>

				<%=company.getPanNumber() != null ? company.getPanNumber() : "N/A"%>


				<br> <strong>Drug License:</strong>

				<%=company.getDrugLicenseNumber() != null ? company.getDrugLicenseNumber() : "N/A"%>


				<br> <strong>Wholesale License:</strong>

				<%=company.getWholesaleLicenseNumber() != null ? company.getWholesaleLicenseNumber() : "N/A"%>


				<%
				}
				%>


			</div>


		</div>



		<!-- =====================================================
	     SCREEN CUSTOMER DETAILS
	====================================================== -->

		<div class="screen-bill-details">


			<div class="section-title">Bill To</div>


			<div class="details-box">


				<%
				if (client != null) {
				%>


				<strong> <%=client.getClientName() != null ? client.getClientName() : ""%>

				</strong> <br> Owner:

				<%=client.getOwnerName() != null ? client.getOwnerName() : "N/A"%>

				<br> Contact Person:

				<%=client.getContactPerson() != null ? client.getContactPerson() : "N/A"%>

				<br>


				<%=client.getAddress() != null ? client.getAddress() : ""%>

				<br>


				<%=client.getCity() != null ? client.getCity() : ""%>,

				<%=client.getState() != null ? client.getState() : ""%>

				-

				<%=client.getPincode() != null ? client.getPincode() : ""%>

				<br> Phone:

				<%=client.getPhone() != null ? client.getPhone() : "N/A"%>

				<br> Email:

				<%=client.getEmail() != null ? client.getEmail() : "N/A"%>


				<hr>


				<strong>GSTIN:</strong>

				<%=client.getGstNumber() != null ? client.getGstNumber() : "N/A"%>

				<br> <strong>PAN:</strong>

				<%=client.getPanNumber() != null ? client.getPanNumber() : "N/A"%>

				<br> <strong>Drug License:</strong>

				<%=client.getDrugLicenseNumber() != null ? client.getDrugLicenseNumber() : "N/A"%>

				<br> <strong>Wholesale License:</strong>

				<%=client.getWholesaleLicenseNumber() != null ? client.getWholesaleLicenseNumber() : "N/A"%>

				<br> <strong>Purchase Permit:</strong>

				<%=client.getPurchasePermitNumber() != null ? client.getPurchasePermitNumber() : "N/A"%>


				<%
				} else {
				%>


				Customer information not available.


				<%
				}
				%>


			</div>


		</div>



		<!-- =====================================================
	     PRINT CONTENT
	====================================================== -->

		<div class="print-content">


			<!-- =================================================
		     INVOICE ITEMS
		================================================== -->

			<div class="section-title">Invoice Items</div>


			<table class="invoice-table">


				<thead>

					<tr>

						<th>#</th>

						<th>Product</th>

						<th>Batch</th>

						<th>Mfg Date</th>

						<th>Expiry Date</th>

						<th>HSN</th>

						<th>Qty</th>

						<th>Free</th>

						<th>Rate</th>

						<th>GST %</th>

						<th>Amount</th>

					</tr>

				</thead>


				<tbody>


					<%
					int serialNo = 1;

					if (invoiceItems != null) {

						for (InvoiceItem item : invoiceItems) {
					%>


					<tr>


						<td class="text-center"><%=serialNo++%></td>


						<td><strong> <%=item.getProductName()%>

						</strong> <br> <small> Code: <%=item.getProductCode()%>

						</small></td>


						<td class="text-center"><%=item.getBatchNumber() != null ? item.getBatchNumber() : "N/A"%></td>


						<td class="text-center"><%=item.getManufacturingDate() != null ? dateFormat.format(item.getManufacturingDate()) : "N/A"%></td>


						<td class="text-center"><%=item.getExpiryDate() != null ? dateFormat.format(item.getExpiryDate()) : "N/A"%></td>


						<td class="text-center"><%=item.getHsnCode() != null ? item.getHsnCode() : "N/A"%></td>


						<td class="text-center"><%=item.getOrderedQuantity()%></td>


						<td class="text-center"><%=item.getFreeQuantity()%></td>


						<td class="text-right">₹<%=item.getUnitPrice()%>

						</td>


						<td class="text-center"><%=item.getGstRate()%>%</td>


						<td class="text-right">₹<%=item.getLineTotal()%>

						</td>


					</tr>


					<%
					}

					} else {
					%>


					<tr>

						<td colspan="11" class="text-center">No invoice items found.

						</td>

					</tr>


					<%
					}
					%>


				</tbody>


			</table>



			<!-- =================================================
		     TOTALS
		================================================== -->

			<table class="summary-table">


				<tr>

					<td>Subtotal</td>


					<td class="text-right">₹<%=invoice.getSubtotal()%>

					</td>

				</tr>


				<tr>

					<td>Discount</td>


					<td class="text-right">₹<%=invoice.getDiscountAmount()%>

					</td>

				</tr>


				<tr>

					<td>GST / Tax</td>


					<td class="text-right">₹<%=invoice.getTaxAmount()%>

					</td>

				</tr>


				<tr class="grand-total">

					<td>Grand Total</td>


					<td class="text-right">₹<%=invoice.getTotalAmount()%>

					</td>

				</tr>


				<tr>

					<td>Due Date</td>


					<td class="text-right"><%=invoice.getDueDate() != null ? dateFormat.format(invoice.getDueDate()) : "N/A"%></td>

				</tr>


			</table>



			<!-- =================================================
		     BANK DETAILS
		================================================== -->

			<div class="bank-box">


				<h6>Bank Details</h6>


				<%
				if (company != null) {
				%>


				<strong>Bank:</strong>

				<%=company.getBankName() != null ? company.getBankName() : "N/A"%>


				<br> <strong>Account Name:</strong>

				<%=company.getBankAccountName() != null ? company.getBankAccountName() : "N/A"%>


				<br> <strong>Account Number:</strong>

				<%=company.getBankAccountNumber() != null ? company.getBankAccountNumber() : "N/A"%>


				<br> <strong>IFSC:</strong>

				<%=company.getIfscCode() != null ? company.getIfscCode() : "N/A"%>


				<%
				}
				%>


			</div>



			<!-- =================================================
		     TERMS
		================================================== -->

			<div class="terms">


				<h6>Terms & Conditions</h6>


				<ol>


					<li>Goods once sold will not be accepted back except as per
						agreed company policy.</li>


					<li>Payment should be made within the agreed credit period.</li>


					<li>All disputes are subject to the jurisdiction of the
						seller's registered location.</li>


					<li>Medicines must be stored according to the recommended
						storage conditions.</li>


					<li>Batch number and expiry date should be checked before
						dispensing.</li>


				</ol>


			</div>



			<!-- =================================================
		     SIGNATURE
		================================================== -->

			<div class="signature">


				<strong> For <%=company != null && company.getCompanyName() != null ? company.getCompanyName() : "AVNI PHARMA"%>

				</strong> <br> <br> <br> Authorized Signatory


			</div>


		</div>


	</div>



	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
		
	</script>


</body>

</html>