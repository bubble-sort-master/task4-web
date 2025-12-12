<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  response.setHeader("Cache-Control", "no-store");
  response.setHeader("Pragma", "no-cache");
  response.setHeader("Expires", "0");
  response.setDateHeader("Expires", -1);
%>
<html>
<head>
  <title>Car List</title>
  <style>
      table {
          border-collapse: collapse;
          width: 80%;
          margin: 20px auto;
      }
      th, td {
          border: 1px solid #333;
          padding: 8px;
          text-align: center;
      }
      th {
          background-color: #eee;
      }
  </style>
</head>
<body>
<h2 style="text-align:center;">All Cars</h2>

<table>
  <thead>
  <tr>
    <th>ID</th>
    <th>Model</th>
    <th>Plate Number</th>
    <th>Year</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="car" items="${cars}">
    <tr>
      <td>${car.id}</td>
      <td>${car.model}</td>
      <td>${car.plateNumber}</td>
      <td>${car.year}</td>
    </tr>
  </c:forEach>
  </tbody>
</table>

</body>
</html>
