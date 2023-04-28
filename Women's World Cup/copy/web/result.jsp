<%--
  Created by IntelliJ IDEA.
  User: changruidi
  Date: 2022/9/21
  Time: 11:04 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>State: <%= request.getAttribute("State")  %></h2>
<h2>Population: <%= request.getAttribute("Population")  %></h2>
<h2>Nickname <%= request.getAttribute("Nickname")  %></h2>
<h2>Capital <%= request.getAttribute("Capital")  %></h2>
<h2>Song <img src="<%= request.getAttribute("Song")  %>"></h2>
<%--Credit: <%= request.getAttribute("Song")  %>--%>
<h2>Flag <img src="<%= request.getAttribute("Flag")  %>"></h2>
<%--Credit: <%= request.getAttribute("Flag")  %>--%>
<br>
<br>
<input type="submit" value="Continue"/>
</body>
</html>
