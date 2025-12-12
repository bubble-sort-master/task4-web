<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
  Username: <input type="text" name="username"/>
  <br/>
  Password: <input type="password" name="pass"/>
  <br/>
  <input type="submit" value="Login"/>
  <br/>
  ${login_err}
  ${default_err}
  <c:if test="${not empty sessionScope.register_success}">
    <div style="color:green;">${sessionScope.register_success}</div>
    <c:remove var="register_success" scope="session"/>
  </c:if>

</form>
<br/>
<a href="pages/register.jsp">Зарегистрироваться</a>

</body>
</html>