<%@ page contentType="text/html;charset=UTF-8" %>
<%
  response.setHeader("Cache-Control", "no-store");
  response.setHeader("Pragma", "no-cache");
  response.setHeader("Expires", "0");
  response.setDateHeader("Expires", -1);
%>
<html>
<head>
  <title>Admin Main</title>
</head>
<body>
<h2>Welcome, ${firstName} ${lastName} (ADMIN)</h2>
<p>Username: ${username}</p>

<form action="${pageContext.request.contextPath}/controller" method="post">
  <input type="hidden" name="command" value="logout"/>
  <input type="submit" value="Logout"/>
</form>

<form action="${pageContext.request.contextPath}/controller" method="get">
  <input type="hidden" name="command" value="show_users"/>
  <input type="submit" value="Show All Users"/>
</form>
<form action="${pageContext.request.contextPath}/controller" method="get">
  <input type="hidden" name="command" value="show_cars"/>
  <input type="submit" value="Show All Cars"/>
</form>

<p style="color:red;">${admin_err}</p>
</body>
</html>
