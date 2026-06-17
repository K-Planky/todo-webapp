<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Edit to-do" scope="request"/>
<%@ include file="/WEB-INF/layout/header.jsp" %>

<div class="card form-narrow mx-auto mt-4">
    <div class="card-body p-4">
        <h1 class="h4 mb-4">Edit to-do</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2 mb-3">
                <i class="fa-solid fa-circle-exclamation me-1"></i>${error}
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/edit">
            <input type="hidden" name="id" value="${todo.id}">
            <div class="mb-4">
                <label class="form-label" for="title">Title</label>
                <input class="form-control" id="title" name="title"
                       value="<c:out value='${todo.title}'/>" autofocus>
            </div>
            <div class="d-flex justify-content-end gap-2">
                <a class="btn btn-link text-secondary text-decoration-none"
                   href="${pageContext.request.contextPath}/todos">Cancel</a>
                <button class="btn btn-primary" type="submit">Save</button>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/layout/footer.jsp" %>