<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Delete To-Do</title></head>
<body>
<h1>Delete To-Do</h1>
<p>Delete "<c:out value='${todo.title}'/>"? This cannot be undone.</p>
<form method="post" action="${pageContext.request.contextPath}/delete">
    <input type="hidden" name="id" value="${todo.id}">
    <button type="submit">Delete</button>
    <a href="${pageContext.request.contextPath}/todos">Cancel</a>
</form>
</body>
</html>