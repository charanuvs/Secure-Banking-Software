<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

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

    <h3>Displaying the last 1000 lines in the log file</h3>

    <c:if test="${fn:length(logs) gt 0}">
        <div class="container-fluid">
            <c:forEach items="${logs}" var="log">
                <code><c:out value="${log}"/></code>
            </c:forEach>
        </div>
    </c:if>
    <c:if test="${fn:length(logs) eq 0}">
        <div class="well">No logs found</div>
    </c:if>

</div>

</body>

</html>