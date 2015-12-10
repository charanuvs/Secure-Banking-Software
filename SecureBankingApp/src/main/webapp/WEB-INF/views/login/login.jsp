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
        <c:url var="formAction" value="/loginAuth"/>
        <form:form method="POST" name="login"
                   class="form-horizontal" commandName="loginFormBean" action="${formAction}">
            <legend>Login</legend>
            <div class="form-group">
                <label class="col-sm-3 control-label" for="username">Username</label>

                <div class="col-sm-7">
                    <form:input path="username" class="form-control osk-trigger" type="text"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label" for="username">Password</label>

                <div class="col-sm-7">
                    <form:input path="password" class="form-control osk-trigger" type="password"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-3"></div>
                <div class="col-sm-7">
                    <input type="submit" id="submit" class="btn btn-primary btn-lg btn-sm-10"
                           value="Login">
                    <a href="<c:url value="/pwd" />" style="margin-left: 10px;">Forgot password</a>
                </div>
            </div>

        </form:form>

    </div>
</div>

<script type="text/javascript">
    $(function () {
        $('#loginFormBean .osk-trigger').onScreenKeyboard({
            rewireReturn: 'submit',
            rewireTab: true
        });
    });
</script>
</body>

</html>