<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Edit To-Do</title></head>
<body>
<h1>Edit To-Do</h1>
<p style="color:red">${error}</p>
<form method="post" action="${pageContext.request.contextPath}/edit">
    <input type="hidden" name="id" value="${todo.id}">
    <label>Title <input type="text" name="title" value="<c:out value='${todo.title}'/>"></label>
    <button type="submit">Save</button>
    <a href="${pageContext.request.contextPath}/todos">Cancel</a>
</form>
</body>
</html>