<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Create account" scope="request"/>
<%@ include file="/WEB-INF/layout/header.jsp" %>

<div class="card form-narrow mx-auto mt-4">
    <div class="card-body p-4">
        <h1 class="h4 mb-4 text-center">Create your account</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2 mb-3">
                <i class="fa-solid fa-circle-exclamation me-1"></i>${error}
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/register">
            <div class="mb-3">
                <label class="form-label" for="username">Username</label>
                <input class="form-control" id="username" name="username"
                       value="<c:out value='${param.username}'/>" autofocus>
            </div>
            <div class="mb-3">
                <label class="form-label" for="password">Password</label>
                <input class="form-control" id="password" name="password" type="password">
            </div>
            <div class="mb-4">
                <label class="form-label" for="confirmPassword">Confirm password</label>
                <input class="form-control" id="confirmPassword" name="confirmPassword" type="password">
            </div>
            <button class="btn btn-primary w-100" type="submit">Create account</button>
        </form>

        <p class="text-center text-secondary small mt-4 mb-0">
            Already have an account? <a href="${pageContext.request.contextPath}/login">Log in</a>
        </p>
    </div>
</div>

<%@ include file="/WEB-INF/layout/footer.jsp" %>