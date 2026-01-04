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

<div id="driver-info"></div>

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

        <c:forEach var="driver" items="${nearest_drivers}">
          <c:if test="${row == driver.latitude && col == driver.longitude}">
            🚕
          </c:if>
        </c:forEach>
      </div>
    </c:forEach>
  </c:forEach>
</div>

<c:if test="${not empty avg_price}">
  <p>Approximate cost: ${avg_price}</p>
</c:if>
<c:if test="${empty nearest_drivers}">
  <p>No available cars</p>
</c:if>

<br/>
<form action="${pageContext.request.contextPath}/controller" method="post">
  <input type="hidden" name="command" value="client_order"/>
  <input type="hidden" name="action" value="search"/>

  <input type="hidden" name="pickupLat" value="${client_latitude}"/>
  <input type="hidden" name="pickupLon" value="${client_longitude}"/>

  <label for="dropoffLat">Dropoff Latitude:</label>
  <input type="text" id="dropoffLat" name="dropoffLat" required/>

  <label for="dropoffLon">Dropoff Longitude:</label>
  <input type="text" id="dropoffLon" name="dropoffLon" required/>

  <input type="submit" value="Search Nearest Cars"/>
</form>

<br/>

<c:if test="${not empty avg_price}">
  <form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="client_order"/>
    <input type="hidden" name="action" value="create"/>

    <input type="hidden" name="pickupLat" value="${client_latitude}"/>
    <input type="hidden" name="pickupLon" value="${client_longitude}"/>
    <input type="hidden" name="dropoffLat" value="${dropoffLat}"/>
    <input type="hidden" name="dropoffLon" value="${dropoffLon}"/>

    <input type="submit" value="Create Order"/>
  </form>
</c:if>

<br/>
<form action="${pageContext.request.contextPath}/controller">
  <input type="hidden" name="command" value="logout"/>
  <input type="submit" value="Logout"/>
</form>

<script>
    let orderId = "${order_id}";
    let pollingInterval;

    function checkOrderStatus() {
        fetch("${pageContext.request.contextPath}/controller?command=client_order&action=check_status&orderId=" + orderId)
            .then(response => response.json())
            .then(data => {
                if (data.status && data.status === "waiting") {
                    console.log("Order still waiting...");
                } else if (data.driverName) {
                    clearInterval(pollingInterval);
                    document.getElementById("driver-info").innerHTML =
                        "<h3>Your driver:</h3>" +
                        "<p>Name: " + data.driverName + "</p>" +
                        "<p>Car: " + data.carModel + " (" + data.carPlate + ")</p>" +
                        "<p>Current location: (" + data.currentLat + ";" + data.currentLon + ")</p>";
                }
            })
            .catch(err => console.error("Polling error", err));
    }

    if (orderId) {
        pollingInterval = setInterval(checkOrderStatus, 5000);
    }
</script>

</body>
</html>
