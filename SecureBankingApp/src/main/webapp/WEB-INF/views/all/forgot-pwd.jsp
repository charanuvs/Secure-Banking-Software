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
<nav class="navbar navbar-default navbar-static-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed"
                    data-toggle="collapse"
                    data-target="#navbar"
                    aria-expanded="false"
                    aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<c:url value='/'></c:url>">Bank of Upper Concourse</a>
        </div>
    </div>
    <!--/.container-fluid -->
</nav>

<div class="container col-sm-6 col-sm-offset-3 ">

    <c:if test="${page.valid == false}">
        <div class="alert alert-danger">Invalid username and password</div>
    </c:if>

    <div class="well">
        <c:url var="formAction" value="/pwd"/>
        <form method="post" name="forgot-password"
              class="form-horizontal" action="${formAction}">
            <legend>Forgot Password</legend>
            <div class="form-group">
                <label class="col-sm-3 control-label" for="username">Username</label>

                <div class="col-sm-7">
                    <input type="text" name="username" id="username" class="form-control">
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-3"></div>
                <div class="col-sm-7">
                    <input type="submit" id="submit" class="btn btn-primary btn-lg btn-sm-10"
                           value="Submit">
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        </form>

    </div>
</div>
</body>

</html>