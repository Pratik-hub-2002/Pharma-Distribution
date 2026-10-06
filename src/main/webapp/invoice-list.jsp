<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Invoices</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>

<body>

	<div class="container mt-5">

		<h2>Invoice Management</h2>

		<hr>

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

		<p>Invoice module is connected successfully.</p>

		<a href="dashboard.jsp" class="btn btn-secondary"> Back to
			Dashboard </a>

	</div>

</body>
</html>