<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Bank of Upper Concourse</title>

    <!-- include css -->
    <jsp:include page="../css.jsp"></jsp:include>

    <!-- include js -->
    <jsp:include page="../js.jsp"></jsp:include>
</head>

<body>
<jsp:include page="nav.jsp"></jsp:include>

<div class="container col-sm-8 col-sm-offset-2 ">

    <h3>Transaction Information for Account -
        <span class="text-muted"><c:out value="${account.accountNum}"/></span></h3>

    <div>Current Balance</div>
    <h3 class="text-success" style="margin-top: 0; padding-top: 0;">
        <strong><fmt:formatNumber value="${account.balance}"
                                  type="currency"/></strong>
    </h3>

    <c:if test="${not empty transactions}">
        <table class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>Date</th>
                <th>From Account</th>
                <th>To Account</th>
                <th>Amount</th>
                <th>Transaction Type</th>
                <th>Transaction Status</th>
            </tr>
            </thead>
            <tbody>

            <c:forEach var="transaction" varStatus="status"
                       items="${transactions}">
                <tr>
                    <td><fmt:formatDate value="${transaction.date}" pattern="MM/dd/yy"/></td>
                    <td>
                        <c:if test="${transaction.fromAccount.user.userId == user}">
                            <c:out value="${transaction.fromAccount.accountNum }"/>
                            <span class="text-muted">(Your account)</span>
                        </c:if>
                        <c:if test="${transaction.fromAccount.user.userId != user}">
                            <c:out value="${transaction.fromAccount.user.name}"/>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${transaction.toAccount.user.userId == user}">
                            <c:out value="${transaction.toAccount.accountNum }"/>
                            <span class="text-muted">(Your account)</span>
                        </c:if>
                        <c:if test="${transaction.toAccount.user.userId != user}">
                            <c:out value="${transaction.toAccount.user.name }"/>
                        </c:if>
                    </td>
                    <td><fmt:formatNumber value="${transaction.amount}"
                                          type="currency"/></td>
                    <td><c:out value="${transaction.transactionType}"/></td>
                    <td><c:out value="${transaction.status }"/></td>
                </tr>
            </c:forEach>

            </tbody>
        </table>

        <!-- Can download -->
        <div class="container-fluid"><a href="<c:url
        value='/user/statements/download/${account.accountNum}' />"
                                        class="btn btn-primary btn-lg">Download Statement</a></div>
    </c:if>

    <c:if test="${fn:length(transactions) eq 0}">
        <div class="well">No Transactions for this account</div>
    </c:if>

</div>

</body>

</html>