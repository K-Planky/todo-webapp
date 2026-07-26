<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Log in" scope="request"/>
<c:set var="authLayout" value="true" scope="request"/>
<%@ include file="/WEB-INF/layout/header.jsp" %>

<div class="text-center mb-4">
    <span class="brand-mark brand-mark-lg mb-3"><%@ include file="/WEB-INF/layout/logo.jsp" %></span>
    <h1 class="h3 mb-1">Welcome back</h1>
    <p class="text-secondary mb-0">Sign in to pick up where you left off.</p>
</div>

<div class="card form-narrow w-100">
    <div class="card-body p-4 p-sm-5">

        <c:if test="${not empty message}">
            <div class="alert alert-success py-2 mb-3">
                <i class="fa-solid fa-circle-check me-1"></i>${message}
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2 mb-3">
                <i class="fa-solid fa-circle-exclamation me-1"></i>${error}
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <input type="hidden" name="csrfToken" value="<c:out value='${csrfToken}'/>">
            <div class="mb-3">
                <label class="form-label" for="username">Username</label>
                <input class="form-control" id="username" name="username" autofocus>
            </div>
            <div class="mb-4">
                <label class="form-label" for="password">Password</label>
                <input class="form-control" id="password" name="password" type="password" maxlength="72">
            </div>
            <button class="btn btn-primary w-100 py-2" type="submit">Log in</button>
        </form>
    </div>
</div>

<p class="text-center text-secondary small mt-4 mb-0">
    No account? <a href="${pageContext.request.contextPath}/register">Create one</a>
</p>

<%@ include file="/WEB-INF/layout/footer.jsp" %>
