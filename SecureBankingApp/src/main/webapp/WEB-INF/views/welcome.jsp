<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${page.title}"/></title>

    <!-- include css -->
    <jsp:include page="css.jsp"></jsp:include>

    <!-- include js -->
    <jsp:include page="js.jsp"></jsp:include>
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

<div class="container">
    <div class="jumbotron">
        <h1>Bank of Upper Concourse</h1>

        <p>Hello. Welcome to our banking application. In order to login and use this application, please go through the
            steps provided in the <a href="http://www.public.asu.edu/~vuppulur/ss.pdf" target="_blank">User guide</a>
        </p>

        <p>
            <a class="btn btn-lg btn-primary" href="<c:url value='/login' />" role="button">Log In to Access »</a>
        </p>
    </div>
</div>
</body>

</html>