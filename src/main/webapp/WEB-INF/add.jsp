<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Add To-Do</title></head>
<body>
<h1>Add a To-Do</h1>
<p style="color:red">${error}</p>
<form method="post" action="${pageContext.request.contextPath}/add">
    <label>Title <input type="text" name="title" value="<c:out value='${param.title}'/>"></label>
    <button type="submit">Add</button>
    <a href="${pageContext.request.contextPath}/todos">Cancel</a>
</form>
</body>
</html>