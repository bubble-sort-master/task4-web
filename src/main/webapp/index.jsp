<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>BUBER TAXI</title>
</head>
<body>
<h1><%= "BUBER TAXI" %></h1>
<h2>Вход</h2>
<form action="controller" method="post">
  <input type="hidden" name="command" value="login"/>
  Login: <input type="text" name="login"/>
  <br/>
  Password: <input type="password" name="pass"/>
  <br/>
  <input type="submit" value="Login"/>
  <br/>
  ${login_err}
</form>
<br/>
<a href="pages/register.jsp">Зарегистрироваться</a>

</body>
</html>