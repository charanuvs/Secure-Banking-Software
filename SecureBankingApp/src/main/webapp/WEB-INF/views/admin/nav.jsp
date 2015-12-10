<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

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

        <p class="navbar-text">
            <small class="text-muted">Hi,</small>
            <span class="text-info"><c:out value="${sessionScope['loggedInUser'].name}"/></span></p>

        <div class="collapse navbar-collapse">
            <jsp:include page="../logout_inc.jsp"/>

            <ul class="nav navbar-nav navbar-right">
                <li class="active"><a href="<c:url value='/admin/user/add' />">Add User</a></li>
                <li class="active"><a href="<c:url value='/admin/user/list' />">List Users</a></li>
                <li class="active"><a href="<c:url value='/all/update' />">Update Info</a></li>
                <li class="active"><a href="<c:url value='/all/pwd' />">Change Password</a></li>
                <li class="active"><a href="<c:url value='/admin/systemlog' />">System Log</a></li>
            </ul>
        </div>
    </div>
    <!--/.container-fluid -->
</nav>