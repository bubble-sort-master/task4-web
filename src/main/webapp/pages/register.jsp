<%@ page contentType="text/html;charset=UTF-8" %>
<%
  response.setHeader("Cache-Control", "no-store");
  response.setHeader("Pragma", "no-cache");
  response.setHeader("Expires", "0");
  response.setDateHeader("Expires", -1);
%>
<html>
<head><title>Register</title></head>
<body>
<h2>Register</h2>
<form action="${pageContext.request.contextPath}/controller" method="post">
  <input type="hidden" name="command" value="register"/>
  Username: <input type="text" name="username" required/><br/>
  Password: <input type="password" name="pass" required/><br/>
  First name: <input type="text" name="firstName" required/><br/>
  Last name: <input type="text" name="lastName"/><br/>

  <input type="submit" value="Register"/>
</form>
${register_err}
</body>
</html>
