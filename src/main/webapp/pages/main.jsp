<%@ page contentType="text/html;charset=UTF-8" %>
<%
  response.setHeader("Cache-Control", "no-store");
  response.setHeader("Pragma", "no-cache");
  response.setHeader("Expires", "0");
  response.setDateHeader("Expires", -1);
%>
<html>
<head>
  <title>Main</title>
</head>
<body>
<h2>Welcome, ${firstName} ${lastName} (CLIENT)</h2>
<p>Username: ${username}</p>

<br/><br/>
<form action="${pageContext.request.contextPath}/controller">
  <input type="hidden" name="command" value="logout"/>
  <input type="submit" value="Logout"/>
</form>
</body>
</html>
