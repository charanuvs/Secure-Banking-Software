<%@ page language="java" contentType="text/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>XYZ Bank</title>

<!-- include css -->
<jsp:include page="../css.jsp"></jsp:include>

<!-- include js -->
<jsp:include page="../js.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="nav.jsp"></jsp:include>

	<div id="main-content">
		<table style="width: 60%">
			<tr style="background-color: Bisque">
				<th>Account Numbers</th>
				<th>Balance</th>
				<th>Date of Open</th>
			</tr>
			<c:forEach var="account" varStatus="status" items="${accounts}">
				<tr>
					<td><c:out value="${account.accountNum}" /></td>
					<td><c:out value="${account.balance() }" /></td>
					<td><c:out value="${account.openingDate() }" /></td>
				</tr>
			</c:forEach>

		</table>
		<br /> <br /> <br /> <br />

		<form:form id="view-statements-form" method="POST"
			commandName="ViewAccountDetail" ModelAttribute="ViewAccountDetail">
			<div id="errors" style="color: #ff0000"></div>
			Enter Account Number: <form:input type="text" name="accountId"
				id="accountId" path="accountId" />
			<form:input type="submit" class="btn btn-primary btn-lg btn-sm-10"
				name="viewStatements" id="viewStatements" value="View Statements"
				path="viewStatements" />
		</form:form>

		<table class="table table-striped table-bordered">
			<tr>
				<th>Transaction ID</th>
				<th>From Account</th>
				<th>To Account</th>
				<th>Amount</th>
				<th>Transaction Type</th>
				<th>Transaction Status</th>
			</tr>
			<c:if test="${not empty Transactions}">
				<c:forEach var="transaction" varStatus="status"
					items="${Transactions}">
					<tr>
						<td><c:out value="${transaction.transactionId() }" /></td>
						<td><c:out value="${transaction.fromAccount() }" /></td>
						<td><c:out value="${transaction.toAccount() }" /></td>
						<td><c:out value="${transaction.amount() }" /></td>
						<td><c:out value="${transaction.transactionType() }" /></td>
						<td><c:out value="${transaction.status() }" /></td>
					</tr>
				</c:forEach>
			</c:if>
		</table>
		<c:if test="${fn:length(accounts) eq 0}">
			<div class="well">No accounts found for this user</div>
		</c:if>
		<div class="container">
			<a href="<c:url value='/manage/view/statements/${username}' />"
				class="btn btn-default">Go Back</a>
		</div>
	</div>

</body>

</html>