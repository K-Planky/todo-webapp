<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Add a to-do" scope="request"/>
<%@ include file="/WEB-INF/layout/header.jsp" %>

<div class="form-narrow mx-auto">
<a class="text-secondary small text-decoration-none d-inline-flex align-items-center gap-1 mb-3"
   href="${pageContext.request.contextPath}/todos">
    <i class="fa-solid fa-arrow-left"></i> Back to list
</a>

<div class="card">
    <div class="card-body p-4 p-sm-5">
        <h1 class="h3 mb-4">Add a to-do</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2 mb-3">
                <i class="fa-solid fa-circle-exclamation me-1"></i>${error}
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/add">
            <div class="mb-4">
                <label class="form-label" for="title">Title</label>
                <input class="form-control" id="title" name="title" maxlength="255"
                       value="<c:out value='${param.title}'/>" autofocus>
            </div>
            <div class="d-flex justify-content-end gap-2">
                <a class="btn btn-link text-secondary text-decoration-none"
                   href="${pageContext.request.contextPath}/todos">Cancel</a>
                <button class="btn btn-primary" type="submit"><i class="fa-solid fa-plus me-1"></i>Add</button>
            </div>
        </form>
    </div>
</div>
</div>

<%@ include file="/WEB-INF/layout/footer.jsp" %>
