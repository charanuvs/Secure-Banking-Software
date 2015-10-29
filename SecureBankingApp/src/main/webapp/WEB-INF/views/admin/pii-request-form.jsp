<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Bank CSE545</title>

    <!-- include css -->
    <jsp:include page="../css.jsp"></jsp:include>

    <!-- include js -->
    <jsp:include page="../js.jsp"></jsp:include>
</head>
<body>
<jsp:include page="nav.jsp"></jsp:include>

<div class="container col-sm-6 col-sm-offset-3 ">


    <div class="well">

        <p>Are you sure you want request an access to see/update user information?</p>

        <form class="form-horizontal" action="<c:url value="/admin/request/" />" method="post">
            <input type="hidden" name="userId" value="${userId}">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="form-group">
                <div class="col-sm-7">
                    <input type="submit" id="submit" class="btn btn-default btn-sm-10"
                           value="Request">
                    <a class="btn btn-default btn-sm-10"
                       href="<c:url value="/admin/" />">Cancel</a>
                </div>
            </div>
        </form>
    </div>
</div>
</body>

</html>