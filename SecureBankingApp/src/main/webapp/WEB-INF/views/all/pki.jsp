<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>XYZ Bank</title>

    <!-- include css -->
    <jsp:include page="../css.jsp"></jsp:include>

    <!-- include js -->
    <jsp:include page="../js.jsp"></jsp:include>
</head>
<body>
<jsp:include page="../nav.jsp"></jsp:include>
<div class="container col-sm-6 col-sm-offset-3 ">

    <div class="well">
        <c:url var="formAction" value="/all/${typeKey}"/>
        <form:form method="post"
                   class="form-horizontal"
                   action="${formAction}">
            <input type="hidden"
                   name="${_csrf.parameterName}"
                   value="${_csrf.token}"/>
            <legend>Please decrypt the following string using your Private key and enter the result below.
            </legend>

            <div class="form-group">
                <label class="col-sm-4 control-label" for="challengeString">Challenge String</label>

                <div class="col-sm-7">
                 <textarea class="form-control" rows="5" cols="50" id="challengeString"><c:out
                         value="${challengeString}"></c:out></textarea>
                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-4 control-label" for="secretCode">Secret Code</label>

                <div class="col-sm-7">
                    <input type="text" name="secret" class="form-control" id="secretCode">
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-4"></div>
                <div class="col-sm-7">
                    <input type="submit" id="submit"
                           class="btn btn-primary btn-lg btn-sm-10" value="Submit">
                </div>
            </div>

        </form:form>
    </div>
</div>
</body>
</html>