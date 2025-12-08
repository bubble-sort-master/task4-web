<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
  <title>User List</title>
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
<h2 style="text-align:center;">All Users</h2>

<table>
  <thead>
  <tr>
    <th>ID</th>
    <th>Username</th>
    <th>First Name</th>
    <th>Last Name</th>
    <th>Role</th>
    <th>Bonus Points</th>
    <th>Banned</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="user" items="${users}">
    <tr>
      <td>${user.id}</td>
      <td>${user.username}</td>
      <td>${user.firstName}</td>
      <td>${user.lastName}</td>
      <td>${user.role}</td>
      <td>${user.bonusPoints}</td>
      <td>
        <c:choose>
          <c:when test="${user.banned}">Yes</c:when>
          <c:otherwise>No</c:otherwise>
        </c:choose>
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>

</body>
</html>
