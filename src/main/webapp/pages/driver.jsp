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
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
  <style>
      .order-item { display: flex; justify-content: space-between; margin-bottom: 5px; }
      .accept-btn { margin-left: 10px; }
      .map-marker { font-weight: bold; color: red; }
  </style>
</head>
<body>
<h2>Welcome, ${firstName} ${lastName} (DRIVER)</h2>
<p>Username: ${username}</p>

<br/>
<c:if test="${driver_shift_active}">
  <p>Work shift started at: ${shift_start_time}</p>
  <p>Your car: ${car_model} (${car_plate_number})</p>
  <p>Your current location: (${driver_latitude};${driver_longitude})</p>

  <div id="map" class="location-map">
    <div class="location-cell"></div>
    <c:forEach var="col" begin="0" end="7">
      <div class="location-cell">${col}</div>
    </c:forEach>

    <c:forEach var="row" begin="0" end="7">
      <div class="location-cell">${row}</div>
      <c:forEach var="col" begin="0" end="7">
        <div class="location-cell" id="cell-${row}-${col}">
          <c:if test="${row == driver_latitude && col == driver_longitude}">
            <img src="${pageContext.request.contextPath}/images/current_location.png" alt="Driver location">
          </c:if>
        </div>
      </c:forEach>
    </c:forEach>
  </div>
</c:if>

<br/>
<form action="${pageContext.request.contextPath}/controller" method="post">
  <input type="hidden" name="command" value="driver_shift"/>
  <input type="submit" value="${driver_shift_active ? 'End shift' : 'Start shift'}"/>
</form>

<div id="ordersList">
  <p>Loading orders...</p>
</div>

<br/><br/>
<form action="${pageContext.request.contextPath}/controller" method="post">
  <input type="hidden" name="command" value="logout"/>
  <input type="submit" value="Logout"/>
</form>

<c:if test="${not empty driver_error}">
  <p style="color:red">${driver_error}</p>
  <c:remove var="driver_error" scope="session"/>
</c:if>

<script>
    function loadOrders() {
        fetch('${pageContext.request.contextPath}/controller?command=driver_order&action=search')
            .then(r => r.json())
            .then(data => {
                const list = document.getElementById("ordersList");
                list.innerHTML = "";
                document.querySelectorAll(".map-marker").forEach(el => el.remove());

                if (!Array.isArray(data) || data.length === 0) {
                    list.innerHTML = "<p>No orders assigned yet.</p>";
                    return;
                }

                const ul = document.createElement("ul");
                data.forEach((order, index) => {
                    const li = document.createElement("li");
                    li.className = "order-item";

                    const orderNumber = index + 1;
                    const text = "Order #" + orderNumber +
                        ": pickup (" + order.pickupLat + "," + order.pickupLon + ")" +
                        " → dropoff (" + order.dropoffLat + "," + order.dropoffLon + ")" +
                        ", price " + order.price;

                    const span = document.createElement("span");
                    span.textContent = text;

                    const btn = document.createElement("button");
                    btn.className = "accept-btn";
                    btn.textContent = "Accept";
                    btn.onclick = () => {
                        fetch('${pageContext.request.contextPath}/controller?command=driver_order&action=accept&orderId=' + order.id)
                            .then(r => r.json())
                            .then(resp => {
                                if (resp.accepted) {
                                    console.log("Order accepted successfully");
                                } else {
                                    console.log("Failed to accept order");
                                }
                            });
                    };

                    li.appendChild(span);
                    li.appendChild(btn);
                    ul.appendChild(li);

                    const cellId = "cell-" + order.pickupLat + "-" + order.pickupLon;
                    const cell = document.getElementById(cellId);
                    if (cell) {
                        const marker = document.createElement("span");
                        marker.className = "map-marker";
                        marker.textContent = orderNumber;
                        cell.appendChild(marker);
                    }
                });
                list.appendChild(ul);
            })
            .catch(err => console.error("Error loading orders", err));
    }


    setInterval(loadOrders, 5000);
    loadOrders();
</script>

</body>
</html>
