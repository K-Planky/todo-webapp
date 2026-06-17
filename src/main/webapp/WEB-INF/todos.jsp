<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Your to-dos" scope="request"/>
<%@ include file="/WEB-INF/layout/header.jsp" %>

<div class="d-flex align-items-center justify-content-between mb-4">
    <h1 class="h4 mb-0">Your to-dos</h1>
    <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/add">
        <i class="fa-solid fa-plus me-1"></i>Add
    </a>
</div>

<div class="card">
    <ul class="list-group list-group-flush">
        <c:forEach var="todo" items="${todos}">
            <li class="list-group-item d-flex align-items-center gap-3 py-3">

                <form method="post" action="${pageContext.request.contextPath}/toggle" class="m-0 d-flex">
                    <input type="hidden" name="id" value="${todo.id}">
                    <c:choose>
                        <c:when test="${todo.completed}">
                            <button type="submit" aria-label="Mark as not done"
                                    class="btn btn-link p-0 lh-1 text-decoration-none text-success">
                                <i class="fa-solid fa-circle-check fa-lg"></i>
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="submit" aria-label="Mark as done"
                                    class="btn btn-link p-0 lh-1 text-decoration-none text-secondary">
                                <i class="fa-regular fa-circle fa-lg"></i>
                            </button>
                        </c:otherwise>
                    </c:choose>
                </form>

                <span class="flex-grow-1 ${todo.completed ? 'text-decoration-line-through text-muted' : ''}">
                    <c:out value="${todo.title}"/>
                </span>

                <a class="btn btn-sm p-1 btn-action" aria-label="Edit"
                   href="${pageContext.request.contextPath}/edit?id=${todo.id}">
                    <i class="fa-solid fa-pen"></i>
                </a>
                <a class="btn btn-sm p-1 btn-action btn-action-danger" aria-label="Delete"
                   href="${pageContext.request.contextPath}/delete?id=${todo.id}">
                    <i class="fa-solid fa-trash-can"></i>
                </a>
            </li>
        </c:forEach>

        <c:if test="${empty todos}">
            <li class="list-group-item text-center text-muted py-5">
                <i class="fa-regular fa-circle-check d-block fs-3 mb-2 opacity-50"></i>
                Nothing here yet — add your first to-do.
            </li>
        </c:if>
    </ul>
</div>

<%@ include file="/WEB-INF/layout/footer.jsp" %>