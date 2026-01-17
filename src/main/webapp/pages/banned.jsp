<%@ page contentType="text/html;charset=UTF-8" %>
<%
  response.setHeader("Cache-Control", "no-store");
  response.setHeader("Pragma", "no-cache");
  response.setHeader("Expires", "0");
  response.setDateHeader("Expires", -1);
%>

<html>
<head>
  <title>Access Denied</title>
  <style>
      body {
          text-align: center;
          margin-top: 100px;
          font-family: Arial, sans-serif;
      }
      h2 {
          color: red;
      }
      form {
          margin-top: 30px;
      }
  </style>
</head>
<body>

<h2>You have been banned</h2>
<p>${firstName} ${lastName}, your account is blocked.</p>

<form action="${pageContext.request.contextPath}/controller" method="post">
  <input type="hidden" name="command" value="logout"/>
  <input type="submit" value="Logout"/>
</form>

</body>
</html>
