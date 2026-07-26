<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Your to-dos" scope="request"/>
<%@ include file="/WEB-INF/layout/header.jsp" %>

<c:set var="doneCount" value="0"/>
<c:forEach var="t" items="${todos}">
    <c:if test="${t.completed}"><c:set var="doneCount" value="${doneCount + 1}"/></c:if>
</c:forEach>

<div class="d-flex align-items-start justify-content-between mb-4">
    <div class="d-flex align-items-baseline gap-3">
        <h1 class="h3 mb-0">Your to-dos</h1>
        <c:choose>
            <c:when test="${empty todos}">
                <span class="text-secondary small">No tasks yet</span>
            </c:when>
            <c:otherwise>
                <span class="text-secondary small">${doneCount} of ${todos.size()} done</span>
            </c:otherwise>
        </c:choose>
    </div>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/add">
        <i class="fa-solid fa-plus me-1"></i>Add
    </a>
</div>

<div class="card">
    <ul class="list-group list-group-flush">
        <c:forEach var="todo" items="${todos}">
            <li class="list-group-item todo-item d-flex align-items-center gap-3 py-3 px-4${todo.completed ? ' is-done' : ''}">

                <form method="post" action="${pageContext.request.contextPath}/toggle" class="m-0 d-flex">
                    <input type="hidden" name="csrfToken" value="<c:out value='${csrfToken}'/>">
                    <input type="hidden" name="id" value="${todo.id}">
                    <c:choose>
                        <c:when test="${todo.completed}">
                            <button type="submit" aria-label="Mark as not done"
                                    class="btn btn-link p-0 lh-1 text-decoration-none text-success">
                                <i class="fa-solid fa-square-check fa-lg"></i>
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="submit" aria-label="Mark as done"
                                    class="btn btn-link p-0 lh-1 text-decoration-none text-secondary">
                                <i class="fa-regular fa-square fa-lg"></i>
                            </button>
                        </c:otherwise>
                    </c:choose>
                </form>

                <span class="todo-title flex-grow-1 ${todo.completed ? 'text-decoration-line-through text-muted' : ''}">
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
                <p class="mb-1 fw-medium text-body">All clear.</p>
                <p class="small mb-0">Add your first to-do to get started.</p>
            </li>
        </c:if>
    </ul>
</div>

<%@ include file="/WEB-INF/layout/footer.jsp" %>
