<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Driver Main</title>
</head>
<body>
<h2>Welcome, ${firstName} ${lastName} (DRIVER)</h2>
<p>Username: ${username}</p>

<br/><br/>
<form action="${pageContext.request.contextPath}/controller">
  <input type="hidden" name="command" value="logout"/>
  <input type="submit" value="Logout"/>
</form>
</body>
</html>
