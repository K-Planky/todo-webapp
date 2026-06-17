<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Delete to-do" scope="request"/>
<%@ include file="/WEB-INF/layout/header.jsp" %>

<a class="text-secondary small text-decoration-none d-inline-flex align-items-center gap-1 mb-3"
   href="${pageContext.request.contextPath}/todos">
    <i class="fa-solid fa-arrow-left"></i> Back to list
</a>

<div class="card form-narrow">
    <div class="card-body p-4 p-sm-5">
        <div class="d-flex align-items-start gap-3 mb-3">
            <span class="d-inline-flex align-items-center justify-content-center flex-shrink-0 rounded-circle
                         text-danger" style="width:2.5rem;height:2.5rem;background:rgba(220,53,69,.1);">
                <i class="fa-solid fa-trash-can"></i>
            </span>
            <div>
                <h1 class="h4 mb-1">Delete to-do?</h1>
                <p class="text-secondary mb-0">
                    You're about to permanently delete
                    <strong class="text-body"><c:out value="${todo.title}"/></strong>. This can't be undone.
                </p>
            </div>
        </div>
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
