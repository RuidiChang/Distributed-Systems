<%--
  Created by IntelliJ IDEA.
  User: changruidi
  Date: 2022/9/18
  Time: 11:29 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Distributed Systems Class Clicker</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
  </head>
  <body>
  <form action="submit" method="post">
    <h1>Distributed Systems Class Clicker</h1>
    <br>
    <% if (request.getAttribute("PreviousChoice") != null) {%>
    <h2>Your "<%=request.getAttribute("PreviousChoice")%>" has been registered</h2>
    <%}%>
    <br>
    Submit your answer to current question<br>
    <label><input name="Choice" value="A" required type="radio" checked="true"/>A</label><br>
    <label><input name="Choice" value="B" required type="radio"/>B</label><br>
    <label><input name="Choice" value="C" required type="radio"/>C</label><br>
    <label><input name="Choice" value="D" required type="radio"/>D</label><br>
    <br>
    <br>
    <input type="submit" value="Submit"/>

  </form>
  </body>
</html>

