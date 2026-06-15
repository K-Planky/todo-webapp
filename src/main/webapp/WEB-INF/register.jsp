<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Register</title></head>
<body>
<h1>Create an account</h1>
<p style="color:red">${error}</p>
<form method="post"
      action="${pageContext.request.contextPath}/register">
    <label>Username <input type="text" name="username"
                           value="<c:out value='${param.username}'/>"></label><br>
    <label>Password <input type="password"
                           name="password"></label><br>
    <button type="submit">Register</button>
    <a href="${pageContext.request.contextPath}/login">Cancel</a>
</form>
</body>
</html>