<%--
  Created by IntelliJ IDEA.
  User: changruidi
  Date: 2022/9/19
  Time: 12:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Distributed Systems Class Clicker</title>
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
</head>
<body>
<h1>Distributed Systems Class Clicker</h1>
<% if ((int) request.getAttribute("A") == 0 && (int) request.getAttribute("B") == 0
        && (int) request.getAttribute("C") == 0 && (int) request.getAttribute("D") == 0) {%>
<h2>There are currently no results</h2>
<% } else { %>
<% if ((int) request.getAttribute("A") != 0) { %>
<h2>A <%= request.getAttribute("A")  %>
</h2>
<% } %>
<% if ((int) request.getAttribute("B") != 0) { %>
<h2>B <%= request.getAttribute("B")  %>
</h2>
<% } %>
<% if ((int) request.getAttribute("C") != 0) { %>
<h2>C <%= request.getAttribute("C")  %>
</h2>
<% } %>
<% if ((int) request.getAttribute("D") != 0) { %>
<h2>D <%= request.getAttribute("D")  %>
</h2>
<% } %>
<% } %>
</body>
</html>
