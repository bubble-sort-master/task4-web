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
  <title>Main</title>
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<h2>Welcome, ${firstName} ${lastName} (CLIENT)</h2>
<p>Username: ${username}</p>
<br/><br/>
<p>Your current location: (${client_latitude};${client_longitude})</p>
<br/>
<div class="location-map">
  <div class="location-cell"></div>
  <c:forEach var="col" begin="0" end="7">
    <div class="location-cell">${col}</div>
  </c:forEach>

  <c:forEach var="row" begin="0" end="7">
    <div class="location-cell">${row}</div>
    <c:forEach var="col" begin="0" end="7">
      <div class="location-cell">
        <c:if test="${row == client_latitude && col == client_longitude}">
          <img src="${pageContext.request.contextPath}/images/current_location.png" alt="You are here">
        </c:if>
      </div>
    </c:forEach>
  </c:forEach>
</div>
<br/>
<!-- Форма для создания заказа -->
<form action="${pageContext.request.contextPath}/controller" method="post">
  <input type="hidden" name="command" value="client_order"/>
  <input type="hidden" name="action" value="create"/>

  <!-- pickup координаты берём из сессии -->
  <input type="hidden" name="pickupLat" value="${client_latitude}"/>
  <input type="hidden" name="pickupLon" value="${client_longitude}"/>

  <!-- dropoff координаты вводит пользователь -->
  <label for="dropoffLat">Dropoff Latitude:</label>
  <input type="text" id="dropoffLat" name="dropoffLat" required/>

  <label for="dropoffLon">Dropoff Longitude:</label>
  <input type="text" id="dropoffLon" name="dropoffLon" required/>

  <input type="submit" value="Create Order"/>
</form>

<br/>
<form action="${pageContext.request.contextPath}/controller">
  <input type="hidden" name="command" value="logout"/>
  <input type="submit" value="Logout"/>
</form>
</body>
</html>
