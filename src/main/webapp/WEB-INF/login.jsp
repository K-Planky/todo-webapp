<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<body>
<h2>Login</h2>
<p style="color:green">${message}</p>
<p>${error}</p>
<form action="${pageContext.request.contextPath}/login" method="post">
    Username:<br/>
    <input type="text" name="username"/>
    <br/>
    Password:<br/>
    <input type="password" name="password">
    <br><br>
    <input type="submit" value="Submit">
</form>
</body>
</html>
