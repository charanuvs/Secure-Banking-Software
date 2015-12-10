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
<jsp:include page="nav.jsp"/>

<div class="container col-sm-6 col-sm-offset-3 ">
    <!--
    <c:if test="${page.valid == false}">
        <div class="alert alert-danger">Something wrong</div>
    </c:if>

    <div class="well">
        Welcome - <c:out value="${loggedInUser.name}"/>
    </div>
-->
    <p style="color:green">Your transaction was successfully submitted and is awaiting approval</p>
</div>
</body>

</html>