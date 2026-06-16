<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Your To-Dos</title></head>
<body>
<h1>To-Dos for <c:out value="${username}"/></h1>

<form method="post" action="${pageContext.request.contextPath}/logout">
    <button type="submit">Log out</button>
</form>

<a href="${pageContext.request.contextPath}/add">Add a to-do</a>

<table border="1" cellpadding="6">
    <thead>
    <tr>
        <th>Title</th>
        <th>Status</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="todo" items="${todos}">
        <tr>
            <td><c:out value="${todo.title}"/></td>
            <td>
                <c:choose>
                    <c:when test="${todo.completed}">Done</c:when>
                    <c:otherwise>Not done</c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty todos}">
        <tr>
            <td colspan="2">No to-dos yet.</td>
        </tr>
    </c:if>
    </tbody>
</table>
</body>
</html>