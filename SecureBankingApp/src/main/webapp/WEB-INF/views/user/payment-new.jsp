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
<jsp:include page="../nav.jsp"></jsp:include>

<div class="container col-sm-6 col-sm-offset-3 ">


    <div class="well">
        <c:url var="formAction" value="/user/payment"/>
        <form:form method="post"
                   class="form-horizontal" commandName="transaction" action="${formAction}">
            <legend>Make a transfer</legend>
            <form:errors path="*" cssClass="alert alert-danger" element="div"></form:errors>
            <div class="form-group">
                <label class="col-sm-4 control-label" for="fromAccountNumber">From Account</label>

                <div class="col-sm-7">
                    <select class="form-control" id="fromAccountNumberId">
                        <c:forEach items="${accounts}" var="account">
                            <option value="${account.accountNum}"><c:out
                                    value="${account.accountNum} - ${account.accountType}"/></option>
                        </c:forEach>
                    </select>
                    <form:hidden path="fromAccountNumber" id="fromAccountNumber"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-4 control-label" for="toAccountNumber">To Account</label>

                <div class="col-sm-7">
                    <select class="form-control" id="toAccountNumberId">
                        <c:forEach items="${merchantAccounts}" var="account">
                            <option value="${account.accountNum}"><c:out
                                    value="${account.accountNum} - ${account.user.name}"/></option>
                        </c:forEach>
                    </select>
                    <form:hidden path="toAccountNumber" id="toAccountNumber"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-4 control-label" for="amountString">Amount</label>

                <div class="col-sm-7">
                    <form:input path="amountString" class="form-control" type="text"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-4"></div>
                <div class="col-sm-7">
                    <input type="submit" id="submit" class="btn btn-primary btn-lg btn-sm-10"
                           value="Submit">
                </div>
            </div>

        </form:form>

    </div>
</div>
<script type="text/javascript">
    $("#fromAccountNumberId").change(function () {
        $("#fromAccountNumber").val($(this).val());
    });

    $("#toAccountNumberId").change(function () {
        $("#toAccountNumber").val($(this).val());
    });

    $("#toAccountNumber").val($("#toAccountNumberId").val());
    $("#fromAccountNumber").val($("#fromAccountNumberId").val());
</script>

</body>
</html>