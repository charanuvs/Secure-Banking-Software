<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

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

<div class="container col-sm-8 col-sm-offset-2 ">

    <h3>Users</h3>

    <c:if test="${fn:length(piiRequests) gt 0}">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>#</th>
                <th>Request to User Details</th>
                <th>Access from User</th>
                <th>Authorize</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${piiRequests}" var="pii" varStatus="loop">
                <tr>
                    <td><c:out value="${loop.index + 1}"/></td>
                    <td><c:out value="${pii.user.name}"/></td>
                    <td><c:out value="${pii.fromUser.name}"/></td>
                    <td><a href="<c:url
                    value='/gov/authorize/${pii.user.userId}/${pii.fromUser.userId}'/>"
                           class="btn btn-default btn-sm">Authorize</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
    <c:if test="${fn:length(piiRequests) eq 0}">
        <div class="well">No requests found</div>
    </c:if>

</div>

</body>

</html>