<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head><title>Register</title></head>
<body>
<h2>Register</h2>
<form action="${pageContext.request.contextPath}/controller" method="post">
  <input type="hidden" name="command" value="register"/>
  Username: <input type="text" name="login"/><br/>
  Password: <input type="password" name="pass"/><br/>
  First name: <input type="text" name="firstName"/><br/>
  Last name: <input type="text" name="lastName"/><br/>
  <input type="submit" value="Register"/>
</form>
${register_err}
</body>
</html>
