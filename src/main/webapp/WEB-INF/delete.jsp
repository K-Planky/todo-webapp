<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Delete to-do" scope="request"/>
<%@ include file="/WEB-INF/layout/header.jsp" %>

<div class="form-narrow mx-auto">
<a class="text-secondary small text-decoration-none d-inline-flex align-items-center gap-1 mb-3"
   href="${pageContext.request.contextPath}/todos">
    <i class="fa-solid fa-arrow-left"></i> Back to list
</a>

<div class="card">
    <div class="card-body p-4 p-sm-5">
        <div class="d-flex align-items-center gap-3 mb-4">
            <span class="d-inline-flex align-items-center justify-content-center flex-shrink-0 rounded-circle
                         text-danger" style="width:2.5rem;height:2.5rem;background:rgba(220,53,69,.1);">
                <i class="fa-solid fa-trash-can"></i>
            </span>
            <h1 class="h3 mb-0">Delete to-do?</h1>
        </div>

        <p class="text-secondary mb-2">You're about to permanently delete:</p>

        <div class="d-flex align-items-center gap-3 rounded px-3 py-3 mb-3"
             style="background: var(--neb-field); border: 1px solid var(--neb-border); border-left: 3px solid var(--bs-danger);">
            <i class="fa-lg ${todo.completed ? 'fa-solid fa-square-check text-success' : 'fa-regular fa-square text-secondary'}"></i>
            <span class="fw-medium text-body text-break"><c:out value="${todo.title}"/></span>
        </div>

        <p class="text-secondary small mb-4">
            <i class="fa-solid fa-circle-exclamation me-1"></i>This can't be undone.
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
</div>

<%@ include file="/WEB-INF/layout/footer.jsp" %>
