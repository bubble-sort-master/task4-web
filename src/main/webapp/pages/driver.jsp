<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  response.setHeader("Cache-Control", "no-store");
  response.setHeader("Pragma", "no-cache");
  response.setHeader("Expires", "0");
  response.setDateHeader("Expires", -1);
%>
<html>
<head>
  <title>Driver Main</title>
</head>
<body>
<h2>Welcome, ${firstName} ${lastName} (DRIVER)</h2>
<p>Username: ${username}</p>

<br/>
<c:if test="${driver_shift_active}">
  <p>Work shift started at: ${shift_start_time}</p>
  <p>Your car: ${car_model} (${car_plate})</p>
</c:if>

<br/>
<form action="${pageContext.request.contextPath}/controller" method="post">
  <input type="hidden" name="command" value="driver_shift"/>
  <input type="submit"
         value="${driver_shift_active ? 'End shift' : 'Start shift'}"/>
</form>

<br/><br/>
<form action="${pageContext.request.contextPath}/controller" method="post">
  <input type="hidden" name="command" value="logout"/>
  <input type="submit" value="Logout"/>
</form>

<c:if test="${not empty driver_error}">
  <p style="color:red">${driver_error}</p>
  <c:remove var="driver_error" scope="session"/>
</c:if>

</body>
</html>
