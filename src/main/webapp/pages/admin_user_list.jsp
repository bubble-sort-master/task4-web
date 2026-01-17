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
      button {
          padding: 5px 10px;
          cursor: pointer;
      }
  </style>

  <script>
      function toggleBan(userId, banned) {
          const action = banned ? "unban" : "ban";

          fetch("${pageContext.request.contextPath}/controller?command=USER_STATUS&action=" + action + "&userId=" + userId)
                  .then(r => r.json())
              .then(resp => {
                  if (resp.success) {
                      location.reload();
                  } else {
                      alert(resp.message || "Internal error");
                  }
              })
              .catch(err => console.error("Error updating user status", err));
      }
  </script>

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
    <th>Action</th>
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

      <td>
        <button onclick="toggleBan(${user.id}, ${user.banned})">
          <c:choose>
            <c:when test="${user.banned}">Unban</c:when>
            <c:otherwise>Ban</c:otherwise>
          </c:choose>
        </button>
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>

</body>
</html>
