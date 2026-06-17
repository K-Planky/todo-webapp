<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Delete to-do" scope="request"/>
<%@ include file="/WEB-INF/layout/header.jsp" %>

<div class="card form-narrow mx-auto mt-4">
    <div class="card-body p-4">
        <h1 class="h4 mb-3">Delete to-do?</h1>
        <p class="text-secondary">
            You're about to permanently delete
            <strong class="text-body"><c:out value="${todo.title}"/></strong>. This can't be undone.
        </p>
        <form method="post" action="${pageContext.request.contextPath}/delete">
            <input type="hidden" name="id" value="${todo.id}">
            <div class="d-flex justify-content-end gap-2">
                <a class="btn btn-link text-secondary text-decoration-none"
                   href="${pageContext.request.contextPath}/todos">Cancel</a>
                <button class="btn btn-danger" type="submit"><i class="fa-solid fa-trash-can me-1"></i>Delete</button>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/layout/footer.jsp" %>