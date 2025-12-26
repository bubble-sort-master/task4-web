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
<form action="${pageContext.request.contextPath}/controller">
  <input type="hidden" name="command" value="logout"/>
  <input type="submit" value="Logout"/>
</form>
</body>
</html>
