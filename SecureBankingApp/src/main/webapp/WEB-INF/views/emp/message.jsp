<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<div class="container col-sm-6 col-sm-offset-3 ">

    <c:if test="${page.valid == true}">
        <c:if test="${not empty page.message}">
            <div class="alert alert-success">
                <c:out value="${page.message}"></c:out>
            </div>
        </c:if>
    </c:if>
    <c:if test="${page.valid == false}">
        <c:if test="${not empty page.message}">
            <div class="alert alert-danger">
                <c:out value="${page.message}"></c:out>
            </div>
        </c:if>
    </c:if>
</div>
</body>

</html>